package com.studentsteam.sonic.exceptions;

public class OutOfWorldCamPositionX extends Exception {
    private float x;
    public OutOfWorldCamPositionX(float x){
        this.x = x;
    }
    @Override
    public String getMessage(){
        return "Sonic position X ( "+x+" ) is out of the screen.";
    }
}
