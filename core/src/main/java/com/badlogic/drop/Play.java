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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 *
 * @author paulcaplin
 */
public class Play implements Screen {

    //Tanner added current
    PacketEnemy testPacket;
    //FitViewport viewport;
    //Movement update variable(s)
    float timerVar;
    float addPacketTimer;
    boolean spriteOverlapWaypoint;
    
    Array<PacketEnemy> packetArray;
    
    Texture backgroundTexture;
    
    Vector2 touchPos;
    
    
    SpriteBatch spriteBatch;
    
    Path path;
    
    float timerPrint;
    
    //End Tanner added
    
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
        //Tanner added
        spriteBatch = new SpriteBatch();
        timerVar = 0f;
        timerPrint = 0f;
        addPacketTimer = 0f;
        spriteOverlapWaypoint = false;
        
        backgroundTexture = new Texture("background.png");
        
        touchPos = new Vector2();
        
        packetArray = new Array<>();
    }
    

    @Override
    public void show() {
        
        camera = new OrthographicCamera();
    
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/gridLayout.tmx");
        
   
        
       
        
       
        
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
        
        // Create fitviewport with map dimensions and camera
        viewport = new FitViewport(mapWidth, mapHeight, camera);
        
        //Tanner added
        path = new Path(viewport);
        
        // Center camera on map
        camera.position.set(mapWidth /2, mapHeight /2 , 0);
        camera.update();
        
        renderer = new OrthogonalTiledMapRenderer(map);
        
   
        
        // Initialize tower array
        towers = new Array<>();
        
        System.out.println("Welcome to the GR1D. Place your defenses.");
        System.out.println("Tile size: " + tileWidth + "x" + tileHeight);
        
    }

    @Override
    public void render(float delta) {
        
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
        input();
        logic();
        draw();
        renderer.getBatch().end();
        
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
        addPacketTimer += delta;
        timerPrint += delta;
        
        // Max number of packets on the screen is 3 (can be changed by setting < 3 to < x)
        if (addPacketTimer > 3.0f & packetArray.size < 3) {
            createPacket();
            addPacketTimer = 0f;
        }
        
        for (PacketEnemy testPacketIn : packetArray) {
            if (testPacketIn.getTouchDetect(testPacketIn) > 2.0f)
            {
                for (int i = 0; i < path.waypointRectangleArray.size; i++) {
                    if (testPacketIn.enemyRectangle.overlaps(path.waypointRectangleArray.get(i)) & i < path.waypointRectangleArray.size-1 ) {
                        testPacketIn.ChangeVelocity(path.waypointRectangleArray.get(i) , path.waypointRectangleArray.get(i+1));
                        testPacketIn.resetTouchDetect();
                    }
                }
            }


            testPacketIn.updateMovement();
        }
        
        //Out of bounds Check
        for (int i = 0; i < packetArray.size; i++) {
            if (packetArray.get(i).checkOutOfBound()) {
                packetArray.removeIndex(i);
            }
        }
    }
    
    /**
     * createPacket spawns a packet enemy into the array of packet enemies
     * @author tdewe
     */
    private void createPacket() {
        PacketEnemy packet = new PacketEnemy(viewport);
        packetArray.add(packet);
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
        
        for (PacketEnemy packet : packetArray) {
            packet.enemySprite.draw(spriteBatch);
        }
        if (packetArray.size > 0 & timerPrint > 1.0f) {
           System.out.println("Enemy 0: loc x- " + packetArray.get(0).enemySprite.getX() 
                   + "  loc y- " + packetArray.get(0).enemySprite.getY());
           timerPrint = 0f;
        }
        
        spriteBatch.end();
    }
    
}

