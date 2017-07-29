package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.jam.ecs.Globals;

public class Entity {
    private static final String TAG = Entity.class.getSimpleName();
    private static int _ID = 0;
    private final int id = _ID++;
    public Circle bounds = new Circle();
    public float rotation;
    public int layer;
    private int segments;
    public Color color = new Color(Color.WHITE);
    public boolean over;

    public Entity setBounds (float x, float y, float radius) {
        bounds.set(x, y, radius);
        segments = Math.max((int)(Globals.SCALE * Math.sqrt(bounds.radius)/2), 6);
        return this;
    }

    public void update (float delta) {

    }

    public void draw (SpriteBatch batch) {

    }

    private Vector2 overPos = new Vector2();
    private static Vector2 tmp = new Vector2();
    public void drawDebug (ShapeRenderer shapes) {
        shapes.setColor(color);
        shapes.circle(bounds.x, bounds.y, bounds.radius, segments);
        tmp.set(1, 0).rotate(rotation);
        tmp.scl(bounds.radius);
        shapes.line(bounds.x, bounds.y, bounds.x + tmp.x, bounds.y + tmp.y);
        if (over) {
            shapes.setColor(Color.MAGENTA);
            shapes.circle(bounds.x, bounds.y, bounds.radius + .1f, segments);
            shapes.circle(overPos.x, overPos.y, .3f, 16);
            shapes.line(bounds.x, bounds.y, overPos.x, overPos.y);
        }
    }

    public void enter (Vector2 position) {
        over = true;
    }

    public void over (Vector2 position) {
        overPos.set(position);
    }

    public void exit (Vector2 position) {
        over = false;
    }
}
