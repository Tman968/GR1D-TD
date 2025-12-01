/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * sniper subclass
 * @author paulcaplin
 */
public class SniperTower extends Tower {
    /**
     * SniperTower class is a tower subclass that creates and manages Sniper units
     * @author xaplinzz
     */
    
    
    
    /**
         * SniperTower() is the constructor for the Sniper class
         * @author xaplinzz
         */
    public SniperTower() {
        super();
        
        //sniper stats
        damage = 30.0f;
        range = 150;
        cost = 100;
        fireRate = 1.2f;
        //Packet texturing and sprite
        defaultTowerSpriteTexture = new Texture("towers/Sniper.png");
        defaultTowerSpriteTextureShooting = new Texture("towers/SniperShoot.png");
        defaultTowerSprite = new Sprite(defaultTowerSpriteTexture);
    }
  
        
    
}
