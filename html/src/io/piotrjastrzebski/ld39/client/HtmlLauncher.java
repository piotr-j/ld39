package io.piotrjastrzebski.ld39.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import io.piotrjastrzebski.ld39.LD39Game;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(880, 554);
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener () {
            return new LD39Game();
    }
}
