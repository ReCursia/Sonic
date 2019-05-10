package com.studentsteam.sonic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.screens.assets.ScrollingBackground;
import com.studentsteam.sonic.screens.assets.SonicLogo;
import com.studentsteam.sonic.screens.assets.TextureActor;
import org.apache.log4j.Logger;

public class StartScreen implements Screen {
    //Logs
    private final Logger log = Logger.getLogger(this.getClass());

    private Main game;

    private Music music;
    private TextureActor sega;
    //Stage and viewport
    private Stage stage;
    private Viewport viewport;

    public StartScreen(Main game){
        log.info("Запуск начального экрана");
        this.game = game;
        //Texture
        this.sega = new TextureActor("start/sega.png");
        sega.setPosition(Gdx.graphics.getWidth()-sega.getWidth(),5);
        //Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("start/background.mp3"));
        music.setLooping(false);
        music.setVolume(0.1f);
        music.play();
        //Creating stage and viewport
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        //Creating scrolling background
        ScrollingBackground background = new ScrollingBackground("start/BG.png",1.0f);
        //Creating logo
        SonicLogo logo = new SonicLogo();
        //Adding actors
        stage.addActor(background);
        stage.addActor(logo);
        stage.addActor(sega);
     }

    @Override
    public void show() {

    }

    private void handleInput(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            log.info("Переход к экрану сохранения");
            game.setScreen(new SaveScreen(game));
            dispose();
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
    public void resize(int width, int height){

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
        music.dispose();
        stage.dispose();
    }
}
