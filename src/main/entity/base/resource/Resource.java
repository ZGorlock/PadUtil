/*
 * File:    Resource.java
 * Package: main.entity.base.resource
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.resource;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

import commons.access.Filesystem;
import commons.lambda.function.unchecked.UncheckedFunction;
import commons.object.string.StringUtility;
import main.data.mirror.DataMirror;
import main.data.mirror.cache.DataCache;
import main.entity.base.Entity;

public abstract class Resource extends Entity {
    
    //Fields
    
    public transient File file;
    
    public String path;
    
    public transient boolean cached;
    
    public Date cacheDate;
    
    public String source;
    
    public transient URL url;
    
    
    //Constructors
    
    protected Resource() {
        super();
    }
    
    protected Resource(String sourceUrl) {
        this();
        
        this.source = Optional.ofNullable(sourceUrl)
                .map(String::strip)
                .orElse(null);
        this.path = Optional.ofNullable(this.source)
                .map(this::fileLocator)
                .map(File::getPath).map(StringUtility::fixFileSeparators)
                .orElseGet(() -> Optional.ofNullable(this.source)
                        .flatMap(DataMirror::getLocalFile)
                        .map(File::getPath).map(StringUtility::fixFileSeparators)
                        .orElse(null));
        
        initialize();
    }
    
    protected Resource(File file) {
        this();
        
        this.path = Optional.ofNullable(file)
                .map(File::getPath).map(StringUtility::fixFileSeparators)
                .orElse(null);
        this.source = Optional.ofNullable(this.path)
                .map(this::urlLocator)
                .orElseGet(() -> Optional.ofNullable(this.path)
                        .flatMap(DataMirror::getSourceUrl)
                        .orElse(null));
        
        initialize();
    }
    
    
    //Methods
    
    public void initialize() {
        this.file = Optional.ofNullable(this.path).map(File::new).orElse(null);
        this.name = Optional.ofNullable(this.file).map(File::getName).orElse(null);
        this.cached = Optional.ofNullable(this.file).map(DataMirror::localFileExists).orElse(false);
        this.cacheDate = Optional.ofNullable(this.file).filter(DataMirror::localFileExists).map(Filesystem::getCreationTime).orElse(null);
        this.url = Optional.ofNullable(this.source).map(URI::create).map((UncheckedFunction<URI, URL>) URI::toURL).orElse(null);
    }
    
    protected File fileLocator(String url) {
        return DataCache.lookup(url);
    }
    
    protected String urlLocator(String filePath) {
        return DataCache.reverseLookup(filePath);
    }
    
}
