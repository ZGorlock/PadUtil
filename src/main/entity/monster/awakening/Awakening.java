/*
 * File:    Awakening.java
 * Package: main.entity.monster.awakening
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.awakening;

import main.entity.base.resource.Image;
import main.entity.base.tag.Tag;

public class Awakening extends Tag {
    
    //Constants
    
    protected static final String TAG_KEYWORD = "awskills";
    
    
    //Fields
    
    public Image icon;
    
    
    //Constructors
    
    public Awakening() {
        super();
    }
    
    public Awakening(Integer id, String name) {
        super(id, name);
    }
    
    public Awakening(Integer id, String name, String iconUrl) {
        this(id, name);
        
        this.icon = new Image(iconUrl);
    }
    
    public Awakening(String name, String iconUrl) {
        this(extractIdFromIcon(iconUrl), name, iconUrl);
    }
    
    public Awakening(String name, String iconUrl, String searchUrl) {
        this(extractId(searchUrl), name, iconUrl);
    }
    
    
    //Getters
    
    @Override
    protected String getTagKeyword() {
        return TAG_KEYWORD;
    }
    
    @Override
    public String getSearchKey() {
        return "{\"operator\"%3A\"OR\"%2C\"operands\"%3A[{\"" + super.getSearchKey() + "\"%3A1}]}";
    }
    
    
    //Static Methods
    
    public static Integer extractId(String searchUrl) {
        return Integer.parseInt(searchUrl.replaceAll("^.*/\\?" + TAG_KEYWORD + "=.+\"(\\d+?)\".*$", "$1"));
    }
    
}
