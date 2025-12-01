/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.LinkedList;

/**
 *
 * @author natha
 */
public class EnemyHandler {
    public final int NUM_PATH_SEGMENTS = 14;
    public static final int NUM_ENEMY_TYPES = 3;
    public static final int MAX_NUM_ENEMIES = 100; // the max number of enemies of each enemy type that can exist
    
    // the percentage of each enemy type spawned at the start of the game; should sum to 1 and contain NUM_ENEMY_TYPES entries
    public static final float[] I_ENEMY_PROPORTIONS = {0.7f,0.2f,0.1f};
    // the percentage of each enemy type spawned at the end of the game; should sum to 1 and contain NUM_ENEMY_TYPES entries
    public static final float[] F_ENEMY_PROPORTIONS = {0.1f,0.6f,0.3f};
    // the rate of enemy spawns at the start of the game; must be above 0
    public static final float I_ENEMY_SPAWN_RATE = 0.3f;
    // the rate of enemy spawns at the end of the game; must be above 0, and should be above I_ENEMY_SPAWN_RATE
    public static final float F_ENEMY_SPAWN_RATE = 3f;
    // how much time passes until the final phase of the game starts; must be greater than 1
    public static final float END_GAME_START = 100f;
    // how much time passes until the game ends; must be greater than END_GAME_START
    public static final float END_GAME_END = 120f;
    
    private LinkedList<EnemyCracked> enemyList = new LinkedList();
    private LinkedList<EnemyCracked>[] path = new LinkedList[NUM_PATH_SEGMENTS];
    private LinkedList<EnemyCracked>[] enemyTypeLists = new LinkedList[NUM_ENEMY_TYPES];
    
    // linearly increases from 0 to 1 until the start of the final phase of the game
    private float difficulty;
    // linearly increases from I_ENEMY_SPAWN_RATE to F_ENEMY_SPAWN_RATE as difficulty increases
    private float spawnRate;
    // the percentage of each enemy type spawned
    private float[] enemyProportions;
    // the amount of time until the next instance of each enemy type spawns; calculated from the proper enemy proportions as dictated by I_ENEMY_PROPORTIONS and F_ENEMY_PROPORTIONS
    private float[] spawnCooldowns;
    
    FitViewport viewport;
    
    
    /**
     * Constructor for EnemyHandler.
     * Takes in game viewport so enemies can draw themselves.
     * @param gameViewport 
     */
    public EnemyHandler(FitViewport gameViewport) {
        viewport = gameViewport;
        difficulty = 0;
        spawnRate = I_ENEMY_SPAWN_RATE;
        
        for (int i = 0; i<=NUM_PATH_SEGMENTS-1;i++) {
            path[i] = new LinkedList();
        }
        
        enemyProportions = new float[NUM_ENEMY_TYPES];
        spawnCooldowns = new float[NUM_ENEMY_TYPES];
        for (int i = 0; i<=NUM_ENEMY_TYPES-1;i++) {
            enemyTypeLists[i] = new LinkedList();
            enemyProportions[i] = I_ENEMY_PROPORTIONS[i];
            spawnCooldowns[i] = 1 / I_ENEMY_PROPORTIONS[i];
        }
    }
    
    
    /**
     * Spawns enemies as appropriate for previously defined difficulty variation.
     */
    private void spawnEnemies() {
        float delta = Gdx.graphics.getDeltaTime();
        if (difficulty < 1) {
            spawnRate = ((1f - difficulty) * I_ENEMY_SPAWN_RATE) + (difficulty * F_ENEMY_SPAWN_RATE);
            for (int i = 0; i <= NUM_ENEMY_TYPES-1;i++){
                enemyProportions[i] = ((1f - difficulty) * I_ENEMY_PROPORTIONS[i]) + (difficulty * F_ENEMY_PROPORTIONS[i]);
            }
            difficulty += delta / END_GAME_START;
        } else if (difficulty >= 1) {
            spawnRate = F_ENEMY_SPAWN_RATE;
            for (int i = 0; i <= NUM_ENEMY_TYPES-1;i++){
                enemyProportions[i] = F_ENEMY_PROPORTIONS[i];
            }
        }
        
        for (int i = 0; i <= NUM_ENEMY_TYPES-1; i++) {
            if ((spawnCooldowns[i] <= 0) & (getNumEnemiesType(i) < MAX_NUM_ENEMIES)) {
                spawn(i);
                spawnCooldowns[i] = 1 / enemyProportions[i];
            }
            spawnCooldowns[i] -= delta * spawnRate;
        }
        
    }
    
    /**
     * Sorts one enemy forwards.
     * If, on a given path segment (pathIndex), there is exactly one enemy (at enemyIndex in path[pathIndex])
     * that could be out of order, and the Enemy cannot be too far back, then this method will place it in its
     * proper place in the list, resulting in a sorted list.
     * @param pathIndex
     * @param enemyIndex 
     */
    private void pathSortForwardEnemy(int pathIndex, int enemyIndex) {
        double enemyProg = path[pathIndex].get(enemyIndex).getProg();
        int listSize = path[pathIndex].size();
        if (enemyIndex + 1 >= listSize) {
            return;
        } else if (enemyProg <= path[pathIndex].get(enemyIndex+1).getProg()) {
            return;
        }
        int newIndex = enemyIndex + 1;
        while ((newIndex <= listSize-2) && (path[pathIndex].get(enemyIndex).getProg() > path[pathIndex].get(newIndex+1).getProg())) {
            newIndex++;
        }
        
        path[pathIndex].add(newIndex,path[pathIndex].get(enemyIndex));
        path[pathIndex].remove(enemyIndex);
    }
    
    /**
     * Merges all path segments into enemyList.
     */
    private void mergePaths() {
        enemyList.clear();
        for (int currPathSegment = 0; currPathSegment <= NUM_PATH_SEGMENTS-1;currPathSegment++) {
            enemyList.addAll(path[currPathSegment]);
        }
    }
    
    public void setViewport(FitViewport newViewport) {
        viewport = newViewport;
    }
    
    /**
     * Spawns an enemy, with enemy type indicated by the given ID.
     * @param enemyID 
     */
    private void spawn(int enemyID) {
        EnemyCracked newEnemy;
        switch (enemyID) {
            case 0:
                newEnemy = new EnemySlow(viewport);
                break;
            case 1:
                newEnemy = new EnemyQuick(viewport);
                break;
            case 2:
                newEnemy = new EnemyStutterer(viewport);
                break;
            default:
                return;
        }
        
        System.out.println("Spawned " + enemyID);
        enemyList.addFirst(newEnemy);
        enemyTypeLists[enemyID].addFirst(newEnemy);
        path[0].addFirst(newEnemy);
    }
    
    /**
     * Causes every enemy to act, and makes sure that they are all sorted.
     * Returns the number of enemies that hit the F1R3W4LL.
     * @return
     */
    public int action() {
        int numEnemies;
        int newPathSegment;
        int numFirewallHits = 0;
        EnemyCracked currEnemy;
        
        spawnEnemies();
        
        for (int currPathSegment = NUM_PATH_SEGMENTS - 1; currPathSegment >= 0; currPathSegment--) {
            numEnemies = path[currPathSegment].size();
            for (int currEnemyNum = numEnemies - 1;currEnemyNum >= 0;currEnemyNum--) {
                currEnemy = path[currPathSegment].get(currEnemyNum);
                if (currEnemy.getIsDead()) {
                    path[currPathSegment].remove(currEnemyNum);
                    enemyTypeLists[currEnemy.getID()].remove(currEnemy);
                } else {
                    currEnemy.act();
                    newPathSegment = (int)Math.floor(currEnemy.getProg());
                    if (newPathSegment >= NUM_PATH_SEGMENTS) {
                        path[currPathSegment].remove(currEnemyNum);
                        enemyTypeLists[currEnemy.getID()].remove(currEnemy);
                        numFirewallHits++;
                    } else if (currPathSegment != newPathSegment) {
                        path[currPathSegment].remove(currEnemyNum);
                        enemyTypeLists[currEnemy.getID()].remove(currEnemy);
                        path[newPathSegment].addFirst(currEnemy);
                        pathSortForwardEnemy(newPathSegment,0);
                    } else {
                        pathSortForwardEnemy(currPathSegment,currEnemyNum);
                    }
                }
            }
        }
        mergePaths();
        return numFirewallHits;
    }
    
    /**
     * Returns the enemy furthest along the path;
     * @return 
     */
    public EnemyInterface getLatestEnemy() {
        return enemyList.getLast();
    }
    
    /**
     * Returns the list of all enemies sorted by position along the path.
     * @return 
     */
    public LinkedList<EnemyInterface> getEnemies() {
        final LinkedList<EnemyInterface> outList;
        outList = new LinkedList();
        for (EnemyCracked enemy : enemyList) {
            outList.add(enemy);
        }
        return outList;
    }
    
    /**
     * Returns a list of every enemy of a given enemy type, sorted by position along the path.
     * @param id
     * @return 
     */
    public LinkedList<EnemyInterface> getEnemiesType(int id) {
        final LinkedList<EnemyInterface> outList;
        outList = new LinkedList();
        if ((id >= 0) && (id <= NUM_ENEMY_TYPES-1)) {
            for (EnemyInterface enemy : enemyTypeLists[id]) {
                outList.add(enemy);
            }
        }
        return outList;
    }
    
    /**
     * Returns a list of every enemy on a given path segment, sorted by position along the path.
     * @param index
     * @return 
     */
    public LinkedList<EnemyInterface> getEnemiesPathSegment(int index) {
        final LinkedList<EnemyInterface> outList;
        outList = new LinkedList();
        if ((index >= 0) && (index <= NUM_PATH_SEGMENTS-1)) {
            for (EnemyCracked enemy : path[index]) {
                outList.add(enemy);
            }
        }
        return outList;
    }
    
    
    /**
     * Returns the total number of enemies on the grid.
     * @return 
     */
    public int getNumEnemies() {return enemyList.size();}
    
    /**
     * Returns the number of enemies of a given type.
     * @param id
     * @return 
     */
    public int getNumEnemiesType(int id) {
        if ((id >= 0) && (id <= NUM_ENEMY_TYPES-1)) {
            return enemyTypeLists[id].size();
        } else {
            return 0;
        }
    }
    
    /**
     * Returns the number of enemies on a given path segment.
     * @param index
     * @return 
     */
    public int getNumEnemiesPathSegment(int index) {return path[index].size();}
}
