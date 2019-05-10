package com.studentsteam.sonic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.studentsteam.sonic.Main;
import com.studentsteam.sonic.exceptions.OutOfWorldCamPositionX;
import com.studentsteam.sonic.exceptions.OutOfWorldCamPositionY;
import com.studentsteam.sonic.sprites.Eggman;
import com.studentsteam.sonic.sprites.Enemy;
import com.studentsteam.sonic.sprites.InteractiveItem;
import com.studentsteam.sonic.sprites.Sonic;
import com.studentsteam.sonic.threads.PropertiesThread;
import com.studentsteam.sonic.threads.SaveThread;
import com.studentsteam.sonic.tools.B2WorldCreator;
import com.studentsteam.sonic.tools.database.DataItem;
import com.studentsteam.sonic.tools.WorldContactListener;
import com.studentsteam.sonic.scenes.SonicHUD;
import org.apache.log4j.Logger;


public class PlayScreen implements Screen {
    //Logs
    private final Logger log = Logger.getLogger(this.getClass());

    private Main game;

    private Music music;
    private Sound saveSound;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    //Tiled map
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    //Sonic
    private Sonic sonic;
    private SonicHUD hud;
    //Saves
    private Array<DataItem> fileItems;
    //Map properties
    private int mapWidth;
    private int mapHeight;
    //Box2D creator
    private B2WorldCreator creator;
    //Default constructor
    public PlayScreen(Main game,Array<DataItem> fileItems){
        log.info("Запуск игры");
        defineScreen(game,fileItems);
        //Default life count
        SonicHUD.addLife(3);
    }

    private void defineScreen(Main game,Array<DataItem> fileItems){
        this.game = game;
        //Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("play/HongKong_Music_redacted.mp3"));
        music.setLooping(true);
        //music.setVolume(0.1f);
        music.play();
        //Save sound
        this.saveSound = Gdx.audio.newSound(Gdx.files.internal("play/save.mp3"));
        //Files
        this.fileItems = fileItems;
        //Hud
        hud = new SonicHUD(game.batch);
        //Setting cam
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false,600,400);
        //FitViewport
        gamePort = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),gameCam);
        //Box2d
        this.world = new World(new Vector2(0,-15000),true);
        //Tiled map
        TmxMapLoader mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("play/Metropolis.tmx");
        //Properties
        PropertiesThread propThread = new PropertiesThread("PropertiesThread",map);
        propThread.start();
        //Objects(world)
        creator = new B2WorldCreator(this);
        //getting rings (from savefile)
        SonicHUD.addRings(getData().rings);
        //Create sonic
        this.sonic = new Sonic(this);
        //Renderers
        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.b2dr = new Box2DDebugRenderer();
        //Create listener
        world.setContactListener(new WorldContactListener());
        //Ждём окончания потока
        try{
            propThread.thrd.join();
        }catch (InterruptedException err){
            err.printStackTrace();
        }
        //Получаем данные из выполненного потока PropThread
        this.mapHeight = propThread.getMapHeight();
        this.mapWidth = propThread.getMapWidth();
    }
    private void handleInput(float dt){
        //Saving
        if(Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            SaveThread saveThread = new SaveThread("SaveThread", sonic, hud, fileItems, saveSound);
            saveThread.start();
        }
    }

    /**
     * Getting information about save from array
     * @return save information
     */
    public DataItem getData(){
        for(DataItem item:fileItems){
            if(Main.SAVE_INDEX == item.index){
                return item;
            }
        }
        return new DataItem(50.0f,400.0f,0,Main.SAVE_INDEX);
    }
    private void update(float dt){
        //first input interaction
        handleInput(dt);
        //Sonic
        sonic.update(dt);
        //Enemies
        for(Enemy enemy:creator.getEnemies()){
            enemy.update(dt);
        }
        //Items
        for(InteractiveItem item: creator.getRings())
        {
            item.update(dt);
        }
        //Hud update
        hud.update(dt);
        //World step
        world.step(dt,8,3);
        //Set cam position
        setCamPositionX();
        setCamPositionY();
        gameCam.update();
        renderer.setView(gameCam);
    }

    /**
     * Setting cam position to follow sonic Y position
     */
    private void setCamPositionY(){
        try{
            //Y position
            if (sonic.getPositionY() < gameCam.viewportHeight / 2 || sonic.getPositionY() > mapHeight-gameCam.viewportHeight/2 ) {
                throw new OutOfWorldCamPositionY(sonic.getPositionY());
            }
            gameCam.position.y = sonic.getPositionY();
        }catch(OutOfWorldCamPositionY err){
            log.debug(err.getMessage());
        }
    }
    /**
     * Setting cam position to follow sonic X position
     */
    private void setCamPositionX(){
        try{
            //X position
            if (sonic.getPositionX() < gameCam.viewportWidth / 2 || sonic.getPositionX() > mapWidth-gameCam.viewportWidth/2) {
                throw new OutOfWorldCamPositionX(sonic.getPositionX());
            }
            gameCam.position.x = sonic.getPositionX();
        }catch(OutOfWorldCamPositionX err){
            log.debug(err.getMessage());
        }
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render our game map
        renderer.render();
        //renderer our Box2DDebugLines
        //b2dr.render(world,gameCam.combined);

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //sonic, eggman, rings
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        sonic.draw(game.batch);
        for(Enemy item: creator.getEnemies()){
            item.draw(game.batch);
        }
        for(InteractiveItem item: creator.getRings())
        {
            item.draw(game.batch);
        }

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    public TiledMap getMap()
    {
        return map;
    }
    public World getWorld()
    {
        return world;
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
        //Dispose sonic rings and eggman
        music.dispose();
        hud.stage.dispose();
        //renderer.dispose();
        //map.dispose();
        //Errors??
        //world.dispose();
        //b2dr.dispose();
    }
}
