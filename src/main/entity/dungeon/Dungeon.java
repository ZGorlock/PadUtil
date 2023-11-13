/*
 * File:    Dungeon.java
 * Package: main.entity.dungeon
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.dungeon;

import java.util.List;

import main.data.entity.EntityStore;
import main.entity.base.Entity;
import main.entity.base.resource.Image;
import main.entity.base.resource.Page;

public class Dungeon extends Entity {
    
    //Fields
    
    public Page page;
    
    public Image icon;
    
    public List<Integer> subDungeons;
    
    
    //Constructors
    
    public Dungeon() {
        super();
    }
    
    
    //Methods
    
    @Override
    public void store() {
        storeEntity(this);
    }
    
    
    //Static Methods
    
    public static Dungeon storeEntity(Dungeon entity) {
        return EntityStore.storeDungeon(entity);
    }
    
    public static Dungeon lookupEntity(int id) {
        return EntityStore.lookupDungeon(id);
    }
    
}
