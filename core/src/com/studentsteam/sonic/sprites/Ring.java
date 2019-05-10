package com.studentsteam.sonic.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.studentsteam.sonic.screens.PlayScreen;

public class Ring extends InteractiveItem {
    //Sound
    private Sound ringSound;
    //Timer
    private float stateTimer;
    //Animations
    private Animation<TextureRegion> ringIsNotActive;
    private Animation<TextureRegion> ringIsActive;
    private boolean isActive;
    private boolean destroyed;
    public Ring(PlayScreen screen, float x, float y){
        super(screen, x, y);
        stateTimer = 0;
        isActive = true;
        destroyed = false;
        Texture activeRing = new Texture("items/ring_active.png");
        Texture notActiveRing = new Texture("items/ring_notactive.png");
        setBounds(getX(),getY(),16,16);
        //Sound
        ringSound = Gdx.audio.newSound(Gdx.files.internal("items/ring.mp3"));
        //Setting animations
        //ACTIVE
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4;i++){
            frames.add(new TextureRegion(activeRing,16*i,0,16,16));
        }
        ringIsActive = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
        //NOT ACTIVE
        for(int i = 0; i < 4;i++){
            frames.add(new TextureRegion(notActiveRing,16*i,0,16,16));
        }
        ringIsNotActive = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
    }

    public void draw(Batch batch){
        if(isActive || stateTimer <= 1f){
            super.draw(batch);
        }
    }

    /**
     * To destroy ring when it iTo destroy ring when it is collecteds collected
     */
    public void destroy(){
        stateTimer = 0;
        ringSound.play(0.1f);
        isActive = false;
    }
    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
        if(!isActive && stateTimer > 0.1f && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    /**
     * According to state we are getting each frame of current state ( jumping,running etc)
     * @param dt
     * @return frame of animation
     */
    private TextureRegion getFrame(float dt){
        stateTimer +=dt;
        return isActive ? ringIsActive.getKeyFrame(stateTimer,true) : ringIsNotActive.getKeyFrame(stateTimer,true);
    }
    @Override
    protected void defineItem(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8);
        //fdef.filter.categoryBits = Main.ENEMY_BIT;
        //fdef.filter.maskBits = Main.GROUND_BIT | Main.ENEMY_BIT | Main.OBJECT_BIT;
        fdef.shape = shape;
        //Is sensor (no physical collisions)
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setFixedRotation(true);
    }
}
