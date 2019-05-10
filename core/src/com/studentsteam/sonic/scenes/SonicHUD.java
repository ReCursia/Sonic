package com.studentsteam.sonic.scenes;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.studentsteam.sonic.Main;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class SonicHUD
{
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timecount;
    private static Integer score;
    private static Integer lifecount;
    public static Integer rings;


    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label sonicLabelScore;
    private static Label lifecountLabel;
    private Label lifecountLabelName;
    private static Label ringsLabel;
    private Label ringsLabelName;

    public SonicHUD(SpriteBatch sb)
    {
        worldTimer = 600;
        //worldTimer = 10;
        timecount = 0;
        score = 0;
        lifecount = 0;
        rings = 0;
        //Сomment till the end

        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);
        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        sonicLabelScore = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        lifecountLabel = new Label(String.format("X %d", lifecount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lifecountLabelName = new Label("SONIC", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        ringsLabel = new Label(String.format("%d", rings), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ringsLabelName = new Label("RINGS", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(sonicLabelScore).padRight(900).padTop(10);
        table.row();
        table.add(scoreLabel).padRight(750).padTop(-20);
        table.row();
        table.add(timeLabel).padRight(900).padTop(10);
        table.row();
        table.add(countdownLabel).padRight(750).padTop(-20);
        table.row();
        table.add(ringsLabelName).padRight(900).padTop(10);
        table.row();
        table.add(ringsLabel).padRight(750).padTop(-20);
        table.row();
        table.add(lifecountLabelName).padRight(900).padTop(550);
        table.row();
        table.add(lifecountLabel).padRight(850).padTop(5);
        //add a second row to our table
        table.row();
        //table.add(timeLabel).expandX().padTop(10);
        //table.add(countdownLabel).expandX();
        //add our table to the stage
        stage.addActor(table);
    }

    /**
     *
     * @return amount of rings
     */
    public int getRings(){
        return rings;
    }

    /**
     *
     * @return life count
     */
    public int getLifeCount(){
        return lifecount;
    }

    /**
     *
     * @return world time
     */
    public int getTime(){
        return worldTimer;
    }
    public void update(float dt)
    {
        timecount += dt;
        if(timecount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
               // timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timecount = 0;
        }
    }

    /**
     * Increasing points
     * @param value value of points
     */
    public static void addScore(int value)
    {
        score += value;
        //ТЕСТЫ
        scoreLabel.setText(String.format("%06d", score));
    }

    /**
     * Returns score
     * @return score
     */
    public static int getScore(){
        return score;
    }
    /**
     * Increasing rings. Every 100 rings are giving you 1 life
     * @param value rings
     */
    public static void addRings(int value)
    {
        rings += value;
        addScore(200*value); //200 points for every ring
        //TESTS
        ringsLabel.setText(String.format("%d", rings));
        //If player collected 100 rings, the game gives him extra life
        if(rings  % 100 == 0 && rings != 0)
        {
            addLife(1);
        }
    }

    /**
     * Increasing life count
     * @param value
     */
    public static void addLife(int value)
    {
        lifecount += value;
        //TESTS
        lifecountLabel.setText(String.format("X %d", lifecount));
    }

    /**
     * Decrements life counter
     */
    public static void decrementLife(){
        --lifecount;
        //TESTS
        lifecountLabel.setText(String.format("X %d", lifecount));
    }
}

