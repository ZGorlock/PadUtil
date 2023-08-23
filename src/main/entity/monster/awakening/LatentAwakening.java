/*
 * File:    LatentAwakening.java
 * Package: main.entity.monster.awakening
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.awakening;

public class LatentAwakening extends Awakening {
    
    //Constructors
    
    public LatentAwakening() {
        super();
    }
    
    public LatentAwakening(Integer id, String name) {
        super(id, name);
    }
    
    public LatentAwakening(Integer id, String name, String iconUrl) {
        super(id, name, iconUrl);
    }
    
    public LatentAwakening(String name, String iconUrl, String searchUrl) {
        super(name, iconUrl, searchUrl);
    }
    
}
