package com.studentsteam.sonic.screens.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundImage extends Actor {
    private Texture background;
    public BackgroundImage(String path){
        this.background = new Texture(path);
    }
    public void dispose(){
        background.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,0,0,getStage().getCamera().viewportWidth,getStage().getCamera().viewportHeight);
    }
}