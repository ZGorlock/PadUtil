/*
 * File:    SubDungeon.java
 * Package: main.entity.dungeon
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.dungeon;

import main.data.entity.EntityStore;
import main.entity.base.Entity;
import main.entity.base.resource.Image;
import main.entity.base.resource.Page;

public class SubDungeon extends Entity {
    
    //Fields
    
    public Page page;
    
    public Image icon;
    
    public Integer stamina;
    
    public Integer floorCount;
    
    public Long exp;
    
    public Long coins;
    
    
    //Constructors
    
    public SubDungeon() {
        super();
    }
    
    
    //Methods
    
    @Override
    public void store() {
        storeEntity(this);
    }
    
    
    //Static Methods
    
    public static SubDungeon storeEntity(SubDungeon entity) {
        return EntityStore.storeSubDungeon(entity);
    }
    
    public static SubDungeon lookupEntity(int id) {
        return EntityStore.lookupSubDungeon(id);
    }
    
}
