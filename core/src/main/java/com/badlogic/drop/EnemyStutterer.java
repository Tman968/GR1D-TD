/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

/**
 *
 * @author natha
 */
public class EnemyStutterer extends EnemyAbstract {
    private int stutterCooldown; 
    /**
     * Constructs using the EnemyAbstract constructor with passed variables for ID, max hp, and speed respectively.
     * Also sets stutter cooldown.
     */
    public EnemyStutterer() {
        super(2,10.6f,1.03321f);
        stutterCooldown = 2;
    }
    
    /**
     * Unique act method for EnemyStutterer.
     * EnemyStutterer will not move in one out of every three act() calls.
     */
    @Override
    public void act() {
        if (stutterCooldown <= 0) {
            stutterCooldown = 2;
        } else {
            updateMovement();
            stutterCooldown--;
        }
    }
}
