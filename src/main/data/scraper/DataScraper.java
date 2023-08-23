/*
 * File:    DataScraper.java
 * Package: main.data.scraper
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.scraper;

import java.util.function.Consumer;

import main.data.mirror.cache.DataCache;
import main.data.scraper.page.category.MonsterPageScraper;
import main.data.scraper.resource.category.ImageResourceScraper;

public final class DataScraper {
    
    //Static Fields
    
    public static final MonsterPageScraper monsterPageScraper = new MonsterPageScraper();
    
    public static final ImageResourceScraper imageResourceScraper = new ImageResourceScraper();
    
    
    //Main Methods
    
    public static void main(String[] args) {
        scrape();
    }
    
    
    //Static Methods
    
    public static void scrape() {
        final Runnable printSeparator = () ->
                System.out.println("\n\n" + ("=".repeat(120) + "\n").repeat(2) + "\n");
        
        final Consumer<Runnable> scraper = (Runnable categoryScraper) -> {
            printSeparator.run();
            categoryScraper.run();
        };
        
        scraper.accept(DataScraper::scrapeMonsterPages);
        
        scraper.accept(DataScraper::scrapeImageResources);
    }
    
    public static void scrapeMonsterPages() {
        System.out.println("Scraping Monster Pages...\n\n");
        DataCache.cacheData(DataCache.monsterPages, monsterPageScraper.fetchAll());
    }
    
    public static void scrapeImageResources() {
        System.out.println("Scraping Image Resources...\n\n");
        DataCache.cacheData(DataCache.imageResources, imageResourceScraper.fetchAll());
    }
    
}
