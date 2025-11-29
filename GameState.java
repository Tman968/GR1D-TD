/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.badlogic.drop;
//import com.badlogic.gdx.math.Vector2;

/**
 * GameState class that keeps track of all information about the board
 * and the current board state.
 * @author jacob
 */
public class GameState {
    
    Tower[][] grid = new Tower[3][3];
    
    int currentRound;
    int money;
    
    
    /**
     * Constructor for the GameState
     * @author jacob
     */
    public GameState() {
        // TODO implement GameState constructor
        
    }
    
    /**
     * Increases round by 1 to the next round
     */
    public void NextRound() {
        // TODO implement NextRound method
    }
    
   /**
     * Subtracts specified damage from health
     * @param damage as int damage taken
     */
    
    /**
     * BuyTower will see if spot on grid is available,
     * If yes, then place tower in grid,
     * Then subtract Tower price from money
     * 
     * @param newTower as the new tower to be bought
     * @param x as x position
     * @param y as y position
     */
    public void BuyTower(Tower newTower, int x, int y) {
        // TODO implement BuyTower method
    }
    
    /**
     * Get the tower at specified location
     *
     * @param x as x position
     * @param y as y position
     * 
     * @return the tower
     */
    public Tower GetTower(int x, int y) {
        // TODO implement GetTower method
        
        return null;
    }

    /**
     * Get the current round

     * @return current round as an int
     */
    public int GetCurrentRound() {
        // TODO implement GetCurrentRound method
        return currentRound;
    }
    
    /**
     * Get the current money

     * @return current money as an int
     */
    public int GetMoney() {
        // TODO implement GetMoney method
        return money;
    }
    
}

