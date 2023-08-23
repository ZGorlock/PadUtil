/*
 * File:    DataParser.java
 * Package: main.data.parser
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

import main.data.mirror.cache.EntityCache;
import main.data.parser.page.monster.MonsterPageParser;
import main.data.parser.page.search.SearchPageParser;
import main.entity.monster.Monster;

public final class DataParser {
    
    //Constants
    
    public static final boolean READ_CACHE = false;
    
    public static final boolean WRITE_CACHE = true;
    
    
    //Static Fields
    
    public static final SearchPageParser searchPageParser = new SearchPageParser();
    
    public static final MonsterPageParser monsterPageParser = new MonsterPageParser();
    
    
    //Main Methods
    
    public static void main(String[] args) throws Exception {
        parse();
    }
    
    
    //Static Methods
    
    public static void parse() {
        final Runnable printSeparator = () ->
                System.out.println("\n\n" + ("=".repeat(120) + "\n").repeat(2) + "\n");
        
        final Consumer<Runnable> parser = (Runnable categoryParser) -> {
            printSeparator.run();
            categoryParser.run();
        };
        
        parser.accept(DataParser::parseSearchPages);
        
        parser.accept(DataParser::parseMonsterPages);
    }
    
    public static void parseSearchPages() {
        System.out.println("Parsing Search Pages...\n\n");
        EntityCache.cacheSearchForms(searchPageParser.parseAll());
    }
    
    public static void parseMonsterPages() {
        System.out.println("Parsing Monster Pages...\n\n");
        EntityCache.cacheMonsters(monsterPageParser.parseAll());
        
        //File f = new File(DataMirror.DIR_BASE, "monster\\09804.html");
        //Monster x = monsterPageParser.parseMonster(f);
    }
    
}
