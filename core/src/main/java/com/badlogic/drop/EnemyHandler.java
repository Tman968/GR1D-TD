/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import java.util.LinkedList;

/**
 *
 * @author natha
 */
public class EnemyHandler {
    private static final int NUM_PATH_SEGMENTS = 8;
    private static final int NUM_ENEMY_TYPES = 3;
    
    private LinkedList<EnemyCracked> enemyList = new LinkedList();
    private LinkedList<EnemyCracked>[] enemyTypeLists = new LinkedList[NUM_ENEMY_TYPES];
    private LinkedList<EnemyCracked>[] path = new LinkedList[NUM_PATH_SEGMENTS];
    
    /**
     * Sorts one enemy forwards.
     * If, on a given path segment (pathIndex), there is exactly one enemy (at enemyIndex in path[pathIndex])
     * that could be out of order, and the Enemy cannot be too far back, then this method will place it in its
     * proper place in the list, resulting in a sorted list.
     * @param pathIndex
     * @param enemyIndex 
     */
    private void pathSortForwardEnemy(int pathIndex, int enemyIndex) {
        double enemyPos = path[pathIndex].get(enemyIndex).getProg();
        int listSize = path[pathIndex].size();
        if (enemyIndex + 1 > listSize-1) {
            return;
        } else if (enemyPos <= path[pathIndex].get(enemyIndex).getProg()) {
            return;
        }
        int newIndex = enemyIndex + 1;
        while ((newIndex <= listSize-1) & (enemyPos > path[pathIndex].get(newIndex-1).getProg())) {
            newIndex++;
        }
        
        path[pathIndex].add(newIndex,path[pathIndex].get(enemyIndex));
        path[pathIndex].remove(enemyIndex);
    }
    
    /**
     * Merges all path segments into enemyList.
     */
    private void updateEnemyList() {
        enemyList = path[0];
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
        
        for (int currPathSegment = 0; currPathSegment <= NUM_PATH_SEGMENTS - 1; currPathSegment++) {
            numEnemies = path[currPathSegment].size();
            for (int currEnemyNum = 0; currEnemyNum <= numEnemies--; currEnemyNum++) {
                currEnemy = path[currPathSegment].get(currEnemyNum);
                if (currEnemy.getIsDead()) {
                    path[currPathSegment].remove(currEnemyNum);
                    enemyTypeLists[currEnemy.getID()].remove(currEnemy);
                } else {
                    currEnemy.act();
                    newPathSegment = (int)Math.floor(currEnemy.getProg()*NUM_PATH_SEGMENTS);
                    if (newPathSegment >= NUM_PATH_SEGMENTS) {
                        path[currPathSegment].remove(currEnemyNum);
                        numFirewallHits++;
                    } else if (currPathSegment != newPathSegment) {
                        path[currPathSegment].remove(currEnemyNum);
                        path[newPathSegment].addFirst(currEnemy);
                        pathSortForwardEnemy(newPathSegment,0);
                    } else {
                        pathSortForwardEnemy(currPathSegment,currEnemyNum);
                    }
                }
            }
        }
        updateEnemyList();
        return numFirewallHits;
    }
    
    /**
     * Returns the list of all enemies sorted by position along the path.
     * @return 
     */
    public LinkedList<EnemyInterface> getPath() {
        final LinkedList<EnemyInterface> outList = new LinkedList();
        int numEnemies = enemyList.size();
        for (int i = 0;i<=numEnemies-1;i++) {
            outList.add(enemyList.get(i));
        }
        return outList;
    }
    
    /**
     * Returns a list of every enemy of a given enemy type, sorted by position along the path.
     * @param id
     * @return 
     */
    public LinkedList<EnemyInterface> getEnemyTypeList(int id) {
        final LinkedList<EnemyInterface> outList = new LinkedList();
        int numEnemies = enemyTypeLists[id].size();
        for (int i = 0;i<=numEnemies-1;i++) {
            outList.add(enemyTypeLists[id].get(i));
        }
        return outList;
    }
    
    /**
     * Returns a list of every enemy on a given path segment, sorted by position along the path.
     * @param index
     * @return 
     */
    public LinkedList<EnemyInterface> getPathSegment(int index) {
        final LinkedList<EnemyInterface> outList = new LinkedList();
        int numEnemies = path[index].size();
        for (int i = 0;i<=numEnemies-1;i++) {
            outList.add(path[index].get(i));
        }
        return outList;
    }
}
