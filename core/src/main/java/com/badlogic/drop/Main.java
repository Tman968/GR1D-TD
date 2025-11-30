package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    
    @Override
    public void create() {
        
        setScreen(new Play());
        
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
       

        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        // Draw your application here.
        super.render();
        
        
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
        super.pause();
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
        super.resume();
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
        super.dispose();
    }
}
