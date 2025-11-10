/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author natha
 */
public class EntityHandler {
    public static final Vector2 PATH_START_POS = new Vector2(4.5f,0f);
    public static final Vector2 PATH_END_POS = new Vector2(6.5f,8f);
    public static final int[][] pathData = {{4,-1},
                                            {4,0},{4,1},{4,2},{4,3},
                                            {3,3},{2,3},
                                            {2,4},{2,5},
                                            {3,5},{4,5},{5,5},{6,5},
                                            {6,6},{6,7},
                                            {6,8}};
    public static final float[][] pathOnCoord = {{0f,4.5f,0f},{3.5f,0f}};
    
    public Vector2 progToPos(float prog) {
        Vector2 pos = new Vector2();
        float adjProg = prog + 0.5f;
        int adjPathSeg = (int)Math.floor(adjProg);
        float subProg = adjProg % 1;
        
        pos.x = (1f - subProg) * (float)pathData[adjPathSeg][1] + subProg*(float)pathData[adjPathSeg+1][1];
        pos.y = (1f - subProg) * (float)pathData[adjPathSeg][1] + subProg*(float)pathData[adjPathSeg+1][1];
        return pos;
    }
    
    public void drawAtPos(String sprite) {
        
    }
    
}
