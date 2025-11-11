/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import java.util.LinkedList;



/**
 *
 * @author paulcaplin
 */
public class Play implements Screen {

    //pause button WIP info
    private Stage stage;
    ImageButton.ImageButtonStyle style;
    ImageButton pauseButton;
    //GameState enumerated type controls the current gamestate
    enum GameState {
        INGAME, PAUSED
    }
    GameState gameState;
    //Tanner added current
    //Movement update variable(s)
    float timerVar;
    boolean spriteOverlapWaypoint;
    
    EnemyHandler enemyHandler;
    
    float spawnCDEnemySlow;
    float spawnCDEnemyQuick;
    float spawnCDEnemyStutterer;
    
    Texture backgroundTexture;
    
    Vector2 touchPos;
    
    LinkedList<EnemyInterface> enemyList;
    
    
    SpriteBatch spriteBatch;
    
    
    float timerPrint;
    
    float attackTimer;
    
    //End Tanner added
    
    private float shopStartX;
    private int shopWidth;
    private float mapWidth;
    private float mapHeight;
    float totalWidth;
    
    FitViewport viewport;
    
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    
    // Tower Management
    private Array<Tower> towers = new Array<>();
    private Texture towerTexture;
    
    
    //Map properties
    private int tileWidth;
    private int tileHeight;
    
    public Play() {
        //Pause button properties
        style = new ImageButton.ImageButtonStyle();
        // pauseButton Texture
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("pauseButton.png")));
        // playButton(resume) Texture
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture("playButton.png")));
        pauseButton = new ImageButton(style);
        pauseButton.setSize(40,40);
        
        gameState = GameState.INGAME;
        
        //Tanner added
        spriteBatch = new SpriteBatch();
        timerVar = 0f;
        timerPrint = 0f;
        spawnCDEnemySlow = 1f;
        spawnCDEnemyQuick = 2f;
        spawnCDEnemyStutterer = 5f;
        attackTimer = 0f;
        spriteOverlapWaypoint = false;
        
        backgroundTexture = new Texture("background.png");
        
        
        
        touchPos = new Vector2();
        
        EnemyCommander enemyCommand = new EnemyCommander();
        float prog = -1;
        while (prog <= 15) {
            System.out.println(prog + "-" + (prog+0.5f) % 1f + ": (" + enemyCommand.progToPos(prog).x + "," + enemyCommand.progToPos(prog).y + ")");
            prog += 0.052313f;
        }
        
    }
    

    @Override
    public void show() {
        
        camera = new OrthographicCamera();
    
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/gridLayout.tmx");
        
   
        
       // Shop width in pixels variable
        shopWidth = 350;
        
       
        
        // Get tile dimensions from map
        tileWidth =map.getProperties().get("tilewidth", Integer.class);
        tileHeight = map.getProperties().get("tileheight",Integer.class);
        
        // Get map dimensions in tiles
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        int mapWidthinTiles = layer.getWidth();
        int mapHeightinTiles = layer.getHeight();
        
        // Calculate world dimensiions in pixels
        float mapWidth = mapWidthinTiles * tileWidth;
        float mapHeight = mapHeightinTiles * tileHeight;
        
        // Setting pause button spot
        pauseButton.setX(mapWidth - tileWidth * 3f/4);
        pauseButton.setY(mapHeight - tileHeight * 3f/4);
        
        // Allocate space for the shop
        totalWidth = mapWidth + shopWidth;
        shopStartX = mapWidth;
        // Create fitviewport with map dimensions and camera
        viewport = new FitViewport(totalWidth, mapHeight, camera);
        
        //Tanner added
        enemyHandler = new EnemyHandler(viewport);
        
        // Center camera on map
        camera.position.set(mapWidth /2, mapHeight /2 , 0);
        camera.update();
        
        renderer = new OrthogonalTiledMapRenderer(map);
        
   
        
        // Initialize tower array
        towers = new Array<>();
        
        System.out.println("Welcome to the GR1D. Place your defenses.");
        System.out.println("Tile size: " + tileWidth + "x" + tileHeight);
        
        //Pause button (and other buttons later) stage creation
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(pauseButton);
        // pauseButton click listener
        pauseButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               if (gameState == GameState.INGAME) {
                   gameState = GameState.PAUSED;
               } else if (gameState == GameState.PAUSED) {
                   gameState = GameState.INGAME;
               }
           }
            
        });
        
    }

    @Override
    public void render(float delta) {
        // Rendering stuff while in game
        if (gameState == GameState.INGAME) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            viewport.apply();
            camera.update();

            renderer.setView(camera);
            renderer.render();

            if (Gdx.input.justTouched()){
                handleTowerPlacement();
            }

            // Render towers
            renderer.getBatch().begin();
            for (Tower tower : towers){
                tower.render(renderer.getBatch());
            }

            //Tanner added
            stage.act(delta);
            input();
            logic();
            draw();
            stage.draw();
            renderer.getBatch().end();
        } else if (gameState == GameState.PAUSED) {
            // Does stuff for paused gameState
        } else {
            //Should never reach here
            System.out.println("Something broke, in invalid gameState. Terminating");
            Gdx.app.exit();
        }
        
        
    }
    
    private void handleTowerPlacement(){
        //Get mouse position
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        
        
        // Converts screen coords to world coords
        Vector3 worldCoords = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0));
        
        // Converts world coords to tile coords
        int tileX = (int) Math.floor(worldCoords.x /  tileWidth);
        int tileY = (int) Math.floor(worldCoords.y / tileHeight);
       
        
        // debug lines
        System.out.println("Screen: (" + mouseX + ", " + mouseY + ")");
        System.out.println("World: (" + worldCoords.x + ", " + worldCoords.y + ")");
        System.out.println("Tile: (" + tileX + ", " + tileY + ")");
        System.out.println("World bounds - Width: " + (viewport.getWorldWidth()) + ", Height: " + (viewport.getWorldHeight()));
       
        
        // Checks if the tile is buildable
        if(isBuildable(tileX, tileY)){
            // Place tower
            Minigun tower = new Minigun();
            tower.setPosition(tileX * tileWidth, tileY * tileHeight, tileWidth);
            towers.add(tower);
            System.out.println("Tower placed at the tile: " + tileX + ", " + tileY);
        } else {
            System.out.println("ERR0R. CANNOT BUILD!");
        }
         
    }

    private boolean isBuildable(int tileX, int tileY){
        // Get the buildable layer from the map
        TiledMapTileLayer buildableLayer = (TiledMapTileLayer) map.getLayers().get(0);
        
     
        
        // Check if there's a buildable tile at thid position
        TiledMapTileLayer.Cell cell = buildableLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;
        
        
        int tileId = cell.getTile().getId();
        
        System.out.println("Tile ID at (" + tileX + ", " + tileY + "): " + tileId);
        
        // Check if a tower already exists here
        for (Tower tower : towers) {
            if (tower.getTileX() == tileX && tower.getTileY() == tileY){
                
                return false;
            }
        }
        
        // Checks if the tile is an enemy tile 
        if (tileId == 3){
            
         
            
            return false;
        }
         
       
        
        
        
        
        return true;
    }
    
    @Override
    public void resize(int width, int height){ 
       viewport.update(width,height, true);
      
       /*
      camera.viewportWidth = width;
      camera.viewportHeight = height;
      camera.update();
        */
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
    
    
    
    //Tanner added main methods
    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        timerVar += delta;
        timerPrint += delta;
        attackTimer += delta;
        spawnCDEnemySlow -= delta;
        spawnCDEnemyQuick -= delta;
        spawnCDEnemyStutterer -= delta;
        
        if (spawnCDEnemySlow <= 0f & enemyHandler.getNumEnemiesType(0) < 4) {
            enemyHandler.spawn(0);
            spawnCDEnemySlow = 3f;
        }
        if (spawnCDEnemyQuick <= 0f & enemyHandler.getNumEnemiesType(1) < 4) {
            enemyHandler.spawn(1);
            spawnCDEnemyQuick = 7f;
        }
        if (spawnCDEnemyStutterer <= 0f & enemyHandler.getNumEnemiesType(2) < 4) {
            enemyHandler.spawn(2);
            spawnCDEnemyStutterer = 10f;
        }
        
        enemyHandler.action();
        
        
        
        // Enemy Takes Damage
        if (enemyHandler.getNumEnemies() > 0 && attackTimer > 1.0f) {
            for (Tower tower : towers) {
                enemyHandler.getLatestEnemy().damage(tower.damage);
                
                if (!tower.isInAnim) {
                    tower.activateShootAnimation();
                }
                
                
                
            }
            attackTimer = 0f;
        }
        
        // Tower shooting animation is updated
        if (towers.size > 0) {
            for (Tower tower : towers)
            {
                if (tower.isInAnim)
                {
                    tower.updateShootAnimation();
                }
            }
        }
        
    }
    
    private void input() {
        
    }
    
    private void draw() {
        viewport.apply();
        
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        
        //Draws the waypoints onto the screen for testing / debugging
        /*for (Sprite waypointSprite : path.waypointSpriteArray) {
            waypointSprite.draw(spriteBatch);
        }*/
        
        enemyList = enemyHandler.getEnemies();
        
        for (EnemyInterface enemy : enemyList) {
            enemy.getEnemySprite().draw(spriteBatch);
        }
        if (enemyHandler.getNumEnemies() > 0 & timerPrint > 1.0f) {
           System.out.println("Enemy 0: loc x- " + enemyList.getLast().getEnemySprite().getX() 
                   + "  loc y- " + enemyList.getLast().getEnemySprite().getY());
           timerPrint = 0f;
        }
        
        spriteBatch.end();
    }
    
}

