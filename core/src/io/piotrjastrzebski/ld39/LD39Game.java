package io.piotrjastrzebski.ld39;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;

public class LD39Game extends Game {
    public SpriteBatch batch;

    @Override
    public void create () {
        batch = new SpriteBatch();
        VisUI.load();
        setScreen(new GameScreen(this));
    }

    @Override public void dispose () {
        super.dispose();
        VisUI.dispose();
        batch.dispose();
    }
}
