
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    
    // set up table for tower interaction
    private Table towerTable;
    ImageButton.ImageButtonStyle deleteTowerButtonStyle;
    ImageButton deleteTowerButton;
    
    // Track which tile is currently selected
    private int selectedTileX = -1;
    private int selectedTileY = -1;
    
    EnemyHandler enemyHandler;
    
    float spawnCDEnemySlow;
    float spawnCDEnemyQuick;
    float spawnCDEnemyStutterer;
    
    Texture backgroundTexture;
    Texture BSODTexture;
    Vector2 touchPos;
    
    LinkedList<EnemyInterface> enemyList;
    
    
    SpriteBatch spriteBatch;
    
    private Firewall firewall;
    private Shop shop;
    
    float timerPrint;
    
    float attackTimer;
    
    //End Tanner added
    
    private float shopStartX;
    private int shopWidth;
    private float mapWidth;
    private float mapHeight;
    float totalWidth;
    
    // Tower type constants
    private static final int minigunTower = 1;
    private static final int sniperTower = 2;
    private static final int empTower = 3;
    
    FitViewport viewport;
    
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    
    // Tower Management
    private Array<Tower> towers = new Array<>();
    private Tower[][] towerGrid;
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
        
        // tower table properties
        deleteTowerButtonStyle = new ImageButton.ImageButtonStyle();
        // tableButtonFont
        //BitmapFont tableButtonfont = new BitmapFont();
        //towerButtonStyle.font = tableButtonfont;
        // towerButton Texture
        deleteTowerButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("towerHandler/deleteButtonUp.png")));
        deleteTowerButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("towerHandler/deleteButtonDown.png")));
        deleteTowerButton = new ImageButton(deleteTowerButtonStyle);

        // delete button listener
        deleteTowerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                // Remove tower from towerGrid array
                towerGrid[selectedTileX][selectedTileY] = null;
                // Parse through towers, deleting the tower that matches the tile
                for (Tower tower : towers) {
                    if (tower.getTileX() == selectedTileX && tower.getTileY() == selectedTileY) {
                        shop.sellTower(tower.towerType);
                        towers.removeValue(tower, true);
                    }
                }
                towerTable.setVisible(false); // Close the menu after selection
                System.out.println("Tower deleted at: " + selectedTileX + "," + selectedTileY);
            }
        });
        
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
        BSODTexture = new Texture("BSOD.png");
                
        // Initializes firewall with 100HP
        firewall = new Firewall(100);
        
        
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
        
        // Initialize towerGrid with new mapwidth and height
        towerGrid = new Tower[mapWidthinTiles][mapHeightinTiles];
        
        // Calculate world dimensiions in pixels
        float mapWidth = mapWidthinTiles * tileWidth;
        float mapHeight = mapHeightinTiles * tileHeight;
        
        // Setting pause button spot
        pauseButton.setX(mapWidth + tileWidth + 220);
        pauseButton.setY(mapHeight - tileHeight * 3f/4);
        
        // Allocate space for the shop
        totalWidth = mapWidth + shopWidth;
        shopStartX = mapWidth;
        // Create fitviewport with map dimensions and camera
        viewport = new FitViewport(totalWidth, mapHeight, camera);
        
        shop = new Shop(shopStartX, 0, shopWidth, mapHeight, viewport);
        
        
        
        //Tanner added
        enemyHandler = new EnemyHandler(viewport);
        
        // Center camera on map
        camera.position.set(totalWidth /2, mapHeight /2 , 0);
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
        
        // Tower Handler Table
        towerTable = new Table();
        towerTable.setSize(150, 100);
        
        towerTable.add(deleteTowerButton).size(56, 18);
        
        stage.addActor(towerTable);
        
        towerTable.setVisible(false);
        
    }

    @Override
    public void render(float delta) {
        // Rendering stuff while in game
        if (gameState == GameState.INGAME) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            viewport.apply();
            camera.update();

            // Render Map
            renderer.setView(camera);
            renderer.render();

            // Handle Input
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
            logic();
            draw();
            
            stage.draw();
            if(firewall.isDestroyed()) {
                renderer.getBatch().draw(BSODTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }
          
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
        
        // Convert the mouse click to Stage coordinates (UI coordinates)
        Vector2 stageCoord = stage.screenToStageCoordinates(new Vector2(mouseX, mouseY));

        // Check if the click hit any Actor (namely delete button) on the Stage
        Actor hitActor = stage.hit(stageCoord.x, stageCoord.y, true);

        // If an UI element is hit, don't place tower
        if (hitActor != null) {
            return;
        }
        
        // Converts screen coords to world coords
        Vector3 worldCoords = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0));
        
        // If click is in the shop area, handle the tower selection
        if (worldCoords.x >= shopStartX){
            shop.handleTouch(mouseX, mouseY);
            return; // Doesn't try to place the tower
        }
        
        // If no tower is selected, don't place anything
        if (!shop.hasTowerSelected()){
            System.out.println("No tower selected. Click a tower in the SH0P first.");
            return;
        }
        
        // Converts world coords to tile coords
        int tileX = (int) Math.floor(worldCoords.x / tileWidth);
        int tileY = (int) Math.floor(worldCoords.y / tileHeight);
        
        // Bounds check to prevent crash if clicking outside map area
        if (tileX < 0 || tileY < 0 || tileX >= towerGrid.length || tileY >= towerGrid[0].length) {
            towerTable.setVisible(false);
            return;
        }
       
        // debug lines
        System.out.println("Screen: (" + mouseX + ", " + mouseY + ")");
        System.out.println("World: (" + worldCoords.x + ", " + worldCoords.y + ")");
        System.out.println("Tile: (" + tileX + ", " + tileY + ")");
        System.out.println("World bounds - Width: " + (viewport.getWorldWidth()) + ", Height: " + (viewport.getWorldHeight()));
        
        
       
        // All the logic for interacted with the tile after clicking
        // First: check if the tile is an interactable Tower tile, vs a useless Enemy tile
        if (detectTowerTile(tileX, tileY)) {
            // Second: check if a tower is at the tile
            if (isTileEmpty(tileX, tileY)) {
                // If tile is empty, Place tower
                int selectedType = shop.getSelectedTowerType();
                // Attempt to purchase the tower
                if (shop.purchaseTower(selectedType)) {
                    Tower tower = createTowerFromSelection(selectedType);
                    
                    tower.setPosition(tileX * tileWidth, tileY * tileHeight, tileWidth);
                    towers.add(tower);
                    towerGrid[tileX][tileY] = tower;
                
                    // Hide menu if we just placed a tower
                    towerTable.setVisible(false);
                
                    System.out.println("Tower placed at the tile: " + tileX + ", " + tileY);
                }
            } 
            // If tile is not empty, show menu
            else {
                // Change selected tiles
                selectedTileX = tileX;
                selectedTileY = tileY;
                
                // Move Table
                towerTable.setX(tileX * tileWidth);
                towerTable.setY(tileY * tileHeight);
                towerTable.setVisible(true);
            }
        }
        else {
            // Clicked invalid area, hide menu
            towerTable.setVisible(false);
        }
    }
         
 // Dectect if specified Tile is Enemy or Tower Tile
    // If EnemyTile, return false
    // If TowerTile, return true
    private boolean detectTowerTile (int tileX, int tileY)
    {
        // Get the buildable layer from the map
        // This is layer 1 from the tmx file
        TiledMapTileLayer buildableLayer = (TiledMapTileLayer) map.getLayers().get(1);
        
        // Check if the tile exists
        TiledMapTileLayer.Cell cell = buildableLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) {
            System.out.println("ERROR. CANNOT BUILD! THIS TILE DOESN'T EXIST!");
            // If Enemy Tile, return false
            return false;
        }
        // If tile exists, get the id of the selected tile
        int tileId = cell.getTile().getId();
        System.out.println("Tile ID at (" + tileX + ", " + tileY + "): " + tileId);
        // Check if tile id is an enemy (value of 3) or tower (value of 1) tile
        if (tileId == 3) {
            System.out.println("ERROR. CANNOT BUILD! THIS TILE IS AN ENEMY TILE!");
            return false;
        }
        return true;
    }
    
    // Check if Tile has a Tower currently there
    private boolean isTileEmpty (int tileX, int tileY)
    {
        // Return bool based on if tile is empty
        return towerGrid[tileX][tileY] == null;    
    }

    private Tower createTowerFromSelection(int towerType) {
        switch (towerType) {
            case minigunTower:
                return new Minigun();
            case sniperTower:
                return new SniperTower();
            case empTower:
                return new EMP();
            default:
                System.out.println("Invalid tower type!");
                return null;
        }
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

    //Tanner added main methods
    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        timerVar += delta;
        timerPrint += delta;
        attackTimer += delta;
       
        int numFirewallHits = enemyHandler.action();
       
        // Apply damage to firewall for each enemy that reached the end
        if (numFirewallHits > 0) {
            int damagePerEnemy = 10; // Each enemy deals 10 HP damage
            firewall.takeDamage(numFirewallHits * damagePerEnemy);
            System.out.println("F1R3W4LL TAKES " + (int)(numFirewallHits * damagePerEnemy) + " DAMAGE! ");
        }
        
        // Check if game is over
        if (firewall.isDestroyed()) {
            //System.out.println("F1R3W4LL BREACHED! GAME OVER.");
            
            System.out.println("==============================");
            System.out.println("      MAINFRAME BREACHED!     ");
            System.out.println("          GAME OVER.          ");
            System.out.println("==============================");
        }
        
        // Enemy Takes Damage
        if (enemyHandler.getNumEnemies() > 0 && attackTimer > 0.3f) {
        
            for (Tower tower : towers) {
 
                if (!tower.isInAnim) {
                    tower.activateShootAnimation();
                }
                
            }
            attackTimer = 0f;
            
        }
        // Tower shooting animation is updated and shooting towers 
        if (towers.size > 0) {
            for (Tower tower : towers)
            {
                if (tower.isInAnim)
                {
                    if (tower.updateShootAnimation() && enemyHandler.getNumEnemies() > 0) {
                        enemyHandler.getLatestEnemy().damage(tower.damage);
                    }
                }
            }
        }
        if (enemyHandler.getNumEnemies() > 0) {
            LinkedList<EnemyInterface> currentEnemies = enemyHandler.getEnemies();
            for (EnemyInterface enemy: currentEnemies) {
                    if (enemy.getIsDead())
                    {
                        shop.addCurrency(5);
                    }
            }
        }
        
        
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
        
        shop.render(spriteBatch, firewall.getCurrentHealth(), firewall.getMaxHealth());
        
        spriteBatch.end();
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
        shop.dispose();
    }
}