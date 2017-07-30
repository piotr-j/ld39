package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ld39.game.Map;
import io.piotrjastrzebski.ld39.game.utils.IntRect;

public abstract class Building<T extends Building> {
    public final int EAST = 0;
    public final int SOUTH = 1;
    public final int WEST = 2;
    public final int NORTH = 3;
    public final float buildCost;
    public final String name;
    public IntRect bounds = new IntRect();
    public Color tint = new Color(1, 1, 1, 1);
    public int direction = EAST;
    protected Map map;
    protected Buildings buildings;
    protected static Vector2 tmp = new Vector2();
    protected boolean flooded = false;
    protected float floodedTime;
    protected float floodedDemolishTimer = 5;

    public Building (String name, float buildCost, int x, int y, int width, int height) {
        this.name = name;
        this.buildCost = buildCost;
        bounds.set(x, y, width, height);
    }

    public void update (float delta) {
        if (flooded) {
            floodedTime += delta;
        } else if (floodedTime > 0){
            floodedTime -= delta * .25f;
        }
        if (floodedTime >= floodedDemolishTimer) {
            buildings.demolish(this);
        }
    }

    public float cx () {
        return bounds.x + bounds.width/2f;
    }

    public float cy () {
        return bounds.y + bounds.height/2f;
    }

    public void drawDebug(ShapeRenderer shapes) {
        shapes.setColor(Color.WHITE);
        shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapes.setColor(tint);
        shapes.rect(bounds.x + .15f, bounds.y + .15f, bounds.width - .3f, bounds.height - .3f);
    }

    public void drawDebug2 (ShapeRenderer shapes) {

    }

    void drawFlooded(ShapeRenderer shapes) {
        shapes.setColor(MathUtils.random(.7f, 1f), MathUtils.random(.3f, 7f), 0, .5f);
        shapes.circle(cx() + MathUtils.random(-.5f, .5f), cy() + MathUtils.random(-.5f, .5f), MathUtils.random(.2f, .8f), 12);
    }

    public abstract T duplicate();

    public void rotateCCW () {
        direction--;
        if (direction < EAST) direction = NORTH;
    }

    public void rotateCW () {
        direction++;
        if (direction > NORTH) direction = EAST;
    }

    public T duplicate (T instance) {
        instance.direction = direction;
        instance.map = map;
        instance.buildings = buildings;
        return instance;
    }

    public String info() {
        return name;
    }

    public void flooded (boolean flooded) {
        this.flooded = flooded;
    }
}
