/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Tower superclass that creates a layout used for all tower units.
 * @author jacob
 */
public class Enemy {
    
    float health;
    float speed; //Maybe int? Could call it velocity and make it a vector
    Vector2 velocity;
    float maxVelocity;
    
    Vector2 location;
    Vector2 currentTile;
    
    float percentage; // percentage along track
    
    // viewport variables
    FitViewport viewport;
    float worldWidth;
    float worldHeight;
    
    //Default Textures and Sprites
    Texture enemyTexture;
    Sprite enemySprite;
    
    //Enemy Hitbox
    Rectangle enemyRectangle;
    
    /**
         * Constructor for the tower superclass, setting default values.
         * @author tdewe
         */
    public Enemy() {
        //Setting viewport variables
        viewport = new FitViewport(8,5);
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();
        
        // Setting up vectors
        location = new Vector2();
        velocity = new Vector2();
        velocity.x = 0f;
        velocity.y = 0f;
        location.x = 0f;
        location.y = (worldHeight/2);
        
        //Setting textures and sprites
        enemyTexture = new Texture("packet.png");
        enemySprite = new Sprite(enemyTexture);
        enemySprite.setSize(1, 1);
        enemySprite.setX(location.x);
        enemySprite.setY(location.y);
        enemyRectangle = new Rectangle();
        
        //Setting default values for variables
        health = 0;
        speed = 0;
        maxVelocity = 0;
        
    }
    
    /**
     * changes velocity based on the location of two waypoints
     * @param waypointStart as starting waypoint
     * @param waypointEnd as ending waypoint
     * @param inputEnemy the Enemy that velocity is being changed
     */
    public void ChangeVelocity(Enemy inputEnemy, Rectangle waypointStart, Rectangle waypointEnd) {
        float speedVel = 0f;
        //Setting up vectors for changing the enemy velocity later
        Vector2 startLocation = new Vector2();
        Vector2 endLocation = new Vector2();
        startLocation.x = (waypointStart.getX() - waypointStart.getWidth()/2);
        startLocation.y = (waypointStart.getY() - waypointStart.getHeight()/2);
        endLocation.x = (waypointEnd.getX() - waypointEnd.getWidth()/2);
        endLocation.y = (waypointEnd.getY() - waypointEnd.getHeight()/2);
        
        // Setting speed to velocity
        if (velocity.x != 0) {
            speedVel = velocity.x;
        }
        else {
            speedVel = velocity.y;
        }
        
        if (startLocation.x == endLocation.x) {
            // changing velocity to move along the y-axis
            // Checking if endpoint is above/below the startpoint
            velocity.x = 0f;
            if (startLocation.y < endLocation.y) {
                velocity.y = speedVel;
            }
            else {
                velocity.y = -speedVel;
            }
        }
        else {
            // changing velocity to move along the x-axis
            if (speedVel < 0) {
                speedVel = -speedVel;
            }
            velocity.x = speedVel;
            velocity.y = 0f;
        }
        
        System.out.println("Velocity x = " + velocity.x + "     Velocity y = " + velocity.y);
    }
    
    public void updateMovement(Enemy inputEnemy) {
        float delta = Gdx.graphics.getDeltaTime();
        enemySprite.translateX(delta * velocity.x);
        enemySprite.translateY(delta * velocity.y);
        enemyRectangle.set(enemySprite.getX(), enemySprite.getY(), enemySprite.getWidth()/2, enemySprite.getHeight()/2);
    }
    
    /**
     * Subtracts specified damage from health
     * @param damage as int damage taken
     */
    public void TakeDamage(int damage) {
        health -= damage;
        
    }
    
    /**
     * Gets percentage along track
     *
     * @return percentage as a float.
     */
    public float GetPercentage() {
        return percentage;
    }
    
    /**
     * Gets current location
     *
     * @return location as a Vector2.
     */
    public Vector2 GetLocation() {
        return location;
    }
    
    /**
     * Gets current tile that enemy is on
     *
     * @return current tile as a Vector2.
     */
    public Vector2 GetCurrentTile() {
        return currentTile;
    }
    
    
}
