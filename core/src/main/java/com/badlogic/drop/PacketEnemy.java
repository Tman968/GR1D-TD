/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * packet subclass
 * @author tdewe
 */
public class PacketEnemy extends Enemy {
    
    
    /**
         * PacketEnemy() is the constructor for the packet class
         * @author tdewe
         * @param gameViewport takes the viewport of the class packetEnemy is created in
         */
    public PacketEnemy(FitViewport gameViewport) {
        //Setting up viewport
        viewport = gameViewport;
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();
        
        //Starting location
        location.x = 0f;
        location.y = (worldHeight * 3.5f/8);
        
        //Packet texturing and sprite
        enemyTexture = new Texture("packet.png");
        enemySprite = new Sprite(enemyTexture);
        enemySprite.setSize(48f, 48f);
        enemySprite.setX((location.x)-enemySprite.getWidth()/2);
        enemySprite.setY((location.y)-enemySprite.getHeight()/2);
        
        // Variables for default packet stats placeholders for now
        health = 100;
        speed = 0;
        velocity.x = 40f; //Constant speed of packet
        velocity.y = 0f;
        percentageUpdate = 0.01f;
        //Max velocity may not be used in the program, as a constant speed may be used
        // (no acceleration)
        maxVelocity = 3f; //Constant that will be determined through testing
        deathMoney = 25; // How much money the packet gives you
        damage = 5; // how much damage the enemy does when it gets to you
    }
    
    /**
     * updateMovement updates the position of the enemy along with its rectangle
     */
    public void updateMovement() {
        super.updateMovement(this);
    }
    
    public float touchDetectTimer() {
        return super.getTouchDetect(this);
    }
    
    public void resetTouchDetect() {
        super.resetTouchDetect(this);
    }
    
    public boolean checkOutOfBound() {
        return super.checkOutOfBound(this);
    }
}
