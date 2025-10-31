/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

/**
 * The abstract version of the standard enemy class, including default behavior.
 * @author natha
 */
public abstract class EnemyAbstract implements EnemyInterface,EnemyCracked {
    protected final int ID;
    protected final double MAX_HP;
    protected final double BASE_SPEED;
    protected double prog;
    protected double hp;
    protected boolean isDead;
    
    /**
     * Constructor for EnemyAbstract.
     * Defines the constants ID, MAX_HP, and BASE_SPEED with its inputs, and places the enemy at the start of the track.
     * @param id
     * @param maxHP
     * @param baseSpeed 
     */
    public EnemyAbstract(int id, double maxHP, double baseSpeed) {
        ID = id;
        MAX_HP = maxHP;
        BASE_SPEED = baseSpeed;
        prog = 0;
        hp = MAX_HP;
        isDead = false;
    }
    
    /**
     * Default act() behavior.
     * If an enemy uses this method for act(), it simply progress by it's base speed.
     */
    @Override
    public void act() {prog += BASE_SPEED;};
    
    /**
     * Default damage behavior.
     * If an enemy uses this method for damage(), the given damage will simply be subtracted from it's hit points.
     * Further, if the enemy is reduced to 0 health points or less, it will be marked as dead.
     * @param damage 
     */
    @Override
    public void damage(double damage) {
        hp -= damage;
        if (hp<=0) {
            isDead = true;
        }
    }
    
    /**
     * Returns the ID of the enemy.
     * Each enemy type is assigned an ID.
     * @return 
     */
    @Override
    public final int getID() {return ID;}
    /**
     * Returns the maximum health points of the enemy.
     * @return 
     */
    @Override
    public final double getMaxHP() {return MAX_HP;}
    /**
     * Returns a percentage representing the distance along the whole path.
     * @return 
     */
    @Override
    public final double getProg() {return prog;}
    /**
     * Returns the enemy's current health points.
     * @return 
     */
    @Override
    public final double getHP() {return hp;}
    /**
     * Returns true if the enemy is dead.
     * @return 
     */
    @Override
    public final boolean getIsDead() {return isDead;}
    
    
}
