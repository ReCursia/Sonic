package com.studentsteam.sonic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.studentsteam.sonic.Main;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DesktopLauncher {
	public static final String PATH = "log4j.properties";

	public static void main (String[] arg) {
		PropertyConfigurator.configure(PATH);
		System.setProperty("user.name","seconduser"); //Фикс для открытия файлов (пробема в кодировке имени пользователя
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Main(), config);
	}
}
