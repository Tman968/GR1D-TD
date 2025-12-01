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
public class EnemyCommander {
    public final static int[][] pathData = {{4,0},
                                            {4,1},{4,2},{4,3},{4,4},
                                            {5,4},{6,4},
                                            {6,5},{6,6},
                                            {5,6},{4,6},{3,6},{2,6},
                                            {2,7},{2,8},
                                            {2,9}};
    public final static int NUM_PATH_SEG = pathData.length-2;
    
    
    public Vector2 progToPos(float prog) {
        if (prog <= 0f) {
            return new Vector2((pathData[0][1]+pathData[1][1])/2,
                               (pathData[0][0]+pathData[1][0])/2);
        } else if (prog >= (float)NUM_PATH_SEG) {
            return new Vector2((pathData[NUM_PATH_SEG][1]+pathData[NUM_PATH_SEG+1][1])/2,
                               (pathData[NUM_PATH_SEG][0]+pathData[NUM_PATH_SEG+1][0])/2);
        }
        int pathSeg = (int)Math.floor(prog+0.5f);
        float partialProg = (prog+0.5f) % 1f;
        return new Vector2(0.5f+(1f-partialProg)*(float)pathData[pathSeg][1]+partialProg*(float)pathData[pathSeg+1][1],
                           0.5f+(1f-partialProg)*(float)pathData[pathSeg][0]+partialProg*(float)pathData[pathSeg+1][0]);
    }
}