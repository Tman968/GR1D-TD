/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 *
 * @author natha
 */
public class EnemySlow extends EnemyAbstract {
    /**
     * Constructs using the EnemyAbstract constructor with passed variables for ID, max hp, and speed respectively.
     * @param gameViewport
     */
    public EnemySlow(FitViewport gameViewport) {
        super(gameViewport,0,16.5f,0.61013f);
    }
}
