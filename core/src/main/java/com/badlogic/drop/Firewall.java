/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

/**
 * The firewall class that represents the player's health
 * When enemies reach the end of the GRID map, they damage the firewall.
 * @author paulcaplin
 */
public class Firewall {
    
    
    private float maxHealth;
    private float currentHealth;
    private boolean isDestroyed;
    
    /**
     * Constructor for Firewall
     * @param startingHealth The initial maximum health of the firewall
     */
    public Firewall(float startingHealth) {
        this.maxHealth = startingHealth;
        this.currentHealth = startingHealth;
        this.isDestroyed = false;
    }
    
    /**
     * Default constructor with standard health value
     */
    public Firewall() {
        this(100); // Defaults to 100 HP
    }
    
    /**
     * Applies damage to the firewall
     * @param damage Amount of damage to apply
     */
    public void takeDamage(float damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDestroyed = true;
           
        }
    }
    
    /**
     * Repairs the firewall by a specified amount
     * @param amount Amount to heal
     */
    public void repair(float amount) {
        currentHealth += amount;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }
    
    /**
     * Returns current health
     * @return Current health value
     */
    public float getCurrentHealth() {
        return currentHealth;
    }
    
    /**
     * Returns maximum health
     * @return Maximum health value
     */
    public float getMaxHealth() {
        return maxHealth;
    }
    
    /**
     * Returns health as a percentage
     * @return Health percentage (0.0 to 1.0)
     */
    public float getHealthPercentage() {
        return currentHealth / maxHealth;
    }
    
    /**
     * Checks if the firewall has been destroyed
     * @return true if firewall health is 0 or below
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
    
    /**
     * Resets the firewall to full health
     */
    public void reset() {
        currentHealth = maxHealth;
        isDestroyed = false;
    }
    
    
    
    
}
