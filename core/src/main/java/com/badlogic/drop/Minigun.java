/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * minigun subclass
 * @author tdewe
 */
public class Minigun extends Tower{
    /**
     * Minigun class is a tower subclass that creates and manages minigun units
     * @author tdewe
     */
    Texture minigunTexture;
    Texture minigunTextureShooting;
    Sprite minigunSprite;
    
    
    /**
         * Minigun() is the constructor for the Minigun class
         * @author tdewe
         */
    public Minigun() {
        super();
        
        //mingun stats
        damage = 10.0f;
        range = 150;
        cost = 100;
        fireRate = 5;
        
        
        minigunTexture = new Texture("towers/GR1D_Turret_1.png");
        minigunTextureShooting = new Texture("towers/GR1D_Turret_Shooting_1.png");
        minigunSprite = new Sprite(minigunTexture);
        
        defaultTowerSpriteTexture = minigunTexture;
        defaultTowerSpriteTextureShooting = minigunTextureShooting;
        defaultTowerSprite = minigunSprite;
    }
}
