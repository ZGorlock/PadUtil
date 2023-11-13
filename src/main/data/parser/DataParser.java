/*
 * File:    DataParser.java
 * Package: main.data.parser
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser;

import java.util.function.Consumer;

import main.data.mirror.cache.EntityCache;
import main.data.parser.page.dungeon.DungeonPageParser;
import main.data.parser.page.dungeon.SubDungeonPageParser;
import main.data.parser.page.monster.MonsterPageParser;
import main.data.parser.page.search.SearchPageParser;

public final class DataParser {
    
    //Constants
    
    public static final boolean READ_CACHE = false;
    
    public static final boolean WRITE_CACHE = false;
    
    
    //Static Fields
    
    public static final SearchPageParser searchPageParser = new SearchPageParser();
    
    public static final MonsterPageParser monsterPageParser = new MonsterPageParser();
    
    public static final DungeonPageParser dungeonPageParser = new DungeonPageParser();
    
    public static final SubDungeonPageParser subDungeonPageParser = new SubDungeonPageParser();
    
    
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
        
        parser.accept(DataParser::parseDungeonPages);
        parser.accept(DataParser::parseSubDungeonPages);
    }
    
    public static void parseSearchPages() {
        System.out.println("Parsing Search Pages...\n\n");
        EntityCache.cacheSearchForms(searchPageParser.parseAll());
    }
    
    public static void parseMonsterPages() {
        System.out.println("Parsing Monster Pages...\n\n");
        EntityCache.cacheMonsters(monsterPageParser.parseAll());
        
        //File f = new File(DataMirror.DIR_BASE, "monster\\09804.html");
        //Monster x = monsterPageParser.parse(f);
        //EntityStore.storeMonster(x);
    }
    
    public static void parseDungeonPages() {
        System.out.println("Parsing Dungeon Pages...\n\n");
        EntityCache.cacheDungeons(dungeonPageParser.parseAll());
        
        //File f = new File(DataMirror.DIR_BASE, "dungeon\\04741.html");
        //Dungeon x = dungeonPageParser.parse(f);
        //EntityStore.storeDungeon(x);
    }
    
    public static void parseSubDungeonPages() {
        System.out.println("Parsing Sub-Dungeon Pages...\n\n");
        EntityCache.cacheSubDungeons(subDungeonPageParser.parseAll());
        
        //File f = new File(DataMirror.DIR_BASE, "sub-dungeon\\04741_001.html");
        //SubDungeon x = subDungeonPageParser.parse(f);
        //EntityStore.storeSubDungeon(x);
    }
    
}
