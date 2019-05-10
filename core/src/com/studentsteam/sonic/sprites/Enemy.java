package com.studentsteam.sonic.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.studentsteam.sonic.screens.PlayScreen;

public abstract class Enemy extends Sprite
{
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;


    public Enemy(PlayScreen screen, float x,float y)
    {
        this.world = screen.getWorld();
        this.screen = screen;
        defineEnemy(x,y);
        velocity = new Vector2(500,0);
    }

    /**
     * Updates our enemy every frame
     * @param dt
     */
    public abstract void update(float dt);

    /**
     * Defines enemy in our Box2D world
     */
    protected abstract void defineEnemy(float x,float y);

    /**
     * Enemy collision reaction
     */
    public abstract void hitOnHead();

    /**
     * Reverses X and Y velocity
     * @param x axis
     * @param y axis
     */
    public void reverseVelocity(boolean x, boolean y)
    {
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
