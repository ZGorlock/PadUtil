/*
 * File:    SuperAwakening.java
 * Package: main.entity.monster.awakening
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.awakening;

public class SuperAwakening extends Awakening {
    
    //Constructors
    
    public SuperAwakening() {
        super();
    }
    
    public SuperAwakening(Integer id, String name) {
        super(id, name);
    }
    
    public SuperAwakening(Integer id, String name, String iconUrl) {
        super(id, name, iconUrl);
    }
    
    public SuperAwakening(String name, String iconUrl, String searchUrl) {
        super(name, iconUrl, searchUrl);
    }
    
}
