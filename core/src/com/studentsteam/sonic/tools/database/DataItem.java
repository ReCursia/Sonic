package com.studentsteam.sonic.tools.database;

/**
 * This class contains information about save file
 */
public class DataItem {
    public float x; //X position
    public float y; //Y position
    public int rings; //Amount of rings
    public int index; //Save index

    public DataItem(float x, float y, int rings, int index){
        this.x = x;
        this.y = y;
        this.rings = rings;
        this.index = index;
    }
    @Override
    public String toString(){
        return "x = "+x+" y = "+y+" rings = "+rings+" index = "+index;
    }
}