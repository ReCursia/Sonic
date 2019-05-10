package com.studentsteam.sonic;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.studentsteam.sonic.screens.StartScreen;

public class Main extends Game {
	public static int SAVE_INDEX; //Save index
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 720;
	//Box2D Collision Bits
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short SONIC_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short FIREBALL_BIT = 1024;

	public SpriteBatch batch; //Only one for game (delegating game object to each screen)
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new StartScreen(this));
	}
}
