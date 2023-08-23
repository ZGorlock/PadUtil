/*
 * File:    SerializableEntity.java
 * Package: main.entity.base.parser
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.parser;

public interface SerializableEntity {
    
    //Methods
    
    String serialize();
    
    void initialize();
    
}
