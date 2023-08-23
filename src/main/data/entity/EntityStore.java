/*
 * File:    EntityStore.java
 * Package: main.data.entity
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.entity;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import commons.access.Filesystem;
import commons.access.Project;
import main.data.parser.base.Parser;
import main.entity.base.Entity;
import main.entity.base.tag.Tag;
import main.entity.monster.Monster;
import main.entity.monster.awakening.Awakening;
import main.entity.monster.detail.Series;
import main.entity.monster.detail.Type;

public class EntityStore {
    
    //Constants
    
    public static final File ENTITY_DIR = new File(Project.DATA_DIR, "entity");
    
    public static final File AWAKENING_DIR = new File(ENTITY_DIR, "awakening");
    
    public static final File TYPE_DIR = new File(ENTITY_DIR, "type");
    
    public static final File SERIES_DIR = new File(ENTITY_DIR, "series");
    
    public static final File TAG_DIR = new File(ENTITY_DIR, "tag");
    
    public static final File MONSTER_DIR = new File(ENTITY_DIR, "monster");
    
    
    //Static Fields
    
    private static final Map<Integer, Awakening> awakenings = new LinkedHashMap<>();
    
    private static final Map<Integer, Type> types = new LinkedHashMap<>();
    
    private static final Map<Integer, Series> series = new LinkedHashMap<>();
    
    private static final Map<Integer, Tag> tags = new LinkedHashMap<>();
    
    private static final Map<Integer, Monster> monsters = new LinkedHashMap<>();
    
    
    //Static Methods
    
    private static <T extends Entity> T lookup(Map<Integer, T> store, Integer id) {
        return Optional.ofNullable(id)
                .flatMap(i -> Optional.ofNullable(store)
                        .map(s -> s.get(i)))
                .orElse(null);
    }
    
    public static Awakening lookupAwakening(int id) {
        return lookup(awakenings, id);
    }
    
    public static Type lookupType(int id) {
        return lookup(types, id);
    }
    
    public static Series lookupSeries(int id) {
        return lookup(series, id);
    }
    
    public static Tag lookupTag(int id) {
        return lookup(tags, id);
    }
    
    public static Monster lookupMonster(int id) {
        return lookup(monsters, id);
    }
    
    private static <T extends Entity> T store(Map<Integer, T> store, T entity) {
        return Optional.ofNullable(entity)
                .flatMap(e -> Optional.ofNullable(store)
                        .map(s -> s.computeIfAbsent(e.id, id -> e)))
                .orElse(null);
    }
    
    public static Awakening storeAwakening(Awakening entity) {
        return store(awakenings, entity);
    }
    
    public static Type storeType(Type entity) {
        return store(types, entity);
    }
    
    public static Series storeSeries(Series entity) {
        return store(series, entity);
    }
    
    public static Tag storeTag(Tag entity) {
        return store(tags, entity);
    }
    
    public static Monster storeMonster(Monster entity) {
        return store(monsters, entity);
    }
    
    public static void load() {
        loadAwakeningEntityStore();
        loadTypeEntityStore();
        loadSeriesEntityStore();
        loadTagEntityStore();
        loadMonsterEntityStore();
    }
    
    public static Map<Integer, Awakening> loadAwakeningEntityStore() {
        System.out.println("Loading Awakening Entity Store...\n\n");
        return loadEntityStore(awakenings, AWAKENING_DIR, Awakening.class);
    }
    
    public static Map<Integer, Type> loadTypeEntityStore() {
        System.out.println("Loading Type Entity Store...\n\n");
        return loadEntityStore(types, TYPE_DIR, Type.class);
    }
    
    public static Map<Integer, Series> loadSeriesEntityStore() {
        System.out.println("Loading Series Entity Store...\n\n");
        return loadEntityStore(series, SERIES_DIR, Series.class);
    }
    
    public static Map<Integer, Tag> loadTagEntityStore() {
        System.out.println("Loading Tag Entity Store...\n\n");
        return loadEntityStore(tags, TAG_DIR, Tag.class);
    }
    
    public static Map<Integer, Monster> loadMonsterEntityStore() {
        System.out.println("Loading Monster Entity Store...\n\n");
        return loadEntityStore(monsters, MONSTER_DIR, Monster.class);
    }
    
    private static <T extends Entity> Map<Integer, T> loadEntityStore(Map<Integer, T> entityMap, File entityDir, Class<T> type) {
        Optional.of(entityDir).map(Filesystem::getFiles)
                .stream().flatMap(Collection::stream)
                .map(file -> Optional.of(file).map(File::getName)
                        .map(fileName -> fileName.replaceAll("\\..+$", ""))
                        .filter(fileId -> fileId.matches("\\d+")).map(Integer::parseInt)
                        .map(id -> Map.entry(id, file)))
                .filter(Optional::isPresent).map(Optional::get)
                .forEachOrdered(fileEntry -> entityMap.put(fileEntry.getKey(),
                        Parser.gson.fromJson(Filesystem.readFileToString(fileEntry.getValue()), type)
                ));
        return entityMap;
    }
    
    public static void save() {
        saveAwakeningEntityStore();
        saveTypeEntityStore();
        saveSeriesEntityStore();
        saveTagEntityStore();
        saveMonsterEntityStore();
    }
    
    public static boolean saveAwakeningEntityStore() {
        System.out.println("Saving Awakening Entity Store...\n\n");
        return saveEntityStore(awakenings, AWAKENING_DIR);
    }
    
    public static boolean saveTypeEntityStore() {
        System.out.println("Saving Type Entity Store...\n\n");
        return saveEntityStore(types, TYPE_DIR);
    }
    
    public static boolean saveSeriesEntityStore() {
        System.out.println("Saving Series Entity Store...\n\n");
        return saveEntityStore(series, SERIES_DIR);
    }
    
    public static boolean saveTagEntityStore() {
        System.out.println("Saving Tag Entity Store...\n\n");
        return saveEntityStore(tags, TAG_DIR);
    }
    
    public static boolean saveMonsterEntityStore() {
        System.out.println("Saving Monster Entity Store...\n\n");
        return saveEntityStore(monsters, MONSTER_DIR);
    }
    
    private static <T extends Entity> boolean saveEntityStore(Map<Integer, T> entityMap, File entityDir) {
        return Optional.ofNullable(entityMap)
                .map(Map::entrySet).filter(e -> !e.isEmpty())
                .map(entities -> entities.stream()
                        .map(entity -> Filesystem.writeStringToFile(
                                new File(entityDir, (entity.getKey() + ".json")),
                                entity.getValue().serialize()))
                        .reduce(Boolean.TRUE, Boolean::logicalAnd))
                .orElse(false);
    }
    
}
