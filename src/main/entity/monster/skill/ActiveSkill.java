/*
 * File:    ActiveSkill.java
 * Package: main.entity.monster.skill
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.skill;

public class ActiveSkill extends Skill {
    
    //Fields
    
    public Integer baseCountdown;
    
    public Integer minCountdown;
    
    public Integer maxLevel;
    
    public ActiveSkill evolvedSkill;
    
    public Integer transformTo;
    
    
    //Constructors
    
    public ActiveSkill() {
        super();
    }
    
}
