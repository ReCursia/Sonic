package com.studentsteam.sonic.screens.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScrollingBackground extends Actor {

    private static final int DEFAULT_SPEED = 80;

    private Texture image;
    private float x1, x2;
    private int speed;
    private float imageScale;
    private float timer;
    private float delay;

    public ScrollingBackground(String path,float delay)
    {
        this.image = new Texture(path);
        this.delay = delay;
        x1 = 0;
        x2 = image.getWidth();
        imageScale = 1;
        speed = DEFAULT_SPEED;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image,x1,0,getStage().getCamera().viewportWidth ,getStage().getCamera().viewportHeight);
        batch.draw(image,x2,0,getStage().getCamera().viewportWidth,getStage().getCamera().viewportHeight);
    }
    //Update for actors
    @Override
    public void act(float dt) {
        //Picture is starting to move after several seconds (delay)
        if(timer < delay){
            timer += dt;
        }
        else {
            x1 -= speed * dt;
            x2 -= speed * dt;
        }

        if(x1 + image.getWidth()*imageScale <= 0){
            x1 = x2 + image.getWidth()*imageScale;
        }
        if(x2+image.getWidth()*imageScale <= 0){
            x2 = x1 + image.getWidth()*imageScale;
        }
    }
}
