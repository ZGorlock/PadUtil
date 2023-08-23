/*
 * File:    PadUtil.java
 * Package: main
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main;

import main.data.mirror.cache.DataCache;
import main.data.parser.DataParser;
import main.data.scraper.DataScraper;

public class PadUtil {
    
    //Constants
    
    public static final boolean MIRROR = true;
    
    public static final boolean PARSE = true;
    
    
    //Main Method
    
    public static void main(String[] args) {
        if (MIRROR) {
            DataScraper.scrape();
            DataCache.save();
        } else {
            DataCache.load();
        }
        
        if (PARSE) {
            DataParser.parse();
        }
        
        int g = 5;
    }
    
}
