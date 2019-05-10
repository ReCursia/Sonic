package com.studentsteam.sonic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.screens.assets.BackgroundImage;
import com.studentsteam.sonic.screens.assets.SaveSlot;
import com.studentsteam.sonic.threads.ReportThread;
import com.studentsteam.sonic.tools.database.DatabaseSaves;
import com.studentsteam.sonic.tools.database.DataItem;
import org.apache.log4j.Logger;

public class SaveScreen implements Screen {
    //Logs
    private final Logger log = Logger.getLogger(this.getClass());

    private Main game;
    private BackgroundImage background;
    //Music
    private Music music;
    //Audio
    private Sound switchSound;
    private Sound switchError;
    private Sound switchPress;
    //Reports
    private Sound saveSound;
    //Save slots
    private Array<SaveSlot> slots;
    //Даннные о сохранении
    private Array<DataItem> items;
    private int currentPos;

    //Stage and viewport
    private Stage stage;
    private Viewport viewport;

    public SaveScreen(Main game){
        log.info("Запуск экрана сохранения");
        this.game = game;
        this.background = new BackgroundImage("save/background.png");
        //Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("save/background.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        //Audio
        this.switchSound = Gdx.audio.newSound(Gdx.files.internal("save/S3K_4A.wav"));
        this.switchPress = Gdx.audio.newSound(Gdx.files.internal("save/S3K_9F.wav"));
        this.switchError = Gdx.audio.newSound(Gdx.files.internal("save/S3K_40.wav"));
        this.saveSound = Gdx.audio.newSound(Gdx.files.internal("save/save.mp3"));
        //Setting stage and viewport
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        //Getting save items from database
        items = DatabaseSaves.getData();
        //Creating slots
        currentPos = 0;
        slots = new Array<SaveSlot>();
        for(int i = 0; i < 4;i++){
            SaveSlot slot = new SaveSlot();
            slots.add(slot);
            for(DataItem item: items){
                if(item.index == i) slot.setData(item);
            }
            //Set position of slot on screen
            slot.setPosition(50+i*150,180);
        }
        //The first slot is selected
        slots.get(currentPos).setSelected(true);
        //Adding actors
        stage.addActor(background);
        for(SaveSlot slot:slots){
            stage.addActor(slot);
        }
    }
    @Override
    public void show() {

    }

    /**
     * Switch current position to the next slot
     */
    private void selectNextSlot(){
        log.info("Выбор следующего слота");
        slots.get(currentPos+1).setSelected(true);
        slots.get(currentPos).setSelected(false);
        currentPos += 1;
    }
    /**
     * Switch current position to the previous slot
     */
    private void selectPreviousSlot(){
        log.info("Выбор предыдущего слота");
        slots.get(currentPos-1).setSelected(true);
        slots.get(currentPos).setSelected(false);
        currentPos -= 1;
    }

    private void handleInput(float dt){
        //Slot is chosen
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            Main.SAVE_INDEX = currentPos;
            game.setScreen(new PlayScreen(game,items));
            switchPress.play(0.3f);
            dispose();
        }
        //Html report
        else if(Gdx.input.isKeyJustPressed(Input.Keys.F5)){
            ReportThread thread = new ReportThread("ReportHTMl", ReportThread.State.HTML,saveSound);
            thread.start();
        }
        //Pdf report
        else if(Gdx.input.isKeyJustPressed(Input.Keys.F6)){
            ReportThread thread = new ReportThread("ReportPDF", ReportThread.State.PDF,saveSound);
            thread.start();
        }
        //Cursor position
        try{
            if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
                selectNextSlot();
                switchSound.play(0.3f);
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
                selectPreviousSlot();
                switchSound.play(0.3f);
            }
        }catch (IndexOutOfBoundsException err) {
            //Play error sound
            switchError.play(0.3f);
        }
    }

    private void update(float delta){
        handleInput(delta);
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        //Update
        update(delta);
        //Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Draw stage
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        slots.clear();
        background.dispose();
        stage.dispose();
        music.dispose();
    }
}
