package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** 
 * 
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. 
 */
public class Main implements ApplicationListener {
    
    PacketEnemy testPacket;
    FitViewport viewport;
    //Movement update variable(s)
    float timerVar;
    float addPacketTimer;
    boolean spriteOverlapWaypoint;
    
    private Stage stage;
    private Table table;
    private Button shopButton;
    private Skin shopButtonSkin;
    private TextureAtlas atlas;
    private AtlasRegion region;
    private Sprite sprite;


    
    
    Array<PacketEnemy> packetArray;
    
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
        
        
        atlas = new TextureAtlas(Gdx.files.internal("Atlas/GR1D_atlas.atlas"));
        
        
        
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        table.setDebug(true); // This is optional, but enables debug lines for tables.
        
        shopButtonSkin = new Skin(atlas);
        
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        
        buttonStyle.up = shopButtonSkin.getDrawable("shopButton");
        
        shopButtonSkin.add("default", buttonStyle);
        
        shopButton = new Button(shopButtonSkin);
        
        table.add(shopButton).expand().center().width(2).height(1);
        
        
    
        spriteBatch = new SpriteBatch();
        timerVar = 0f;
        addPacketTimer = 0f;
        spriteOverlapWaypoint = false;
        
        backgroundTexture = new Texture("background.png");
        
        touchPos = new Vector2();
        
        path = new Path();
        path.createPath();
        path.createWaypoints();
        
        packetArray = new Array<>();
        
        
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        // Draw your application here.
        
        float delta = Gdx.graphics.getDeltaTime();
        input();
        logic();
        stage.act(delta);
        
        
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        draw();
        
        stage.draw();
        
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
        float delta = Gdx.graphics.getDeltaTime();
        timerVar += delta;
        addPacketTimer += delta;
        
        if (addPacketTimer > 3.0f & packetArray.size < 3) {
            createPacket();
            addPacketTimer = 0f;
        }
        
        /*
        for (PacketEnemy testPacketIn : packetArray) {
            if (testPacketIn.getTouchDetect(testPacketIn) > 0.5f)
            {
                for (int i = 0; i < path.waypointRectangleArray.size; i++) {
                    if (testPacketIn.enemyRectangle.overlaps(path.waypointRectangleArray.get(i)) & i < path.waypointRectangleArray.size-1 ) {
                        testPacketIn.ChangeVelocity(testPacketIn, path.waypointRectangleArray.get(i) , path.waypointRectangleArray.get(i+1));
                        testPacketIn.resetTouchDetect();
                    }
                }
            }


            testPacketIn.updateMovement();
        }
        */
        
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
        
        for (Sprite waypointSprite : path.waypointSpriteArray) {
            waypointSprite.draw(spriteBatch);
        }
        
        for (PacketEnemy packet : packetArray) {
            packet.enemySprite.draw(spriteBatch);
        }
        
        spriteBatch.end();
    }
    /**
     * createPacket spawns a packet enemy into the array of packet enemies
     * @author tdewe
     */
    private void createPacket() {
        PacketEnemy packet = new PacketEnemy();
        packetArray.add(packet);
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
        
        stage.dispose();
    }
}