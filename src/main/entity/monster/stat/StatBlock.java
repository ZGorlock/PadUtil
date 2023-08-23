/*
 * File:    StatBlock.java
 * Package: main.entity.monster.stat
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.stat;

import java.util.List;

import main.entity.base.Entity;

public class StatBlock extends Entity {
    
    //Fields
    
    public Integer level;
    
    public Integer hp;
    
    public Integer atk;
    
    public Integer rcv;
    
    
    //Constructors
    
    public StatBlock() {
        super();
    }
    
    public StatBlock(Integer level, Integer hp, Integer atk, Integer rcv) {
        this();
        
        this.level = level;
        this.hp = hp;
        this.atk = atk;
        this.rcv = rcv;
    }
    
    public StatBlock(List<Integer> stats) {
        this(stats.get(0), stats.get(1), stats.get(2), stats.get(3));
    }
    
    
    //Methods
    
    @Override
    public String toString() {
        return "Lv. " + level;
    }
    
}
