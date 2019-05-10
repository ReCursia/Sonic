package com.studentsteam.sonic.screens.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class SonicLogo extends Actor {
    //Timer
    private float timer;
    //Animation and textures
    private Animation<TextureRegion> sonicAnimation;
    private Animation<TextureRegion> sonicLoop;
    private Texture logo;
    public SonicLogo(){
        //Logo
        this.logo = new Texture("start/logo.png");
        //Animations
        Texture sonic = new Texture("start/sonic.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //Sonic animation
        for(int i = 0; i < 6;i++){
            frames.add(new TextureRegion(sonic,i*80,0,80,75));
        }
        this.sonicAnimation = new Animation<TextureRegion>(0.10f,frames);
        frames.clear();
        //Sonic loop
        for(int i = 6; i < 8;i++){
            frames.add(new TextureRegion(sonic,i*80,0,80,75));
        }
        this.sonicLoop = new Animation<TextureRegion>(0.35f,frames);
        frames.clear();
        //Set size to size of texture
        setSize(logo.getWidth(),logo.getHeight());
    }
    //Update for actors
    @Override
    public void act(float dt){
        timer += dt;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Logo
        batch.draw(logo, getStage().getCamera().viewportWidth/2-getWidth()/2,getStage().getCamera().viewportHeight/2-getHeight()/2);
        if(sonicAnimation.isAnimationFinished(timer)){
            batch.draw(sonicLoop.getKeyFrame(timer ,true),getStage().getCamera().viewportWidth/2-80/2,getStage().getCamera().viewportHeight/2-75/2+35);
        }
        else{
            batch.draw(sonicAnimation.getKeyFrame(timer),getStage().getCamera().viewportWidth/2-80/2,getStage().getCamera().viewportHeight/2-75/2+35);
        }
    }
}
