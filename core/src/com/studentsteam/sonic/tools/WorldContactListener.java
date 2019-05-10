package com.studentsteam.sonic.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.scenes.SonicHUD;
import com.studentsteam.sonic.sprites.Enemy;
import com.studentsteam.sonic.sprites.Ring;
import com.studentsteam.sonic.sprites.Sonic;

/**
 * Class for processing collisions of objects
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //Gdx.app.log("Start contact",contact.toString());
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() instanceof Ring && fixB.getUserData() instanceof Sonic)
        {
            Fixture ring = fixA;
            //Fixture sonic = fixB;
            ((Ring) ring.getUserData()).destroy();
            SonicHUD.addRings(1);
        }
        //TODO: Сделать switch-case для коллизий!
        if(fixA.getFilterData().categoryBits == Main.ENEMY_HEAD_BIT)
           ((Enemy)fixA.getUserData()).hitOnHead();
        if(fixA.getFilterData().categoryBits == Main.OBJECT_BIT)
            ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
