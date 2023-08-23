/*
 * File:    Monster.java
 * Package: main.entity.monster
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.monster;

import java.util.List;

import main.data.entity.EntityStore;
import main.entity.base.Entity;
import main.entity.base.resource.Image;
import main.entity.base.resource.Page;
import main.entity.monster.evolution.EvoTree;
import main.entity.monster.skill.ActiveSkill;
import main.entity.monster.skill.LeaderSkill;
import main.entity.monster.stat.StatBlock;

public class Monster extends Entity {
    
    //Fields
    
    public Page page;
    
    public Image image;
    
    public Image icon;
    
    public Integer stars;
    
    public List<Integer> type;
    
    public List<Integer> series;
    
    public List<Integer> awakenings;
    
    public List<Integer> superAwakenings;
    
    public List<Integer> applicableKillerLatents;
    
    public Boolean extraLatents;
    
    public Integer monsterPoints;
    
    public Long maxExp;
    
    public Integer cost;
    
    public ActiveSkill activeSkill;
    
    public LeaderSkill leaderSkill;
    
    public List<StatBlock> statBlocks;
    
    public List<Integer> similarActiveSkillMonsters;
    
    public List<Integer> sameActiveSkillMonsters;
    
    public List<Integer> sameSeriesMonsters;
    
    public EvoTree evoTree;
    
    public List<Integer> dropLocations;
    
    
    //Constructors
    
    public Monster() {
        super();
    }
    
    
    //Methods
    
    @Override
    public void store() {
        storeEntity(this);
    }
    
    
    //Static Methods
    
    public static Monster storeEntity(Monster entity) {
        return EntityStore.storeMonster(entity);
    }
    
    public static Monster lookupEntity(int id) {
        return EntityStore.lookupMonster(id);
    }
    
}
