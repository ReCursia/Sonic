package com.studentsteam.sonic.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static org.junit.jupiter.api.Assertions.*;

class SonicHUDTest {
    private SonicHUD hud;
    private SpriteBatch batch; //Так как графика нигде не используется, то просто передаем null (неважно)
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        hud = new SonicHUD(batch);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.out.println("Тесты завершены!");
    }

    @org.junit.jupiter.api.Test
    void decrementLife() {
        int before = hud.getLifeCount();
        hud.decrementLife();
        assertEquals(hud.getLifeCount(),before-1);
    }

    @org.junit.jupiter.api.Test
    void addLife(){
        int before = hud.getLifeCount();
        hud.addLife(20);
        assertEquals(hud.getLifeCount(),before+20);
    }
    @org.junit.jupiter.api.Test
    void addScore() {
        int before = hud.getScore();
        hud.addScore(400);
        assertEquals(hud.getScore(),before+400);
    }

    @org.junit.jupiter.api.Test
    void addRings() {
        //Добавление колец (также должно быть +4 жизни)
        int ringsBefore = hud.getRings();
        hud.addRings(99);
        assertEquals(hud.getRings(),ringsBefore+99);
        //За каждые 100 колец дается одна жизнь
        int lifesBefore = hud.getLifeCount();
        hud.addRings(1);
        assertEquals(hud.getLifeCount(),lifesBefore+1);
    }
}