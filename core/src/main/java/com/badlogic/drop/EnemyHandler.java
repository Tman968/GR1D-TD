/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.LinkedList;

/**
 *
 * @author natha
 */
public class EnemyHandler {
    public static final int NUM_PATH_SEGMENTS = 11;
    public static final int NUM_ENEMY_TYPES = 3;
    
    Path enemyCommander;
    
    private LinkedList<EnemyCracked> enemyList = new LinkedList();
    private LinkedList<EnemyCracked>[] path = new LinkedList[NUM_PATH_SEGMENTS];
    private LinkedList<EnemyCracked>[] enemyTypeLists = new LinkedList[NUM_ENEMY_TYPES];
    
    public EnemyHandler() {
        enemyCommander = new Path();
        
        for (int i = 0; i<=NUM_PATH_SEGMENTS-1;i++) {
            path[i] = new LinkedList();
        }
        
        for (int i = 0; i<=NUM_ENEMY_TYPES-1;i++) {
            enemyTypeLists[i] = new LinkedList();
        }
    }
    
    private void acceptCommand(EnemyCracked inEnemy) {
        if (inEnemy.getTouchDetect() > 0.5) {
            for (int waypointNum = 0; waypointNum <= enemyCommander.waypointRectangleArray.size-2;waypointNum++) {
                if (inEnemy.getHitbox().overlaps(enemyCommander.waypointRectangleArray.get(waypointNum))) {
                    inEnemy.changeVelocity(enemyCommander.waypointRectangleArray.get(waypointNum), enemyCommander.waypointRectangleArray.get(waypointNum+1));
                    inEnemy.resetTouchDetect();
                }
            }
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
    
    /**
     * Spawns an enemy, with enemy type indicated by the given ID.
     * @param enemyID 
     */
    public void spawn(int enemyID) {
        EnemyCracked newEnemy;
        switch (enemyID) {
            case 0:
                newEnemy = new EnemySlow();
                break;
            case 1:
                newEnemy = new EnemyQuick();
                break;
            case 2:
                newEnemy = new EnemyStutterer();
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
        
        for (int currPathSegment = NUM_PATH_SEGMENTS - 1; currPathSegment >= 0; currPathSegment--) {
            numEnemies = path[currPathSegment].size();
            for (int currEnemyNum = numEnemies - 1;currEnemyNum >= 0;currEnemyNum--) {
                currEnemy = path[currPathSegment].get(currEnemyNum);
                if (currEnemy.getIsDead()) {
                    path[currPathSegment].remove(currEnemyNum);
                    enemyTypeLists[currEnemy.getID()].remove(currEnemy);
                } else {
                    acceptCommand(currEnemy);
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
    
    public Path getPathData() {return enemyCommander;}
    
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
    
    public int getNumEnemies() {return enemyList.size();}
    
    public int getNumEnemiesType(int id) {
        if ((id >= 0) && (id <= NUM_ENEMY_TYPES-1)) {
            return enemyTypeLists[id].size();
        } else {
            return 0;
        }
    }
    
    public int getNumEnemiesPathSegment(int index) {return path[index].size();}
}
