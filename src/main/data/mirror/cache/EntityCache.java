/*
 * File:    EntityCache.java
 * Package: main.data.cache
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.mirror.cache;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import main.data.entity.EntityStore;
import main.entity.base.Entity;
import main.entity.base.form.SearchForm;
import main.entity.dungeon.Dungeon;
import main.entity.dungeon.SubDungeon;
import main.entity.monster.Monster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityCache {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(EntityCache.class);
    
    
    //Static Fields
    
    public static final Map<File, SearchForm> searchForms = new LinkedHashMap<>();
    
    public static final Map<File, Monster> monsters = new LinkedHashMap<>();
    
    public static final Map<File, Dungeon> dungeons = new LinkedHashMap<>();
    
    public static final Map<File, SubDungeon> subDungeons = new LinkedHashMap<>();
    
    
    //Static Methods
    
    private static <T extends Entity> void cache(Map<File, T> cache, Map<File, T> data) {
        cache.clear();
        cache.putAll(data);
    }
    
    public static void cacheSearchForms(Map<File, SearchForm> data) {
        cache(searchForms, data);
        
        searchForms.values().stream()
                .filter(Objects::nonNull)
                .forEachOrdered(searchForm -> {
                    
                    Optional.of(searchForm).map(e -> e.typeList)
                            .stream().flatMap(Collection::stream)
                            .sorted(Comparator.comparingInt(o -> o.id))
                            .forEachOrdered(EntityStore::storeType);
                    
                    Optional.of(searchForm).map(e -> e.awakeningList)
                            .stream().flatMap(Collection::stream)
                            .sorted(Comparator.comparingInt(o -> o.id))
                            .forEachOrdered(EntityStore::storeAwakening);
                    
                    Optional.of(searchForm).map(e -> e.seriesList)
                            .stream().flatMap(Collection::stream)
                            .sorted(Comparator.comparingInt(o -> o.id))
                            .forEachOrdered(EntityStore::storeSeries);
                });
    }
    
    public static void cacheMonsters(Map<File, Monster> data) {
        cache(monsters, data);
        
        monsters.values().stream()
                .filter(Objects::nonNull)
                .forEachOrdered(monster -> {
                    
                    Optional.of(monster)
                            .ifPresent(EntityStore::storeMonster);
                });
    }
    
    public static void cacheDungeons(Map<File, Dungeon> data) {
        cache(dungeons, data);
        
        dungeons.values().stream()
                .filter(Objects::nonNull)
                .forEachOrdered(dungeon -> {
                    
                    Optional.of(dungeon)
                            .ifPresent(EntityStore::storeDungeon);
                });
    }
    
    public static void cacheSubDungeons(Map<File, SubDungeon> data) {
        cache(subDungeons, data);
        
        subDungeons.values().stream()
                .filter(Objects::nonNull)
                .forEachOrdered(subDungeon -> {
                    
                    Optional.of(subDungeon)
                            .ifPresent(EntityStore::storeSubDungeon);
                });
    }
    
}
