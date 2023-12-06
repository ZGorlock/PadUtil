/*
 * File:    Scraper.java
 * Package: main.data.scraper.base
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.scraper.base;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import commons.access.Filesystem;
import commons.lambda.stream.collector.MapCollectors;
import commons.lambda.stream.mapper.Mappers;
import commons.object.collection.ListUtility;
import commons.object.string.StringUtility;
import main.data.mirror.host.ErrorResponse;
import main.data.mirror.host.LinkExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Scraper<T> {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(Scraper.class);
    
    
    //Constants
    
    public static final boolean USE_DELAY = true;
    
    public static final Pattern URL_PATTERN = Pattern.compile("(?<url>(?<host>\\w+://[^/]+/)" + "(?<location>(?<path>(?:[\\w\\-]*(?:/|$))*)(?<name>[\\w\\-]+\\.\\w+|)))");
    
    public static final Pattern URL_EXTRACTOR_PATTERN = Pattern.compile("(?<=\")" + URL_PATTERN.pattern() + "(?=\")");
    
    
    //Methods
    
    public Map<String, File> fetchAll() {
        return tryFetchAll()
                .orElseGet(ErrorResponse.invoke(Collections.emptyMap(), getLogger(), "Failed to fetch all {}", getTypeName(true)));
    }
    
    public Optional<Map<String, File>> tryFetchAll() {
        return tryEnumerateUrls()
                .flatMap(this::tryFetch);
    }
    
    public List<String> enumerateUrls() {
        return tryEnumerateUrls()
                .orElseGet(ErrorResponse.invoke(Collections.emptyList(), getLogger(), "Failed to enumerate urls"));
    }
    
    public Optional<List<String>> tryEnumerateUrls() {
        return LinkExtractor.extractLinks(getUrlExtractorPattern(), getUrlRoot());
    }
    
    public Map<String, File> fetch(List<String> urlList) {
        return tryFetch(urlList)
                .orElseGet(ErrorResponse.invoke(getLogger(), "Failed to fetch {}", getTypeName(true)));
    }
    
    public Optional<Map<String, File>> tryFetch(List<String> urlList) {
        return Optional.ofNullable(urlList)
                .filter(dataUrlList -> !ListUtility.isNullOrEmpty(dataUrlList))
                .stream().flatMap(Collection::stream)
                .map(Mappers.forEach(e -> Optional.ofNullable(e)
                        .map(url -> Optional.ofNullable(urlList).map(list -> (" " + (list.indexOf(url) + 1) + " / " + list.size() + " ")).orElse(""))
                        .map(separator -> StringUtility.pad(separator, 80, '-')).map(separator -> (System.lineSeparator() + separator))
                        .ifPresent(getLogger()::trace)))
                .map(url -> tryFetch(url)
                        .map(data -> Map.entry(url, data))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(MapCollectors.toLinkedHashMap(), Optional::of));
    }
    
    public File fetch(String url) {
        return tryFetch(url)
                .orElseGet(ErrorResponse.invoke(getLogger(), "Failed to fetch {}: {}", getTypeName(), url));
    }
    
    public Optional<File> tryFetch(String url) {
        return Optional.ofNullable(url)
                .map(x -> Mappers.perform(x, e -> getLogger().trace("Fetching {}: {}", getTypeName(), x)))
                .map(this::tryFetchCached)
                .filter(Optional::isPresent)
                .orElseGet(() -> tryCreateCache(url));
    }
    
    public File fetchCached(String url) {
        return tryFetchCached(url)
                .orElseGet(ErrorResponse.invoke(getLogger(), "Failed to fetch cached {}: {}", getTypeName(), url));
    }
    
    public Optional<File> tryFetchCached(String url) {
        return Optional.of(url)
                .flatMap(this::getLocalFile)
                .map(x -> Mappers.perform(x, e -> getLogger().trace("Attempting to load cached {}...", getTypeName())))
                .filter(this::localFilePresent)
                .map(x -> Mappers.perform(x, e -> getLogger().trace("Loaded cached {}: {}", getTypeName(), x)));
    }
    
    public File createCache(String url, boolean overwrite) {
        return tryCreateCache(url, overwrite)
                .orElseGet(ErrorResponse.invoke(getLogger(), "Failed to cache {}: {}", getTypeName(), url));
    }
    
    public File createCache(String url) {
        return createCache(url, false);
    }
    
    public Optional<File> tryCreateCache(String url, boolean overwrite) {
        return Optional.of(url)
                .map(x -> Mappers.perform(x, e -> getLogger().trace("Attempting to download remote {}...", getTypeName())))
                .map(this::scrapeData)
                .flatMap(data -> getLocalFile(url)
                        .filter(localFile -> (overwrite || localFileNotPresent(localFile)) && Filesystem.createDirectory(localFile.getParentFile()))
                        .map(x -> Mappers.perform(x, e -> getLogger().debug("Saving cached {}: {}", getTypeName(), x)))
                        .filter(localFile -> saveData(localFile, data)))
                .map(Mappers.forEach(e -> delay(getDelayRms())));
    }
    
    public Optional<File> tryCreateCache(String url) {
        return tryCreateCache(url, false);
    }
    
    public T scrapeData(String url) {
        return tryScrapeData(url)
                .orElseGet(ErrorResponse.invoke(getLogger(), "Failed to scrape {}: {}", getTypeName(), url));
    }
    
    public Optional<T> tryScrapeData(String url) {
        return Optional.of(url)
                .map(x -> Mappers.perform(x, e -> getLogger().debug("Scraping {}: {}", getTypeName(), x)))
                .map(this::downloadData);
    }
    
    protected T downloadData(String url) {
        return tryDownloadData(url)
                .orElseGet(ErrorResponse.invoke(getLogger(), "Failed to download {} from: {}", getTypeName(), url));
    }
    
    protected abstract Optional<T> tryDownloadData(String url);
    
    protected boolean saveData(File file, T data) {
        return trySaveData(file, data)
                .orElseGet(ErrorResponse.invoke(false, getLogger(), "Failed to save {}: {}", getTypeName(), Optional.ofNullable(file).map(File::getAbsolutePath).orElse(null)));
    }
    
    protected abstract Optional<Boolean> trySaveData(File file, T data);
    
    public Optional<File> getLocalFile(String sourceUrl) {
        return Optional.ofNullable(sourceUrl)
                .map(getUrlPattern()::matcher).filter(Matcher::matches)
                .flatMap(urlMatcher -> getLocalFile(
                        Optional.ofNullable(urlMatcher.group("path")).filter(e -> !e.isBlank()).orElseGet(this::getCategory),
                        Optional.ofNullable(urlMatcher.group("name")).filter(e -> !e.isBlank()).orElse(null)));
    }
    
    public Optional<File> getLocalFile(String sourcePath, String sourceName) {
        return Optional.of(Optional.ofNullable(sourceName)
                        .orElse(sourcePath + '.' + getContentType()))
                .flatMap(this::getLocalFileName)
                .flatMap(localName -> locateLocalFile(
                        Optional.ofNullable(sourcePath).map(e -> e.replaceAll("_icon", "-icon")).orElse(null),
                        localName));
    }
    
    public Optional<String> getLocalFileName(String sourceName) {
        return Optional.ofNullable(sourceName)
                .map(name -> name.replaceAll("\\.[^.]+$", ""))
                .map(nameKey -> Optional.of(nameKey)
                        .flatMap(this::formatLocalFileName)
                        .orElse(nameKey))
                .map(nameKey -> (nameKey + "." + getContentType()));
    }
    
    protected abstract Optional<String> formatLocalFileName(String nameKey);
    
    protected abstract Optional<File> locateLocalFile(String localPath, String localName);
    
    protected abstract boolean localFilePresent(File localFile);
    
    protected abstract boolean localFileNotPresent(File localFile);
    
    
    //Getters
    
    protected String getTypeName(boolean plural) {
        return "data";
    }
    
    protected final String getTypeName() {
        return getTypeName(false);
    }
    
    protected abstract String getCategory();
    
    protected abstract String getUrlRoot();
    
    protected abstract String getMirrorRoot();
    
    protected abstract Pattern getUrlPattern();
    
    protected abstract Pattern getUrlExtractorPattern();
    
    protected abstract String getContentType();
    
    protected abstract int getDelayRms();
    
    protected Logger getLogger() {
        return logger;
    }
    
    
    //Static Methods
    
    public static void delay(int delayMs) {
        if (!USE_DELAY) {
            return;
        }
        
        final int delay = (int) (Math.random() * (delayMs / 10)) + delayMs;
        logger.trace("Sleeping {}ms...", delay);
        
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }
    
}
