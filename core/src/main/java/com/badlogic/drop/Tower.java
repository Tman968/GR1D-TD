/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Tower superclass that creates a layout used for all tower units.
 * @author tdewe
 */
public class Tower {
    //Tower interaction variables
    float damage;
    int range;
    int cost;
    int fireRate; //int correct type? maybe float?
    Vector2 location;
    boolean isHacked;
    
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
        defaultTowerSprite = new Sprite(defaultTowerSpriteTexture);
        defaultBulletSprite = new Sprite(defaultBulletTexture);
        
        
        //Setting default variable values
        damage = 0.0f;
        range = 0;
        cost = 0;
        fireRate = 0;
    }
    
}
