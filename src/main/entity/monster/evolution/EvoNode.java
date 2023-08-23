/*
 * File:    EvoNode.java
 * Package: main.entity.monster.evolution
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.evolution;

import java.util.List;

import main.entity.base.Entity;

public class EvoNode extends Entity {
    
    //Fields
    
    public List<Integer> materials;
    
    
    //Constructors
    
    public EvoNode() {
        super();
    }
    
    public EvoNode(Evolution evo) {
        this();
        
        this.id = evo.id;
        this.materials = evo.materials;
    }
    
    
    //Methods
    
    @Override
    public String toString() {
        return String.valueOf(id);
    }
    
}
