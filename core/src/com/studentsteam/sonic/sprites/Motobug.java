package com.studentsteam.sonic.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.screens.PlayScreen;

public class Motobug extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> runAnimation;
    private boolean setToDestroy;
    private boolean destroyed;

    public Motobug(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        Texture motobug = new Texture("play/motobug.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=0;i<3;i++) {
            frames.add(new TextureRegion(motobug, i * 44, 0, 44, 29));
        }
        runAnimation = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
        setBounds(getX(),getY(),44,29);
        setToDestroy = false;
        destroyed = false;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        //fix Y +7
        TextureRegion region;
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y -getHeight()/2);
        region = runAnimation.getKeyFrame(stateTime,true);
        setRegion(runAnimation.getKeyFrame(stateTime,true));
        b2body.setLinearVelocity(velocity);
        if(stateTime >= 4){
            reverseVelocity(true,false);
            stateTime = 0;
        }
        if((b2body.getLinearVelocity().x < 0) && region.isFlipX()){
            region.flip(true,false);;
        }
        else if((b2body.getLinearVelocity().x > 0) && !region.isFlipX()){
            region.flip(true,false);
        }
        setRegion(region);
    }
    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15);
        fdef.filter.categoryBits = Main.ENEMY_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT | Main.ENEMY_BIT | Main.OBJECT_BIT | Main.SONIC_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setFixedRotation(true);
    }

    @Override
    public void hitOnHead() {

    }
}
