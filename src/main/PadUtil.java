/*
 * File:    PadUtil.java
 * Package: main
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main;

import java.io.File;

import commons.access.Project;
import main.data.entity.EntityStore;
import main.data.mirror.cache.DataCache;
import main.data.parser.DataParser;
import main.data.scraper.DataScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PadUtil {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(PadUtil.class);
    
    static {
        System.setProperty("logback.configurationFile", new File(Project.RESOURCES_DIR, "logback.xml").getAbsolutePath());
    }
    
    
    //Constants
    
    public static final boolean MIRROR = false;
    
    public static final boolean PARSE = true;
    
    
    //Main Method
    
    public static void main(String[] args) throws Exception {
        if (MIRROR) {
            DataScraper.scrape();
            DataCache.save();
        } else {
            DataCache.load();
        }
        
        if (PARSE) {
            DataParser.parse();
            EntityStore.save();
        } else {
            EntityStore.load();
        }
    }
    
}
