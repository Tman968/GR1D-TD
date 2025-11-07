/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 *
 * @author tdewe
 */
public class Path {
    Array<Vector2> pathPoints;
    Texture waypointTexture;
    Sprite waypointSpriteDefault;
    FitViewport viewport;
    Array<Sprite> waypointSpriteArray;
    Array<Rectangle> waypointRectangleArray;
    
    
    public Path(FitViewport gameViewport) {
        pathPoints = new Array<>();
        waypointSpriteArray = new Array<>();
        waypointRectangleArray = new Array<>();
        waypointTexture = new Texture("dotWaypoint.png");
        waypointSpriteDefault = new Sprite(waypointTexture);
        waypointSpriteDefault.setSize(20f, 20f);
        viewport = new FitViewport(8,5);
        
        createPath(gameViewport);
        createWaypoints();
    }
    
    /**
     * createPath sets the path coordinates for the class to use for path following
     * 
     * @author tdewe
     */
    private void createPath(FitViewport gameViewport) {
        viewport = gameViewport;
        viewport.apply();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        worldWidth = (worldWidth/8);
        worldHeight = (worldHeight/8);
       
        
        System.out.println("WorldWidth: " + worldWidth);
        System.out.println("WorldHeight: " + worldHeight);
        
        pathPoints.add(new Vector2(0f, (worldHeight * 3.5f)));
        pathPoints.add(new Vector2(worldWidth * 3.5f, worldHeight * 3.5f));
        pathPoints.add(new Vector2(worldWidth * 3.5f, worldHeight * 5.5f));
        pathPoints.add(new Vector2(worldWidth * 5.5f, worldHeight * 5.5f));
        pathPoints.add(new Vector2(worldWidth * 5.5f, worldHeight * 1.5f));
        pathPoints.add(new Vector2(worldWidth * 8.0f, worldHeight *1.5f));
        
        int i = 0;
        for (Vector2 waypoint : pathPoints) {
            System.out.println("Point " + i + ":    " + waypoint);
            i++;
        }
        
    }
    
    /**
     * createWaypoints creates the visual waypoints used for testing the path following algorithm
     * 
     * @author tdewe
     */    
    private void createWaypoints() {
        //For loop to create the number of waypointSprites equal to the number of pathPoints
        float waypointWidth = 20f;
        float waypointHeight = 20f;
        float waypointSpriteWidth = waypointSpriteDefault.getWidth();
        float waypointSpriteHeight = waypointSpriteDefault.getHeight();
        
        for (int i = 0; i < pathPoints.size; i++) {
            Sprite waypointSprite = new Sprite (waypointTexture);
            Rectangle waypointRectangle = new Rectangle();
            waypointSprite.setSize(waypointWidth, waypointHeight);
            waypointSprite.setX((pathPoints.get(i).x) - waypointSpriteWidth/2);
            waypointSprite.setY((pathPoints.get(i).y) - waypointSpriteHeight/2);
            waypointRectangle.set(waypointSprite.getX(), waypointSprite.getY(), waypointSprite.getWidth(), waypointSprite.getHeight());
            waypointRectangleArray.add(waypointRectangle);
            waypointSpriteArray.add(waypointSprite);
        }
    }
    
    
}
