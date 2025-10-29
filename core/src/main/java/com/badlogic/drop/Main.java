package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** 
 * 
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. 
 */
public class Main implements ApplicationListener {
    
    PacketEnemy testPacket;
    FitViewport viewport;
    
    Texture backgroundTexture;
    
    Vector2 touchPos;
    
    
    SpriteBatch spriteBatch;
    
    Path path;
    
    
    /**
     * its main
     * @author jacob
     */
    /*public Main(){
        
    }*/
    
    @Override
    public void create() {
        // Prepare your application here.
        testPacket = new PacketEnemy();
        viewport = new FitViewport(8,5);
        spriteBatch = new SpriteBatch();
        
        backgroundTexture = new Texture("background.png");
        
        touchPos = new Vector2();
        
        path = new Path();
        path.createPath();
        path.createWaypoints();
        
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

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
            testPacket.enemySprite.setCenterX(touchPos.x); //change the horizontally centered position of the bucket
            testPacket.enemySprite.setCenterY(touchPos.y);
        }
    }
    
    private void logic() {
        
        testPacket.updateMovement();
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
        testPacket.enemySprite.draw(spriteBatch);
        
        for (Sprite waypointSprite : path.waypointSpriteArray) {
            waypointSprite.draw(spriteBatch);
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