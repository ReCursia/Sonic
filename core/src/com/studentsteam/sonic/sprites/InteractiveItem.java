package com.studentsteam.sonic.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.studentsteam.sonic.screens.PlayScreen;

abstract public class InteractiveItem extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;


    public InteractiveItem(PlayScreen screen, float x,float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineItem();
    }

    abstract public void update(float dt);

    /**
     * Defines interactive item
     */
    protected abstract void defineItem();
}
