/*
 * File:    DataParser.java
 * Package: main.data.parser
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser;

import java.io.File;
import java.util.function.Consumer;

import main.data.parser.page.monster.MonsterPageParser;
import main.entity.monster.Monster;

public final class DataParser {
    
    //Constants
    
    public static final boolean READ_CACHE = false;
    
    public static final boolean WRITE_CACHE = true;
    
    
    //Static Fields
    
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
        
        parser.accept(DataParser::parseMonsterPages);
    }
    
    public static void parseMonsterPages() {
        System.out.println("Parsing Monster Pages...\n\n");
//        monsterPageParser.parseAll();

//        File f = new File("data\\.mirror\\pad.chesterip.cc\\monster\\02588.html");
//        File f = new File("data\\.mirror\\pad.chesterip.cc\\monster\\09359.html");
//        File f = new File("data\\.mirror\\pad.chesterip.cc\\monster\\09804.html");
        File f = new File("data\\.mirror\\pad.chesterip.cc\\monster\\10333.html");
        Monster m = monsterPageParser.parseMonster(f);
        
        int g = 5;
    }
    
}
