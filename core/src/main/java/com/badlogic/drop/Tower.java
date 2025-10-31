/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Tower superclass that creates a layout used for all tower units.
 * @author tdewe
 */
public class Tower {
    //Tower interaction variables
    float damage;
    int range;
    int cost;
    float fireRate; //int correct type? maybe float?
    Vector2 location;
    boolean isHacked;
    
    // Tile Position
    protected int tileX;
    protected int tileY;
    
    //Texture variables
    Texture defaultTowerSpriteTexture;
    Texture defaultBulletTexture;
    
    //Default Sprites
    Sprite defaultTowerSprite;
    Sprite defaultBulletSprite;
    
   
    
    /**
         * Constructor for the tower superclass, setting default values.
         * @author tdewe
         */
    public Tower() { //Constructor
        
       
        
        //Setting up textures and sprites for a default class, will be overridden in children
        //defaultTowerSpriteTexture = new Texture("whatever.png");
        //defaultBulletTexture = new Texture("whatever.png");
        
        
        
        //Setting default variable values
        damage = 0.0f;
        range = 0;
        cost = 0;
        fireRate = 0;
        location = new Vector2();
    }

    
    
    /** 
     * Set the position of the tower
     * @param x World X coordinate
     * @param y World Y coordinate
     * @param tileSize size of tiles for calculating tile position
     */
    
    public void setPosition(float x, float y, int tileSize){
        this.location.set(x,y);
        this.tileX= (int) (x / tileSize);
        this.tileY = (int) (y / tileSize);
        
        // Update sprite position if it exists
        if (defaultTowerSprite != null){
            defaultTowerSprite.setPosition(x,y);
        }
    }
    
   
    
    public void render(Batch batch){
        if (defaultTowerSprite != null){
            defaultTowerSprite.draw(batch);
        }
    }
 
    // Update tower logic like attacking
    
    public void update(float delta, Array<Enemy> enemies){
        //Find closest enemy in range
        Enemy target = null;
        float closestDist = Float.MAX_VALUE;
        
        for(Enemy enemy : enemies){
            if (enemy == null) continue;
            
            float dist = Vector2.dst(location.x, location.y, enemy.getX(), enemy.getY());
            if (dist < range && dist < closestDist){
                closestDist = dist;
                target = enemy;
            }
        }
    }
    
    // Getters
    public int getTileX() {return tileX;}
    public int getTileY() {return tileY;}
    public Vector2 getLocation() {return location;}
    public int getRange() {return range;}
    public int getCost() {return cost;}
    
    
    
    
    
}
