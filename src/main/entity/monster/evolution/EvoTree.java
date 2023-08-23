/*
 * File:    EvoTree.java
 * Package: main.entity.monster.evolution
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import main.entity.base.Entity;

public class EvoTree extends Entity {
    
    //Fields
    
    public EvoNode base;
    
    public List<EvoTree> evolutions;
    
    
    //Constructors
    
    public EvoTree() {
        super();
        
        this.evolutions = new ArrayList<>();
    }
    
    public EvoTree(Evolution evo) {
        this();
        
        this.base = new EvoNode(evo);
    }
    
    public EvoTree(List<Evolution> evolutions) {
        this();
        
        final Map<Integer, EvoTree> evoMap = new HashMap<>();
        for (Evolution evo : evolutions) {
            EvoTree node;
            if (evo.base == null) {
                node = null;
                this.base = new EvoNode(evo);
            } else {
                node = new EvoTree(evo);
                Optional.ofNullable(evoMap.get(evo.base)).orElse(this)
                        .evolutions.add(node);
            }
            evoMap.put(evo.id, node);
        }
    }
    
    
    //Methods
    
    @Override
    public String toString() {
        return String.valueOf(base);
    }
    
}
