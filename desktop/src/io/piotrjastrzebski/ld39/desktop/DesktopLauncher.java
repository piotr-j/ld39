package io.piotrjastrzebski.ld39.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.piotrjastrzebski.ld39.LD39Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("ld39 - ???");
		// web size
		config.setWindowedMode(880, 554);
		new Lwjgl3Application(new LD39Game(), config);
	}
}
