/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 *
 * Manages the tower shop interface
 * Displays available towers, handles the selection, and manages player currency
 * @author paulcaplin
 */
public class Shop {
    
    // Visual Components
    private Texture shopTexture;
    
    public int baseCurrency = 1000;
    
    // Visual Components and Tower Icons
    private Texture shopBackTexture;
    private Texture minigunIcon;
    private Texture sniperIcon;
    private Texture empIcon;
    
    // Shop button rectangles for click detection
    private Rectangle minigunButton;
    private Rectangle sniperButton;
    private Rectangle empButton;
    
    // Shop position and dimensions
    private float shopX;
    private float shopY;
    private float shopWidth;
    private float shopHeight;
    
    // Button Dimensions 
    private float buttonWidth = 80f;
    private float buttonHeight = 80f;
    private float buttonPadding = 20f;
    
    // Selected type of tower (zero means no tower)
    private int selectedTower = 0;
    
    // Type of tower 
    private static final int minigunTower = 1;
    private static final int sniperTower = 2;
    private static final int empTower = 3;
    
    // font for the labels
    private BitmapFont font;
    private final FitViewport viewport;
    
    
    /**
     * Constructor for shop
     * @param x Shop X position
     * @param y Shop Y position
     * @param width Shop width
     * @param height Shop height
     * @param viewport the game viewport for coordinate conversion
     */
    public Shop(float x, float y, float width, float height, FitViewport viewport) {
        this.shopX = x;
        this.shopY = y;
        this.shopWidth = width;
        this.shopHeight = height;
        this.viewport = viewport;
        
        // Initialize textures
        minigunIcon = new Texture("towers/GR1D_Turret_1.png");
        sniperIcon = new Texture("shopButton.png");
        empIcon = new Texture("shopButton.png");
        
        // Initialize buttons
        float startY = shopY + shopHeight - buttonHeight - buttonPadding;
        
        minigunButton = new Rectangle(shopX + buttonPadding, startY, buttonWidth, buttonHeight);
        sniperButton = new Rectangle(shopX + buttonPadding, startY - buttonHeight - buttonPadding, buttonWidth,buttonHeight);
        empButton = new Rectangle(shopX + buttonPadding,   startY - (buttonHeight + buttonPadding) * 2, buttonWidth, buttonHeight);
        
        // Initialize font
        font = new BitmapFont();
    }
        
       
    
    /**
     * Handles touch input for tower selection
     * @param screenX Screen coordinates in X
     * @param screenY Screen coordinates in Y
     * @return true if a tower was selected
     */
    public boolean handleTouch(int screenX, int screenY){
        
        
        //Convert screen corrdinates to world coordinates
        Vector3 worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
        float worldX = worldCoords.x;
        float worldY = worldCoords.y;
        
        // Checks if the users click is within the bounds of the shop area
        if (worldX < shopX || worldX > shopX + shopWidth){
            return false;
        }
        
        // Checks which button is clicked
        
        if (minigunButton.contains(worldX, worldY)){
            selectedTower = minigunTower;
            System.out.println("Minigun Selected");
            return true;
        } else if(sniperButton.contains(worldX, worldY)){
            selectedTower = sniperTower;
            System.out.println("Sniper Selected");
            return true;
        } else if (empButton.contains(worldX, worldY)){
            selectedTower = empTower;
            System.out.println("EMP Selected");
            return true;
        }
        
        
        
        
        return false;
    }
    
    
    /**
     * Renders the shop
     * @param batch SpriteBatch to draw with
     */
    public void render(SpriteBatch batch){
        // Draws shop background
        
        //draw tower buttons
        batch.draw(minigunIcon, minigunButton.x, minigunButton.y, minigunButton.width, minigunButton.height);
        
        batch.draw(sniperIcon, sniperButton.x, sniperButton.y, sniperButton.width, sniperButton.height);
        
        batch.draw(empIcon, empButton.x, empButton.y, empButton.width, empButton.height);
        
        // Draw labels and costs
        font.draw(batch, "Minigun", minigunButton.x + buttonWidth + 10, minigunButton.y + buttonHeight/2);
        font.draw(batch, "$100", minigunButton.x + buttonWidth + 10, minigunButton.y + buttonHeight/2 - 20);
        
        font.draw(batch, "Sniper", sniperButton.x + buttonWidth + 10, sniperButton.y + buttonHeight/2);
        font.draw(batch, "$150", sniperButton.x + buttonWidth + 10, sniperButton.y + buttonHeight/2 - 20);
        
        font.draw(batch, "EMP", empButton.x + buttonWidth + 10, empButton.y + buttonHeight/2);
        font.draw(batch, "$200", empButton.x + buttonWidth + 10, empButton.y + buttonHeight/2 - 20);
        
    }
  
    
    /**
     * Get the currently selected tower type
     * @return Tower type constant or -1 if none selected
     */
    public int getSelectedTowerType() {
        return selectedTower;
    }
    
    /**
     * Clear the tower selection
     */
    public void clearSelection() {
        selectedTower = 0;
    }
    
    /**
     * Check if a tower is currently selected
     * @return true if a tower is selected
     */
    public boolean hasTowerSelected() {
        return selectedTower != 0;
    }
    
    /**
     * Get the rectangle of the currently selected button
     * @return Rectangle of selected button or null
     */
    private Rectangle getSelectedButton() {
        switch (selectedTower) {
            case minigunTower:
                return minigunButton;
            case sniperTower:
                return sniperButton;
            case empTower:
                return empButton;
            default:
                return null;
        }
    }
    
    /**
     * Dispose of resources
     */
    public void dispose() {
        minigunIcon.dispose();
        sniperIcon.dispose();
        empIcon.dispose();
        font.dispose();
    }
    
    
    
    
    
}
