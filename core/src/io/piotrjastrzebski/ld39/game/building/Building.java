package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ld39.game.utils.IntRect;

public abstract class Building<T extends Building> {
    public final int EAST = 0;
    public final int SOUTH = 1;
    public final int WEST = 2;
    public final int NORTH = 4;
    public final String name;
    public IntRect bounds = new IntRect();
    public Color tint = new Color(1, 1, 1, 1);
    public int direction = EAST;

    public Building (String name, int x, int y, int width, int height) {
        this.name = name;
        bounds.set(x, y, width, height);
    }

    public void update (float delta) {

    }

    private static Vector2 tmp = new Vector2();
    public void drawDebug(ShapeRenderer shapes) {
        shapes.setColor(Color.WHITE);
        shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapes.setColor(tint);
        shapes.rect(bounds.x + .15f, bounds.y + .15f, bounds.width - .3f, bounds.height - .3f);
        shapes.setColor(0, 0, 0, .3f);
        float cx = bounds.x + bounds.width * .5f;
        float cy = bounds.y + bounds.height * .5f;
        tmp.set(Math.min(bounds.width * .33f, bounds.height * .33f), 0);
        switch (direction) {
        case EAST: {
            tmp.rotate(0);
        } break;
        case SOUTH: {
            tmp.rotate(-90);
        } break;
        case WEST: {
            tmp.rotate(-180);
        } break;
        case NORTH: {
            tmp.rotate(-270);
        } break;
        }
        shapes.rectLine(cx, cy, cx + tmp.x, cy + tmp.y, .1f);
    }

    public abstract T duplicate();
}
