package com.studentsteam.sonic.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.screens.PlayScreen;

public class Eggman extends Enemy
{
    private float stateTime;
    private Animation runAnimation;
    private Animation deadAnimation;
    private boolean setToDestroy;
    private boolean destroyed;
    private Sound desSound;

    //Eggman's textures
    Texture egm_stand = new Texture("play/Eggman_stand.png");
    Texture egm_dead = new Texture("play/Eggman_dead.png");
    Texture egm_run = new Texture("play/Eggman_run.png");


    public Eggman(PlayScreen screen, float x, float y)
    {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=0;i<3;i++)
            frames.add(new TextureRegion(egm_run, i * 59,0, 59,55));
        runAnimation = new Animation(0.1f,frames);
        frames.clear();
        frames = new Array<TextureRegion>();
        for(int i=0;i<2;i++)
            frames.add(new TextureRegion(egm_dead, i * 32,0, 32,52));
        deadAnimation = new Animation(0.1f,frames);
        frames.clear();
        setBounds(getX(),getY(),59,55);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt)
    {
        stateTime += dt;
        if(setToDestroy)
        {
            if(!destroyed){
                world.destroyBody(b2body);
                destroyed = true;
            }
            setRegion((TextureRegion) deadAnimation.getKeyFrame(stateTime,true));
            setBounds(getX(),getY(),32,52);
        }
        else
        {
            b2body.setLinearVelocity(velocity);
            //fix +10 Y position
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2+10);
            setRegion((TextureRegion) runAnimation.getKeyFrame(stateTime,true));
        }

    }

    /**
     * Defines eggman in our Box2D world
     */
    @Override
    protected void defineEnemy(float x,float y)
    {
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

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-15, 21).scl(1);
        vertice[1] = new Vector2(15, 21).scl(1);
        vertice[2] = new Vector2(-6, 6).scl(1);
        vertice[3] = new Vector2(6, 6).scl(1);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Main.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }


    public void draw(Batch batch)
    {
        if(!destroyed || stateTime < 3.0f)
        {
            super.draw(batch);
        }
    }

    /**
     * Collision reaction
     */
    @Override
    public void hitOnHead()
    {
        setToDestroy = true;
        stateTime = 0;
        this.desSound = Gdx.audio.newSound(Gdx.files.internal("play/Eggman_reply.mp3"));
        desSound.play();
    }
}
