/*
 * File:    Entity.java
 * Package: main.entity.base
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.data.parser.base.Parser;
import main.entity.base.parser.SerializableEntity;

public abstract class Entity implements SerializableEntity {
    
    //Fields
    
    public Integer id;
    
    public String name;
    
    
    //Constructors
    
    public Entity() {
        super();
    }
    
    public Entity(Integer id, String name) {
        super();
        
        this.id = id;
        this.name = name;
    }
    
    
    //Methods
    
    @Override
    public String serialize() {
        return Parser.toJson(this);
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public String toString() {
        return Stream.of(getId(), getName())
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(" - "));
    }
    
    public void store() {
    }
    
    
    //Getters
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
}
