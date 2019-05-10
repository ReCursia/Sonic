package com.studentsteam.sonic.screens.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.studentsteam.sonic.tools.database.DataItem;

public class SaveSlot extends Actor {
    //Timer
    private float timer;
    //Is selected
    private boolean isSelected;
    //Data
    private DataItem data;
    //Textures
    private Texture bars;
    private Texture slot;
    public SaveSlot(){
        //Timer
        timer = 0;
        //IsSelected
        this.isSelected = false;
        //Textures
        this.bars = new Texture("save/redbars.png");
        this.slot = new Texture("save/slot.png");
        //Set size to size of texture
        setSize(slot.getWidth(),slot.getHeight());
    }

    /**
     * Assigment data to save slot
     * @param data
     */
    public void setData(DataItem data){
        this.data = data;
    }

    /**
     * Outputting data of save slot
     */
    public void printData(){
        try {
            System.out.println(data.toString());
        }
        catch (NullPointerException err){
            System.out.println("NULL");
        }
    }

    /**
     * Sets chosen slot to "isSelected" state to make it red when true
     * @param value
     */
    public void setSelected(boolean value){
        isSelected = value;
    }
    //Update for actors
    @Override
    public void act(float dt){
        timer +=dt;
        if(timer >= 0.5f){
            timer = 0;
        }

    }

    /**
     * Message about save slot
     * @return
     */
    private CharSequence getMessage(){
        try {
            return data.rings+"";
        }catch (NullPointerException err){
            return "NEW";
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = new BitmapFont(); //To print message
        font.setColor(Color.BLACK);
        batch.draw(slot,getX(),getY(),getWidth(),getHeight());
        if(isSelected && (timer >= 0.25f)){
            batch.draw(bars,getX()-5,getY()-5,getWidth()+5,getHeight()+10);
        }
        font.draw(batch,getMessage(),getX()+getWidth()*3/10,getY()+getHeight()*5/6);
    }
}
