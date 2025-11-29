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
    Texture packetTexture;
    Sprite packetSprite;
    
    
    /**
         * SniperTower() is the constructor for the Sniper class
         * @author xaplinzz
         */
    public SniperTower() {
        
        //Packet texturing and sprite
        packetTexture = new Texture("sniper.png");
        packetSprite = new Sprite(packetTexture);
    }
  
        
    
}
