package com.studentsteam.sonic.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.screens.PlayScreen;
import com.studentsteam.sonic.sprites.Eggman;
import com.studentsteam.sonic.sprites.Enemy;
import com.studentsteam.sonic.sprites.Motobug;
import com.studentsteam.sonic.sprites.Ring;

/**
 * Delegated class for creating box2d objects
 */
public class B2WorldCreator
{
    private Array<Enemy> enemies; //Enemies
    private Array<Ring> rings; //Rings
    public B2WorldCreator(PlayScreen screen)
    {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //BodyDef bdef = new BodyDef();
        //PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        //Body body;

        //Getting objects of layer
        MapObjects objects = map.getLayers().get(5).getObjects();
        //Creating ground
        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object);
            } else {
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bd);

            fdef.shape = shape;
            fdef.filter.categoryBits = Main.OBJECT_BIT;

            body.createFixture(shape, 1);

            shape.dispose();
        }
        //Creating rings
        rings = new Array<Ring>();
        objects = map.getLayers().get(2).getObjects();
        for(MapObject object: objects){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            rings.add(new Ring(screen,rect.getX(),rect.getY()));
        }
        //Create eggman,enemies
        enemies = new Array<Enemy>();
        objects = map.getLayers().get(3).getObjects();
        for(MapObject object: objects){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            enemies.add(new Eggman(screen,rect.getX(),rect.getY()));
        }
        //Create enemies
        objects = map.getLayers().get(4).getObjects();
        for(MapObject object: objects){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            enemies.add(new Motobug(screen,rect.getX(),rect.getY()));
        }


    }
    public Array<Enemy> getEnemies(){return enemies;}
    public Array<Ring> getRings(){
        return rings;
    }
    //Rectangle shape
    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f),
                (rectangle.y + rectangle.height * 0.5f ));
        polygon.setAsBox(rectangle.width * 0.5f,
                rectangle.height * 0.5f,
                size,
                0.0f);
        return polygon;
    }
    //Polygons shape
    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i];
        }

        polygon.set(worldVertices);
        return polygon;
    }
}

