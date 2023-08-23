/*
 * File:    Skill.java
 * Package: main.entity.monster.skill
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster.skill;

import java.util.List;

import main.entity.base.Entity;

public abstract class Skill extends Entity {
    
    //Fields
    
    public String description;
    
    public List<SkillTag> tags;
    
    
    //Constructors
    
    protected Skill() {
        super();
    }
    
}
