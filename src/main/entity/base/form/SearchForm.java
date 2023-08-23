/*
 * File:    SearchForm.java
 * Package: main.entity.base.form
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.form;

import java.util.List;

import main.entity.base.tag.Tag;
import main.entity.monster.awakening.Awakening;
import main.entity.monster.detail.Series;
import main.entity.monster.detail.Type;

public class SearchForm extends Form {
    
    //Fields
    
    public List<Type> typeList;
    
    public List<Awakening> awakeningList;
    
    public List<Series> seriesList;
    
    public List<Tag> enemyTagList;
    
    public List<Tag> teamTagList;
    
    public List<Tag> boardTagList;
    
    public List<Tag> orbsChangeTagList;
    
    public List<Tag> orbsSpawnTagList;
    
    public List<Tag> leaderSkillTagList;
    
    
    //Constructors
    
    public SearchForm() {
        super();
    }
    
}
