package com.studentsteam.sonic.threads;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import org.apache.log4j.Logger;

public class PropertiesThread implements Runnable {
    //Logs
    private final Logger log = Logger.getLogger(this.getClass());

    public Thread thrd; //Link thread
    private int mapWidth;
    private int mapHeight;
    private TiledMap map;
    public PropertiesThread(String name, TiledMap map){
        this.map = map;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        thrd = new Thread(this,name);
    }
    public void start(){
        thrd.start(); //Launch thread
    }
    public int getMapWidth(){
        return mapWidth;
    }
    public int getMapHeight(){
        return mapHeight;
    }
    @Override
    public void run() {
        log.debug("Запуск потока");
        MapProperties prop = map.getProperties();
        //Width and height of map
        int width = prop.get("width", Integer.class);
        int height = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        mapWidth = width * tilePixelWidth;
        mapHeight = height * tilePixelHeight;
        log.debug("Завершение потока");
    }
}