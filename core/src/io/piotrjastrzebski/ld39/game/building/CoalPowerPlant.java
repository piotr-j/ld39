package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.ld39.game.Coal;

public class CoalPowerPlant extends Building<CoalPowerPlant> implements CoalConsumer {
    private Array<Coal> coals = new Array<>();
    private int coalCap = 10;
    private float burnrate = 1;
    private float coal;
    private float genPerSecond = 10;
    private float power;
    private float powerCap = 1000;
    public CoalPowerPlant (int x, int y) {
        super("Coal Power Plant", x, y, 3, 2);
        tint.set(Color.FIREBRICK);
    }

    boolean generating;
    @Override public void update (float delta) {
        super.update(delta);
        if (coal <= 0 && coals.size > 0) {
            coal = coals.pop().value;
        }
        if (coal > 0) {
            coal -= burnrate * delta;
            power += genPerSecond * delta;
            if (power > powerCap) power = powerCap;
            generating = true;
        } else {
            generating = false;
        }
    }

    @Override public void drawDebug (ShapeRenderer shapes) {
        super.drawDebug(shapes);
        if (generating) {
            shapes.setColor(0, 0, 0, .5f);
            shapes.circle(cx() + MathUtils.random(-.5f, .5f), cy() + MathUtils.random(-.5f, .5f), MathUtils.random(.2f, .8f), 12);
        }
    }

    @Override public boolean accept (Coal coal) {
        if (coals.size > coalCap) return false;
        coals.add(coal);
        return true;
    }

    @Override public CoalPowerPlant duplicate () {
        CoalPowerPlant instance = new CoalPowerPlant(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
