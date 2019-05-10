package com.studentsteam.sonic.screens.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureActor extends Actor {
    private Texture image;

    public TextureActor(String path) {
        this.image = new Texture(path);
        //Setting size
        setSize(image.getWidth(),image.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image,getX(),getY(),getWidth(),getHeight());
    }
}
