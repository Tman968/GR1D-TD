/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.math.Rectangle;

/**
 * An extended variant of the standard EnemyInterface which includes the act function.
 * This interface grant the ability to command the enemy to move, and as such should not be used outside of the EnemyHandler class.
 * @author natha
 */
public interface EnemyCracked extends EnemyInterface {
    /**
     * Commands the enemy to act.
     * In other words, it asks the enemy to perform whatever tasks it should every "tick".
     */
    abstract public void act();
    abstract public void changeVelocity(Rectangle waypointStart, Rectangle waypointEnd);
    abstract public void resetTouchDetect();
}
