/*
 * File:    Image.java
 * Package: main.entity.base.resource
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.resource;

import java.io.File;

import main.data.mirror.cache.DataCache;

public class Image extends Resource {
    
    //Constructors
    
    public Image() {
        super();
    }
    
    public Image(String sourceUrl) {
        super(sourceUrl);
    }
    
    public Image(File file) {
        super(file);
    }
    
    
    //Methods
    
    @Override
    protected File fileLocator(String url) {
        return DataCache.lookupImageResource(url);
    }
    
    @Override
    protected String urlLocator(String filePath) {
        return DataCache.lookupImageUrlKey(filePath);
    }
    
}
