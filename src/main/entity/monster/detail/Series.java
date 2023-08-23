/*
 * File:    Series.java
 * Package: main.entity.monster
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.detail;

import main.data.entity.EntityStore;
import main.entity.base.tag.Tag;

public class Series extends Tag {
    
    //Constants
    
    protected static final String TAG_KEYWORD = "series";
    
    
    //Constructors
    
    public Series() {
        super();
    }
    
    public Series(Integer id, String name) {
        super(id, name);
    }
    
    public Series(String name, String searchUrl) {
        this(extractId(searchUrl), name);
    }
    
    
    //Methods
    
    @Override
    public void store() {
        storeEntity(this);
    }
    
    
    //Getters
    
    @Override
    protected String getTagKeyword() {
        return TAG_KEYWORD;
    }
    
    
    //Static Methods
    
    public static Integer extractId(String searchUrl) {
        return Tag.extractId(searchUrl, TAG_KEYWORD);
    }
    
    public static Series storeEntity(Series entity) {
        return EntityStore.storeSeries(entity);
    }
    
    public static Series lookupEntity(int id) {
        return EntityStore.lookupSeries(id);
    }
    
}
