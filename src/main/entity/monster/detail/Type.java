/*
 * File:    Type.java
 * Package: main.entity.monster.detail
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.detail;

import java.util.List;

import main.data.entity.EntityStore;
import main.entity.base.resource.Image;
import main.entity.base.tag.Tag;

public class Type extends Tag {
    
    //Constants
    
    protected static final String TAG_KEYWORD = "type";
    
    public static List<String> TYPE_NAMES = List.of(
            "Evo Material",
            "Balanced",
            "Physical",
            "Healer",
            "Dragon",
            "God",
            "Attacker",
            "Devil",
            "Machine",
            "",
            "",
            "",
            "Awoken Material",
            "",
            "Enhance Material",
            "Redeemable Material"
    );
    
    
    //Fields
    
    public Image icon;
    
    
    //Constructors
    
    public Type() {
        super();
    }
    
    public Type(Integer id) {
        super(id, TYPE_NAMES.get(id));
    }
    
    public Type(Integer id, String iconUrl) {
        this(id);
        
        this.icon = new Image(iconUrl);
    }
    
    public Type(String iconUrl) {
        this(extractIdFromIcon(iconUrl), iconUrl);
    }
    
    public Type(String iconUrl, String searchUrl) {
        this(extractId(searchUrl), iconUrl);
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
    
    public static Type storeEntity(Type entity) {
        return EntityStore.storeType(entity);
    }
    
    public static Type lookupEntity(int id) {
        return EntityStore.lookupType(id);
    }
    
}
