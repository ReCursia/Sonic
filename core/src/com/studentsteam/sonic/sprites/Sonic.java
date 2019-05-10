package com.studentsteam.sonic.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.screens.PlayScreen;
import com.studentsteam.sonic.tools.database.DataItem;
import com.studentsteam.sonic.tools.Lerp;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

public class Sonic extends Sprite {
    //States
    public enum State {STANDING,WALKING,RUNNING,JUMPING,SQUATTING,SPINNING,DASHING}
    private State previousState;
    private State currentState;
    private World world; //For abstract
    private Body b2body; //For abstract
    private TextureRegion sonicStand;
    private Animation<TextureRegion> sonicWalk;
    private Animation<TextureRegion> sonicRun;
    private Animation<TextureRegion> sonicJump;
    private Animation<TextureRegion> sonicSquat;
    private Animation<TextureRegion> sonicSpin;
    private Animation<TextureRegion> sonicDash;
    private float stateTimer;
    private boolean isRunningRight;
    private boolean isJumping;
    float X;
    float Y;
    public Sonic(PlayScreen screen){
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;

        Texture sprites = new Texture("play/sonicSprites.png");
        this.sonicStand = new TextureRegion(sprites,8,17,40,40);
        setBounds(getX(),getY(),40,40);
        setRegion(sonicStand);
        //Setting animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //WALKING
        for(int i = 0; i < 8;i++){
            frames.add(new TextureRegion(sprites,8+44*i,66,41,40));
        }
        sonicWalk = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
        //JUMPING
        for(int i = 0; i < 5;i++){
            frames.add(new TextureRegion(sprites,8+40*i,330,40,40));
        }
        sonicJump = new Animation<TextureRegion>(0.1f,frames);
        sonicDash = sonicJump; //Because same animations
        frames.clear();
        //SQUATTING
        for(int i = 0; i < 2;i++){
            frames.add(new TextureRegion(sprites,126+35*i,17,34,40));
        }
        sonicSquat = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
        //RUNNING
        for(int i = 0; i < 4;i++){
            frames.add(new TextureRegion(sprites,358+40*i,66,41,40));
        }
        sonicRun = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
        //SPINNING
        for(int i = 0; i < 6;i++){
            frames.add(new TextureRegion(sprites,205+40*i,338,38,34));
        }
        sonicSpin = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();


        //Save information
        DataItem item = screen.getData();
        defineSonic(item.x,item.y);
    }

    public float getPositionX(){
        return b2body.getPosition().x;
    }
    public float getPositionY(){
        return b2body.getPosition().y;
    }

    public void update(float dt){
        handleInput(dt);
        //fix Y +7
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y -getHeight()/2+7);
        setRegion(getFrame(dt));
    }

    /**
     * According to state we are getting each frame of current state ( jumping,running etc)
     * @param dt
     * @return frame of animation
     */
    private TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case WALKING:
                sonicWalk.setFrameDuration(Lerp.interpolate(0.2f,0.1f,abs(b2body.getLinearVelocity().x)/180f));
                region = sonicWalk.getKeyFrame(stateTimer,true);
                break;
            case JUMPING:
                region = sonicJump.getKeyFrame(stateTimer,true);
                break;
            case SQUATTING:
                region = sonicSquat.getKeyFrame(stateTimer,false);
                break;
            case SPINNING:
                region = sonicSpin.getKeyFrame(stateTimer,true);
                break;
            case DASHING:
                region = sonicDash.getKeyFrame(stateTimer,true);
                break;
            case RUNNING:
                region = sonicRun.getKeyFrame(stateTimer,true);
                break;
            default:
                region = sonicStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()){
            region.flip(true,false);
            isRunningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()){
            region.flip(true,false);
            isRunningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer+dt : 0;
        previousState = currentState;
        return region;
    }

    public void handleInput(float dt){
        float acc = 10000*0.046875f;
        float dec = 10000*0.5f;
        float frc = acc; // == acc
        float top = 1000000000*6f;
        float grv = 10000*0.21875f;
        if(Gdx.input.isKeyPressed(Input.Keys.C)){
            X += 50000;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(!isJumping)
            {
                isJumping = true;
                Y += 100000;
            }
        }
        else
            {
            if(Y <= -100000)
            {
                isJumping = false;
                Y = 0;
            }
            else if(isJumping && Y != 0)
            {
                Y -= grv;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if (X >= 0) {
                X -= dec;
            } else if (X > -top) {
                X -= acc;
            }

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(X < 0){
                X += dec;
            }
            else if(X < top){
                X += acc;
            }
        } else {
            X -= min(abs(X), frc) * signum(X);
        }
        b2body.setLinearVelocity(X,Y);
        //b2body.applyForceToCenter(X,Y,true);
        //b2body.applyLinearImpulse(X,Y,b2body.getPosition().x,b2body.getPosition().y,true);
        //b2body.setTransform(new Vector2(b2body.getPosition().x+X,b2body.getPosition().y+Y),0);
        //Gdx.app.log("Velocity X and Y",b2body.getLinearVelocity().x+" "+b2body.getLinearVelocity().y);
        //Gdx.app.log("Current state:",currentState+"");
    }

    /**
     * Getting current state of player
     * @return state
     */
    private State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if(abs(X) > 50f){
            return State.RUNNING;
        }
        else if(X != 0){
            return State.WALKING;
        }
        else{
            return State.STANDING;
        }
    }

    /**
     * Defines sonic in the Box2d world
     * @param x axis
     * @param y axis
     */
    private void defineSonic(float x,float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12);
        fdef.filter.categoryBits = Main.GROUND_BIT | Main.ENEMY_HEAD_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setFixedRotation(true);
        X = 0;
        Y = 0;
    }
}
