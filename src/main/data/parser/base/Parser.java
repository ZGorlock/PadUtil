/*
 * File:    Parser.java
 * Package: main.data.parser.base
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser.base;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import commons.access.Filesystem;
import commons.lambda.stream.collector.MapCollectors;
import commons.lambda.stream.mapper.Mappers;
import commons.object.collection.ListUtility;
import commons.object.string.StringUtility;
import main.data.mirror.DataMirror;
import main.data.mirror.host.ErrorResponse;
import main.data.parser.DataParser;
import main.entity.base.Entity;
import main.entity.base.parser.EntityParser;

public abstract class Parser<T extends Entity> {
    
    //Static Fields
    
    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(new EntityParser())
            .create();
    
    
    //Methods
    
    public Map<File, T> parseAll() {
        return tryParseAll()
                .orElseGet(ErrorResponse.invoke(Collections.emptyMap(), "Failed to parse all " + getTypeName(true)));
    }
    
    public Optional<Map<File, T>> tryParseAll() {
        return tryEnumerateFiles()
                .flatMap(this::tryParse);
    }
    
    public List<File> enumerateFiles() {
        return tryEnumerateFiles()
                .orElseGet(ErrorResponse.invoke(Collections.emptyList(), "Failed to enumerate files"));
    }
    
    public abstract Optional<List<File>> tryEnumerateFiles();
    
    public Map<File, T> parse(List<File> fileList) {
        return tryParse(fileList)
                .orElseGet(ErrorResponse.invoke("Failed to parse " + getTypeName(true)));
    }
    
    public Optional<Map<File, T>> tryParse(List<File> fileList) {
        return Optional.ofNullable(fileList)
                .filter(dataUrlList -> !ListUtility.isNullOrEmpty(dataUrlList))
                .stream().flatMap(Collection::stream)
                .map(Mappers.forEach(e -> Optional.ofNullable(e)
                        .map(file -> Optional.ofNullable(fileList).map(list -> (" " + (list.indexOf(file) + 1) + " / " + list.size() + " ")).orElse(""))
                        .map(separator -> StringUtility.pad(separator, 80, '-')).map(separator -> (System.lineSeparator() + separator))
                        .ifPresent(System.out::println)))
                .map(file -> tryParse(file)
                        .map(data -> Map.entry(file, data))
                        .orElse(null))
                .filter(Objects::nonNull)
                .peek(e -> e.getValue().store())
                .collect(Collectors.collectingAndThen(MapCollectors.toLinkedHashMap(), Optional::of));
    }
    
    public T parse(File file) {
        return tryParse(file)
                .orElseGet(ErrorResponse.invoke("Failed to parse " + getTypeName() + ": " + Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    public Optional<T> tryParse(File file) {
        return Optional.ofNullable(file)
                .map(x -> Mappers.perform(x, e -> System.out.println("Parsing " + getTypeName() + ": " + x.getAbsolutePath())))
                .filter(e -> permitEntityCacheRead())
                .map(this::tryLoadCached)
                .filter(Optional::isPresent)
                .orElseGet(() -> tryParseSource(file));
    }
    
    public T loadCached(File file) {
        return tryLoadCached(file)
                .orElseGet(ErrorResponse.invoke("Failed to load cached " + getTypeName() + ": " + Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    public Optional<T> tryLoadCached(File file) {
        return Optional.of(file)
                .flatMap(this::getLocalFile)
                .map(x -> Mappers.perform(x, e -> System.out.println("Attempting to load cached " + getTypeName() + "...")))
                .filter(this::localFilePresent)
                .map(x -> Mappers.perform(x, e -> System.out.println("Loading cached " + getTypeName() + ": " + x.getAbsolutePath())))
                .flatMap(this::tryLoadData);
    }
    
    public T parseSource(File file, boolean overwrite) {
        return tryParseSource(file, overwrite)
                .orElseGet(ErrorResponse.invoke("Failed to parse " + getTypeName() + ": " + Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    public T parseSource(File file) {
        return parseSource(file, false);
    }
    
    public Optional<T> tryParseSource(File file, boolean overwrite) {
        return Optional.of(file)
                .map(x -> Mappers.perform(x, e -> System.out.println("Attempting to parse " + getTypeName() + "...")))
                .flatMap(this::tryParseData)
                .filter(data -> getLocalFile(file)
                        .filter(localFile -> permitEntityCacheWrite() && (overwrite || localFileNotPresent(localFile)) && Filesystem.createDirectory(localFile.getParentFile()))
                        .map(x -> Mappers.perform(x, e -> System.out.println("Saving parsed " + getTypeName() + ": " + x.getAbsolutePath())))
                        .map(localFile -> saveData(localFile, data))
                        .orElse(true));
    }
    
    public Optional<T> tryParseSource(File file) {
        return tryParseSource(file, false);
    }
    
    public T parseData(File file) {
        return tryParseData(file)
                .orElseGet(ErrorResponse.invoke("Failed to parse " + getTypeName() + ": " + Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    public abstract Optional<T> tryParseData(File file);
    
    public T loadData(File file) {
        return tryLoadData(file)
                .orElseGet(ErrorResponse.invoke("Failed to load " + getTypeName() + ": " + Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    public Optional<T> tryLoadData(File file) {
        return Optional.ofNullable(file)
                .map(Filesystem::readFileToString)
                .flatMap(this::tryReadFromJson);
    }
    
    public T readFromJson(String json) {
        return tryReadFromJson(json)
                .orElseGet(ErrorResponse.invoke("Failed to read " + getTypeName() + " json"));
    }
    
    public Optional<T> tryReadFromJson(String json) {
        return Optional.ofNullable(json)
                .map(e -> gson.fromJson(json, getTypeClass()));
    }
    
    public boolean saveData(File file, T data) {
        return trySaveData(file, data)
                .orElseGet(ErrorResponse.invoke("Failed to save " + getTypeName() + ": " + Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    public Optional<Boolean> trySaveData(File file, T data) {
        return Optional.ofNullable(data)
                .map(this::saveToJson)
                .map(json -> Filesystem.writeStringToFile(file, json));
    }
    
    public String saveToJson(T data) {
        return trySaveToJson(data)
                .orElseGet(ErrorResponse.invoke("Failed to save " + getTypeName() + " json"));
    }
    
    public Optional<String> trySaveToJson(T data) {
        return Optional.ofNullable(data)
                .map(gson::toJson);
    }
    
    public Optional<File> getLocalFile(File sourceFile) {
        return Optional.ofNullable(sourceFile)
                .map(File::getAbsolutePath).map(e -> e.replace("\\", "/"))
                .map(e -> e.replaceAll(("^(.+)/\\.mirror/[^/]+/(.+)\\.html$"), "$1/entity/$2.json"))
                .map(File::new);
    }
    
    protected boolean localFilePresent(File localFile) {
        return DataMirror.localFileExists(localFile);
    }
    
    protected boolean localFileNotPresent(File localFile) {
        return DataMirror.localFileNotExists(localFile);
    }
    
    protected boolean permitEntityCacheRead() {
        return DataParser.READ_CACHE;
    }
    
    protected boolean permitEntityCacheWrite() {
        return DataParser.WRITE_CACHE;
    }
    
    
    //Getters
    
    protected String getTypeName(boolean plural) {
        return "data";
    }
    
    protected final String getTypeName() {
        return getTypeName(false);
    }
    
    protected abstract Class<T> getTypeClass();
    
    protected abstract String getCategory();
    
    
    //Static Methods
    
    public static <T> String toJson(T entity) {
        return gson.toJson(entity);
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
    
    public static <T> T fromJson(File jsonFile, Class<T> clazz) {
        return fromJson(Filesystem.readFileToString(jsonFile), clazz);
    }
    
    public static <T> T fromJson(String json) {
        return gson.fromJson(json, new TypeToken<T>() {
        }.getType());
    }
    
    public static <T> T fromJson(File jsonFile) {
        return fromJson(Filesystem.readFileToString(jsonFile));
    }
    
}
