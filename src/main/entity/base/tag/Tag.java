/*
 * File:    Tag.java
 * Package: main.entity.base.tag
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.tag;

import main.data.entity.EntityStore;
import main.data.mirror.host.DataHost;
import main.entity.base.Entity;

public class Tag extends Entity {
    
    //Constants
    
    protected static final String TAG_KEYWORD = "keywords";
    
    
    //Constructors
    
    public Tag() {
        super();
    }
    
    public Tag(Integer id, String name) {
        super(id, name);
    }
    
    public Tag(String name, String searchUrl) {
        this(extractId(searchUrl), name);
    }
    
    
    //Methods
    
    @Override
    public void store() {
        storeEntity(this);
    }
    
    
    //Getters
    
    protected String getTagKeyword() {
        return TAG_KEYWORD;
    }
    
    public String getSearchKey() {
        return String.valueOf(getId());
    }
    
    public String getSearchQuery() {
        return "?" + getTagKeyword() + "=" + getSearchKey();
    }
    
    public String getSearchLink() {
        return DataHost.URL_BASE + getSearchQuery();
    }
    
    
    //Static Methods
    
    public static Integer extractId(String searchUrl, String tagKeyword) {
        return Integer.parseInt(searchUrl.replaceAll("^.*/\\?" + tagKeyword + "=(\\d+)$", "$1"));
    }
    
    public static Integer extractId(String searchUrl) {
        return extractId(searchUrl, ".+?");
    }
    
    public static Integer extractIdFromIcon(String iconUrl, String iconKeyword) {
        return Integer.parseInt(iconUrl.replaceAll("^.*/" + iconKeyword + "(\\d+)\\.[^\\d.]+$", "$1"));
    }
    
    public static Integer extractIdFromIcon(String iconUrl) {
        return extractIdFromIcon(iconUrl, ".*?");
    }
    
    public static Tag storeEntity(Tag entity) {
        return EntityStore.storeTag(entity);
    }
    
    public static Tag lookupEntity(int id) {
        return EntityStore.lookupTag(id);
    }
    
}
