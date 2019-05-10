package com.studentsteam.sonic.threads;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.scenes.SonicHUD;
import com.studentsteam.sonic.sprites.Sonic;
import com.studentsteam.sonic.tools.database.DatabaseSaves;
import com.studentsteam.sonic.tools.database.DataItem;
import org.apache.log4j.Logger;


public class SaveThread implements Runnable {
    //Logs
    private final Logger log = Logger.getLogger(this.getClass());

    public Thread thrd; //Link thread
    //Соник и hud
    private Sonic sonic;
    private SonicHUD hud;
    //Звуки
    private Sound saveSound;
    //Saves
    private Array<DataItem> fileItems;

    public SaveThread(String name, Sonic sonic, SonicHUD hud, Array<DataItem> fileItems, Sound saveSound){
        this.fileItems = fileItems;
        this.sonic = sonic;
        this.hud = hud;
        this.saveSound = saveSound;
        thrd = new Thread(this,name);
        //Приоритеты
        thrd.setPriority(Thread.MIN_PRIORITY);
    }
    private int indexOfSave(){
        for(DataItem item: fileItems){
            if(item.index == Main.SAVE_INDEX) return item.index;
        }
        return -1;
    }
    public void start(){
        thrd.start(); //Launch thread
    }
    @Override
    public void run() {
        log.debug("Запуск потока");
        log.info("Сохранение игры");
        //Получаем индекс сохранения
        int index = indexOfSave();
        if(index != -1){
            fileItems.set(index,new DataItem(sonic.getPositionX(),sonic.getPositionY(),hud.getRings(),Main.SAVE_INDEX));
        }
        else{
            //Game is new, adding new element to array and save data to file
            fileItems.add(new DataItem(sonic.getPositionX(),sonic.getPositionY(),hud.getRings(),Main.SAVE_INDEX));
        }
        DatabaseSaves.writeData(fileItems);
        //fileSaves.writeData(fileItems);
        log.debug("Завершение потока");
        //Проигрываем звук
        saveSound.play(0.3f);
    }
}
