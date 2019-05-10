package com.studentsteam.sonic.exceptions;

public class OutOfWorldCamPositionY extends Exception {
    private float y;
    public OutOfWorldCamPositionY(float y){
        this.y = y;
    }
    @Override
    public String getMessage(){
        return "Sonic position Y ( "+y+" ) is out of the screen.";
    }
}