/*
 * File:    DataScraper.java
 * Package: main.data.scraper
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.scraper;

import java.util.function.Consumer;

import main.data.mirror.cache.DataCache;
import main.data.scraper.page.category.DungeonPageScraper;
import main.data.scraper.page.category.MonsterPageScraper;
import main.data.scraper.page.category.SubDungeonPageScraper;
import main.data.scraper.page.main.SearchPageScraper;
import main.data.scraper.resource.category.ImageResourceScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataScraper {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(DataScraper.class);
    
    
    //Static Fields
    
    public static final SearchPageScraper searchPageScraper = new SearchPageScraper();
    
    public static final MonsterPageScraper monsterPageScraper = new MonsterPageScraper();
    
    public static final DungeonPageScraper dungeonPageScraper = new DungeonPageScraper();
    
    public static final SubDungeonPageScraper subDungeonPageScraper = new SubDungeonPageScraper();
    
    public static final ImageResourceScraper imageResourceScraper = new ImageResourceScraper();
    
    
    //Main Methods
    
    public static void main(String[] args) {
        scrape();
    }
    
    
    //Static Methods
    
    public static void scrape() {
        final Runnable printSeparator = () ->
                logger.info("\n\n{}\n", ("=".repeat(120) + "\n").repeat(2));
        
        final Consumer<Runnable> scraper = (Runnable categoryScraper) -> {
            printSeparator.run();
            categoryScraper.run();
        };
        
        scraper.accept(DataScraper::scrapeSearchPages);
        
        scraper.accept(DataScraper::scrapeMonsterPages);
        scraper.accept(DataScraper::scrapeSubDungeonPages);
        
        scraper.accept(DataScraper::scrapeDungeonPages);
        scraper.accept(DataScraper::scrapeSubDungeonPages);
        
        scraper.accept(DataScraper::scrapeImageResources);
    }
    
    public static void scrapeSearchPages() {
        logger.info("Scraping Search Pages...\n\n");
        DataCache.cacheData(DataCache.searchPages, searchPageScraper.fetchAll());
    }
    
    public static void scrapeMonsterPages() {
        logger.info("Scraping Monster Pages...\n\n");
        DataCache.cacheData(DataCache.monsterPages, monsterPageScraper.fetchAll());
    }
    
    public static void scrapeDungeonPages() {
        logger.info("Scraping Dungeon Pages...\n\n");
        DataCache.cacheData(DataCache.dungeonPages, dungeonPageScraper.fetchAll());
    }
    
    public static void scrapeSubDungeonPages() {
        logger.info("Scraping Sub-Dungeon Pages...\n\n");
        DataCache.cacheData(DataCache.subDungeonPages, subDungeonPageScraper.fetchAll());
    }
    
    public static void scrapeImageResources() {
        logger.info("Scraping Image Resources...\n\n");
        DataCache.cacheData(DataCache.imageResources, imageResourceScraper.fetchAll());
    }
    
}
