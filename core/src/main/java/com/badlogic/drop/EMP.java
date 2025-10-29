/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * EMP is a Tower subclass that creates a EMP tower unit.
 * @author tdewe
 */
public class EMP extends Tower{
    
    Texture empTexture;
    Sprite empSprite;
    
    /**
     * EMP is a Tower subclass that creates a EMP tower unit.
     * @author tdewe
     */
    
    public EMP() { //Constructor
        /**
         * EMP() is the constructor for the EMP tower subclass.
         * @author tdewe
         */
        
        empTexture = new Texture("EMP.png");
        empSprite = new Sprite(empTexture);
    }
    
    
}
