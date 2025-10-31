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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.cell;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 *
 * @author paulcaplin
 */
public class Play implements Screen {

    
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
    
}

