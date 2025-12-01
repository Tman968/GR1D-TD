/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.badlogic.drop;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Basic interface for an enemies, establishing damaging enemies and returning information on them.
 * @author natha
 */
public interface EnemyInterface {
    /**
     * Applies the passed damage to the enemy's health points.
     * If the enemy is reduced to 0 hit points or less, it is marked as dead.
     * @param damage 
     */
    abstract public void damage(float damage);
    
    
    
    /**
     * Returns the ID of the enemy.
     * Each enemy type is assigned an ID.
     * @return 
     */
    abstract public int getID();
    /**
     * Returns the maximum health points of the enemy.
     * @return 
     */
    abstract public float getMaxHP();
    /**
     * Returns a percentage representing the distance along the whole path.
     * @return 
     */
    abstract public float getProg();
    /**
     * Returns the enemy's current health points.
     * @return 
     */
    abstract public float getHP();
    /**
     * Returns true if the enemy is dead.
     * @return 
     */
    abstract public boolean getIsDead();
    
    /**
     * Returns the hitbox of the enemy.
     * @return 
     */
    abstract public Rectangle getHitbox();
    
    /**
     * Returns the sprite of the enemy.
     * @return 
     */
    abstract public Sprite getEnemySprite();
    
    /**
     * Returns true if the enemy is mid-animation.
     * @return 
     */
    abstract public boolean getIsInAnim();
    
    /**
     * Returns the time that an enemy has been touching a waypoint.
     * @return 
     */
    abstract public float getTouchDetect();
}
