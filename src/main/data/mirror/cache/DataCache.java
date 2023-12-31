/*
 * File:    MirrorIndex.java
 * Package: main.data.mirror.cache
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.mirror.cache;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import commons.access.Filesystem;
import commons.object.collection.map.BiMap;
import commons.object.string.StringUtility;
import main.data.mirror.DataMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCache {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(DataCache.class);
    
    
    //Constants
    
    public static final File INDEX_DIR = new File(DataMirror.MIRROR_DIR, ".index");
    
    public static final File SEARCH_PAGE_INDEX = new File(INDEX_DIR, "search.csv");
    
    public static final File MONSTER_PAGE_INDEX = new File(INDEX_DIR, "monster.csv");
    
    public static final File DUNGEON_PAGE_INDEX = new File(INDEX_DIR, "dungeon.csv");
    
    public static final File SUB_DUNGEON_PAGE_INDEX = new File(INDEX_DIR, "sub-dungeon.csv");
    
    public static final File IMAGE_RESOURCES_INDEX = new File(INDEX_DIR, "image.csv");
    
    
    //Static Fields
    
    public static final BiMap<String, String> searchPages = new BiMap<>();
    
    public static final BiMap<String, String> monsterPages = new BiMap<>();
    
    public static final BiMap<String, String> dungeonPages = new BiMap<>();
    
    public static final BiMap<String, String> subDungeonPages = new BiMap<>();
    
    public static final BiMap<String, String> imageResources = new BiMap<>();
    
    private static final String INDEX_SEPARATOR = "|";
    
    private static final String INDEX_URL_FILE_TYPE = "~";
    
    
    //Static Methods
    
    private static File performLookup(BiMap<String, String> data, String url) {
        return Optional.ofNullable(data)
                .flatMap(e -> Optional.ofNullable(url)
                        .map(DataCache::getUrlKey)
                        .map(e::get))
                .map(File::new)
                .orElse(null);
    }
    
    public static File performLookup(BiMap<String, String> data, URL url) {
        return Optional.ofNullable(url)
                .map(URL::toExternalForm)
                .map(urlString -> performLookup(data, urlString))
                .orElse(null);
    }
    
    public static File lookup(String url) {
        return Stream.of(searchPages, monsterPages, dungeonPages, subDungeonPages, imageResources)
                .map(e -> performLookup(e, url))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
    
    public static File lookup(URL url) {
        return Optional.ofNullable(url)
                .map(URL::toExternalForm)
                .map(DataCache::lookup)
                .orElse(null);
    }
    
    public static File lookupSearchPage(String url) {
        return performLookup(searchPages, url);
    }
    
    public static File lookupSearchPage(URL url) {
        return performLookup(searchPages, url);
    }
    
    public static File lookupMonsterPage(String url) {
        return performLookup(monsterPages, url);
    }
    
    public static File lookupMonsterPage(URL url) {
        return performLookup(monsterPages, url);
    }
    
    public static File lookupDungeonPage(String url) {
        return performLookup(dungeonPages, url);
    }
    
    public static File lookupDungeonPage(URL url) {
        return performLookup(dungeonPages, url);
    }
    
    public static File lookupSubDungeonPage(String url) {
        return performLookup(subDungeonPages, url);
    }
    
    public static File lookupSubDungeonPage(URL url) {
        return performLookup(subDungeonPages, url);
    }
    
    public static File lookupImageResource(String url) {
        return performLookup(imageResources, url);
    }
    
    public static File lookupImageResource(URL url) {
        return performLookup(imageResources, url);
    }
    
    private static String performReverseLookup(BiMap<String, String> data, String filePath) {
        return Optional.ofNullable(data)
                .flatMap(dataMap -> Optional.ofNullable(filePath)
                        .map(DataCache::getFileKey)
                        .map(dataMap::inverseGet)
                        .map(dataKey -> dataKey.replaceAll(("(?<=\\.)" + INDEX_URL_FILE_TYPE + "$"), Filesystem.getFileType(new File(filePath)))))
                .orElse(null);
    }
    
    private static String performReverseLookup(BiMap<String, String> data, File file) {
        return Optional.ofNullable(file)
                .map(StringUtility::fileString)
                .map(filePath -> performReverseLookup(data, filePath))
                .orElse(null);
    }
    
    public static String reverseLookup(String filePath) {
        return Stream.of(searchPages, monsterPages, dungeonPages, subDungeonPages, imageResources)
                .map(dataMap -> performReverseLookup(dataMap, filePath))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
    
    public static String reverseLookup(File file) {
        return Optional.ofNullable(file)
                .map(StringUtility::fileString)
                .map(DataCache::reverseLookup)
                .orElse(null);
    }
    
    public static String lookupSearchPageUrlKey(String filePath) {
        return performReverseLookup(searchPages, filePath);
    }
    
    public static String lookupSearchPageUrlKey(File file) {
        return performReverseLookup(searchPages, file);
    }
    
    public static String lookupMonsterUrlKey(String filePath) {
        return performReverseLookup(monsterPages, filePath);
    }
    
    public static String lookupMonsterUrlKey(File file) {
        return performReverseLookup(monsterPages, file);
    }
    
    public static String lookupDungeonUrlKey(String filePath) {
        return performReverseLookup(dungeonPages, filePath);
    }
    
    public static String lookupDungeonUrlKey(File file) {
        return performReverseLookup(dungeonPages, file);
    }
    
    public static String lookupSubDungeonUrlKey(String filePath) {
        return performReverseLookup(subDungeonPages, filePath);
    }
    
    public static String lookupSubDungeonUrlKey(File file) {
        return performReverseLookup(subDungeonPages, file);
    }
    
    public static String lookupImageUrlKey(String filePath) {
        return performReverseLookup(imageResources, filePath);
    }
    
    public static String lookupImageUrlKey(File file) {
        return performReverseLookup(imageResources, file);
    }
    
    public static String getUrlKey(String url) {
        return Optional.ofNullable(url)
                .map(e -> e.replaceAll("(?<=/)([^/]+)\\.[^/.]+$", ("$1." + INDEX_URL_FILE_TYPE)))
                .orElse(null);
    }
    
    public static String getUrlKey(URL url) {
        return Optional.ofNullable(url)
                .map(URL::toExternalForm)
                .map(DataCache::getUrlKey)
                .orElse(null);
    }
    
    public static String getFileKey(String filePath) {
        return Optional.ofNullable(filePath)
                .map(e -> e.replaceAll("^.*/?(data/\\.mirror/)", "$1"))
                .orElse(null);
    }
    
    public static String getFileKey(File file) {
        return Optional.ofNullable(file)
                .map(StringUtility::fileString)
                .map(DataCache::getFileKey)
                .orElse(null);
    }
    
    public static void cacheData(BiMap<String, String> cache, Map<String, File> data) {
        cache.clear();
        data.forEach((url, file) -> cache.put(getUrlKey(url), getFileKey(file)));
    }
    
    public static void load() {
        loadSearchPageIndex();
        loadMonsterPageIndex();
        loadDungeonPageIndex();
        loadSubDungeonPageIndex();
        loadImageResourceIndex();
    }
    
    public static Map<String, String> loadSearchPageIndex() {
        logger.info("Loading Search Page Index...\n\n");
        return loadDataIndex(searchPages, SEARCH_PAGE_INDEX);
    }
    
    public static Map<String, String> loadMonsterPageIndex() {
        logger.info("Loading Monster Page Index...\n\n");
        return loadDataIndex(monsterPages, MONSTER_PAGE_INDEX);
    }
    
    public static Map<String, String> loadDungeonPageIndex() {
        logger.info("Loading Dungeon Page Index...\n\n");
        return loadDataIndex(dungeonPages, DUNGEON_PAGE_INDEX);
    }
    
    public static Map<String, String> loadSubDungeonPageIndex() {
        logger.info("Loading Sub-Dungeon Page Index...\n\n");
        return loadDataIndex(subDungeonPages, SUB_DUNGEON_PAGE_INDEX);
    }
    
    public static Map<String, String> loadImageResourceIndex() {
        logger.info("Loading Image Resource Index...\n\n");
        return loadDataIndex(imageResources, IMAGE_RESOURCES_INDEX);
    }
    
    private static Map<String, String> loadDataIndex(Map<String, String> dataMap, File indexFile) {
        Filesystem.readLines(indexFile).stream()
                .map(e -> e.split(Pattern.quote(INDEX_SEPARATOR), -1)).filter(e -> (e.length == 2))
                .filter(e -> !StringUtility.isNullOrBlank(e[0])).filter(e -> !StringUtility.isNullOrBlank(e[1]))
                .map(e -> Map.entry(e[0], new File(e[1])))
                .filter(e -> e.getValue().exists())
                .forEachOrdered(e -> dataMap.put(e.getKey(), getFileKey(e.getValue())));
        return dataMap;
    }
    
    public static void save() {
        saveSearchPageIndex();
        saveMonsterPageIndex();
        saveDungeonPageIndex();
        saveSubDungeonPageIndex();
        saveImageResourceIndex();
    }
    
    public static boolean saveSearchPageIndex() {
        logger.info("Saving Search Page Index...\n\n");
        return saveDataIndex(searchPages, SEARCH_PAGE_INDEX);
    }
    
    public static boolean saveMonsterPageIndex() {
        logger.info("Saving Monster Page Index...\n\n");
        return saveDataIndex(monsterPages, MONSTER_PAGE_INDEX);
    }
    
    public static boolean saveDungeonPageIndex() {
        logger.info("Saving Dungeon Page Index...\n\n");
        return saveDataIndex(dungeonPages, DUNGEON_PAGE_INDEX);
    }
    
    public static boolean saveSubDungeonPageIndex() {
        logger.info("Saving Sub-Dungeon Page Index...\n\n");
        return saveDataIndex(subDungeonPages, SUB_DUNGEON_PAGE_INDEX);
    }
    
    public static boolean saveImageResourceIndex() {
        logger.info("Saving Image Resource Index...\n\n");
        return saveDataIndex(imageResources, IMAGE_RESOURCES_INDEX);
    }
    
    private static boolean saveDataIndex(Map<String, String> dataMap, File indexFile) {
        return Optional.ofNullable(dataMap)
                .map(Map::entrySet).filter(e -> !e.isEmpty())
                .map(dataEntries -> dataEntries.stream()
                        .filter(e -> !StringUtility.isNullOrBlank(e.getKey())).filter(e -> Objects.nonNull(e.getValue()))
                        .map(e -> String.join(INDEX_SEPARATOR, e.getKey(), e.getValue()))
                        .collect(Collectors.toList()))
                .map(dataLines -> Filesystem.writeLines(indexFile, dataLines))
                .orElse(false);
    }
    
}
