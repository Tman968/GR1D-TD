/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The abstract version of the standard enemy class, including default behavior.
 * @author natha
 */
public abstract class EnemyAbstract implements EnemyInterface,EnemyCracked {
    protected final int ID;
    protected final float MAX_HP;
    protected final float BASE_SPEED;
    protected float prog;
    protected float progUpdate;
    protected float hp;
    protected boolean isDead;
    
    protected Vector2 velocity;
    protected float maxVelocity;
    protected float scale;
    protected float touchDetectTimer;
    
    protected Vector2 location;
    protected Vector2 currentTile;
    
    protected final FitViewport viewport;
    protected final float worldWidth;
    protected final float worldHeight;
    
    float animTime;
    boolean isInAnim;
    
    //Default Textures and Sprites
    protected Texture enemyTexture;
    protected Texture enemyDamageTexture;
    protected final Sprite enemySprite;
    
    EnemyCommander enemyCommander;
    
    //Enemy Hitbox
    protected Rectangle enemyRectangle;
    
    /**
     * Constructor for EnemyAbstract.
     * Defines the constants ID, MAX_HP, and BASE_SPEED with its inputs, and places the enemy at the start of the track.
     * @param gameViewport
     * @param id
     * @param maxHP
     * @param baseSpeed 
     */
    public EnemyAbstract(FitViewport gameViewport, int id, float maxHP, float baseSpeed) {
        viewport = gameViewport;
        ID = id;
        MAX_HP = maxHP;
        BASE_SPEED = baseSpeed;
        prog = 0f;
        hp = MAX_HP;
        isDead = false;
        
        //Setting viewport variables
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();
        
        // Setting up vectors
        location = new Vector2();
        velocity = new Vector2();
        velocity.x = BASE_SPEED;
        velocity.y = 0f;
        scale = worldHeight/8;
        location.x = 0f;
        location.y = (worldHeight * 3.5f/8);
        progUpdate = BASE_SPEED;
        
        //Setting textures and sprites
        enemyTexture = new Texture("packet.png");
        enemyDamageTexture = new Texture("packetDamage.png");
        enemySprite = new Sprite(enemyTexture);
        enemySprite.setSize(48f, 48f);
        enemySprite.setX((location.x)-enemySprite.getWidth()/2);
        enemySprite.setY((location.y)-enemySprite.getHeight()/2);
        enemyRectangle = new Rectangle();
        
        enemyCommander = new EnemyCommander();
        
        //Setting animation vars
        animTime = 0f;
        isInAnim = false;
    }
    
    protected void updateMovement() {
        float delta = Gdx.graphics.getDeltaTime();
        prog += delta*progUpdate;
        enemySprite.setCenterX(scale * enemyCommander.progToPos(prog).x);
        enemySprite.setCenterY(scale * enemyCommander.progToPos(prog).y);
        // keeps the enemy hitbox (Rectangle) centered along the path
        touchDetectTimer += delta;
        //System.out.println("Distance travelled: " + prog);
        //System.out.println("delta = "+delta+"      Pos(x,y) = ("+enemySprite.getX()+","+enemySprite.getY()+")      Velocity (x,y) = ("+velocity.x+","+velocity.y+")");
    }
    
    
    
    
    
    /**
     * Default act() behavior.
     * If an enemy uses this method for act(), it simply progress by it's base speed.
     */
    @Override
    public void act() {updateMovement();updateDamageAnimation();};
    
    /**
     * 
     * @param waypointStart
     * @param waypointEnd
     */
    @Override
    public void changeVelocity(Rectangle waypointStart, Rectangle waypointEnd) {
        float speedVel;
        //Setting up vectors for changing the enemy velocity later
        Vector2 startLocation = new Vector2();
        Vector2 endLocation = new Vector2();
        startLocation.x = (waypointStart.getX() - waypointStart.getWidth()/2);
        startLocation.y = (waypointStart.getY() - waypointStart.getHeight()/2);
        endLocation.x = (waypointEnd.getX() - waypointEnd.getWidth()/2);
        endLocation.y = (waypointEnd.getY() - waypointEnd.getHeight()/2);
        
        // Setting speed to velocity
        if (velocity.x != 0) {
            speedVel = velocity.x;
        }
        else {
            speedVel = velocity.y;
        }
        
        if (startLocation.x == endLocation.x) {
            // changing velocity to move along the y-axis
            // Checking if endpoint is above/below the startpoint
            velocity.x = 0f;
            if (startLocation.y < endLocation.y) {
                velocity.y = speedVel;
            }
            else {
                velocity.y = -speedVel;
            }
        }
        else {
            // changing velocity to move along the x-axis
            if (speedVel < 0) {
                speedVel = -speedVel;
            }
            velocity.x = speedVel;
            velocity.y = 0f;
        }
        
        //System.out.println("Velocity x = " + velocity.x + "     Velocity y = " + velocity.y);
    }
    
    @Override
    public final void resetTouchDetect() {touchDetectTimer = 0f;}
    
    /**
     * Default damage behavior.
     * If an enemy uses this method for damage(), the given damage will simply be subtracted from it's hit points.
     * Further, if the enemy is reduced to 0 health points or less, it will be marked as dead.
     * @param damage 
     */
    @Override
    public void damage(float damage) {
        hp -= damage;
        if (hp<=0) {
            isDead = true;
        }
        activateDamageAnimation();
    }
    
    // Activate Damage Animation
    protected void activateDamageAnimation() {
        enemySprite.setTexture(enemyDamageTexture);
        isInAnim = true;
        
    }
    
    // Update Damage Animation
    protected void updateDamageAnimation() {
        if (animTime > 0.2f) {
            enemySprite.setTexture(enemyTexture);
            isInAnim = false;
            animTime = 0;
        }
        else {
            animTime += Gdx.graphics.getDeltaTime();
        }
    }
    
    /**
     * Returns the ID of the enemy.
     * Each enemy type is assigned an ID.
     * @return 
     */
    @Override
    public final int getID() {return ID;}
    
    /**
     * Returns the maximum health points of the enemy.
     * @return 
     */
    @Override
    public final float getMaxHP() {return MAX_HP;}
    
    /**
     * Returns a percentage representing the distance along the whole path.
     * @return 
     */
    @Override
    public final float getProg() {return prog;}
    
    /**
     * Returns the enemy's current health points.
     * @return 
     */
    @Override
    public final float getHP() {return hp;}
    
    /**
     * Returns true if the enemy is dead.
     * @return 
     */
    @Override
    public final boolean getIsDead() {return isDead;}
    
    @Override
    public final Sprite getEnemySprite() {return enemySprite;}
    
    @Override
    public final Rectangle getHitbox() {return enemyRectangle;}
    
    @Override
    public final boolean getIsInAnim() {return isInAnim;}
    
    @Override
    public final float getTouchDetect() {return touchDetectTimer;}
    
    
}
