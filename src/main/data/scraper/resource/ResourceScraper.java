/*
 * File:    ResourceScraper.java
 * Package: main.data.scraper.resource
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.scraper.resource;

import java.io.File;
import java.util.Optional;
import java.util.regex.Pattern;

import main.data.mirror.DataMirror;
import main.data.mirror.host.DataHost;
import main.data.scraper.base.Scraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourceScraper<T> extends Scraper<T> {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(ResourceScraper.class);
    
    
    //Constants
    
    public static final String URL_ROOT_RESOURCE = DataHost.URL_STATIC;
    
    public static final String MIRROR_ROOT_RESOURCE = DataMirror.DIR_STATIC.getName();
    
    public static final Pattern URL_PATTERN_RESOURCE = Pattern.compile("(?<url>(?<host>" + Pattern.quote(URL_ROOT_RESOURCE) + ")" + "(?<location>(?<path>(?:[\\w\\-]*(?:/|$))*)(?<name>[\\w\\-]+\\.\\w+|)))");
    
    public static final Pattern URL_EXTRACTOR_PATTERN_RESOURCE = Pattern.compile("(?<=\")" + URL_PATTERN_RESOURCE.pattern() + "(?=\")");
    
    public static final int DELAY_RMS_RESOURCE = 250;
    
    
    //Methods
    
    @Override
    protected Optional<String> formatLocalFileName(String nameKey) {
        return Optional.ofNullable(nameKey);
    }
    
    @Override
    protected final Optional<File> locateLocalFile(String localPath, String localName) {
        return DataMirror.getResourceFile(localPath, localName);
    }
    
    @Override
    protected boolean localFilePresent(File localFile) {
        return DataMirror.localFileExists(localFile);
    }
    
    @Override
    protected boolean localFileNotPresent(File localFile) {
        return DataMirror.localFileNotExists(localFile);
    }
    
    
    //Getters
    
    @Override
    protected String getTypeName(boolean plural) {
        return "resource" + (plural ? "s" : "");
    }
    
    @Override
    protected final String getCategory() {
        return null;
    }
    
    @Override
    protected String getUrlRoot() {
        return URL_ROOT_RESOURCE;
    }
    
    @Override
    protected String getMirrorRoot() {
        return MIRROR_ROOT_RESOURCE;
    }
    
    @Override
    protected Pattern getUrlPattern() {
        return URL_PATTERN_RESOURCE;
    }
    
    @Override
    protected Pattern getUrlExtractorPattern() {
        return URL_EXTRACTOR_PATTERN_RESOURCE;
    }
    
    @Override
    protected int getDelayRms() {
        return DELAY_RMS_RESOURCE;
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
}
