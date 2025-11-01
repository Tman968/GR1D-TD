package com.badlogic.drop;

import java.util.LinkedList;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** 
 * 
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. 
 */
public class Main implements ApplicationListener {
    
    FitViewport viewport;
    //Movement update variable(s)
    float spawnCDEnemySlow;
    float spawnCDEnemyQuick;
    float spawnCDEnemyStutterer;
    
    EnemyHandler enemyHandler;
    Path enemyCommander;
    
    Texture backgroundTexture;
    
    Vector2 touchPos;
    
    LinkedList<EnemyInterface> enemyList;
    
    
    SpriteBatch spriteBatch;
    
    
    /**
     * its main
     * @author jacob
     */
    /*public Main(){
        
    }*/
    
    @Override
    public void create() {
        // Prepare your application here.
        viewport = new FitViewport(8,5);
        spriteBatch = new SpriteBatch();
        spawnCDEnemySlow = 1f;
        spawnCDEnemyQuick = 2f;
        spawnCDEnemyStutterer = 5f;
        
        enemyHandler = new EnemyHandler();
        enemyCommander = enemyHandler.getPathData();
        
        backgroundTexture = new Texture("background.png");
        
        touchPos = new Vector2();
        
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0);

        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        // Draw your application here.
        input();
        logic();
        draw();
        
        
    }
    
    private void input() {
        
        //float speed = 4f;
        //float delta = Gdx.graphics.getDeltaTime();
        
        if (Gdx.input.isTouched())
        {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // get location of touch
            viewport.unproject(touchPos); //Convert units to world units
            //testPacket.enemySprite.setCenterX(touchPos.x); //change the horizontally centered position of the bucket
            //testPacket.enemySprite.setCenterY(touchPos.y);
        }
    }
    
    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        spawnCDEnemySlow -= delta;
        spawnCDEnemyQuick -= delta;
        spawnCDEnemyStutterer -= delta;
        
        if (spawnCDEnemySlow <= 0f & enemyHandler.getNumEnemiesType(0) <= 3) {
            enemyHandler.spawn(0);
            spawnCDEnemySlow = 3f;
        }
        if (spawnCDEnemyQuick <= 0f & enemyHandler.getNumEnemiesType(1) <= 3) {
            enemyHandler.spawn(1);
            spawnCDEnemyQuick = 7f;
        }
        if (spawnCDEnemyStutterer <= 0f & enemyHandler.getNumEnemiesType(2) <= 3) {
            enemyHandler.spawn(2);
            spawnCDEnemyStutterer = 10f;
        }
        
        enemyHandler.action();
    }
    
    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // Local Variables for Sprites 
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        
        spriteBatch.draw(backgroundTexture, 0,0, worldWidth, worldHeight);
        //testPacket.enemySprite.draw(spriteBatch);
        
        
        enemyList = enemyHandler.getEnemies();
        
        for (Sprite waypointSprite : enemyCommander.waypointSpriteArray) {
            waypointSprite.draw(spriteBatch);
        }
        
        for (EnemyInterface enemy : enemyList) {
            enemy.getEnemySprite().draw(spriteBatch);
        }
        
        spriteBatch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}