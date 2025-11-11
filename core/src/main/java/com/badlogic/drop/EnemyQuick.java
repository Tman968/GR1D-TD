/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * A quick enemy type.
 * Extends EnemyAbstract.
 * @author natha
 */
public class EnemyQuick extends EnemyAbstract {
    
    /**
     * Constructs using the EnemyAbstract constructor with passed variables for ID, max hp, and speed respectively.
     * @param gameViewport
     */
    public EnemyQuick(FitViewport gameViewport) {
        super(gameViewport, 1,7.3f,1.362768f); // initializes using the EnemyAbstract contructor
    }
}
