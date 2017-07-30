package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import io.piotrjastrzebski.ld39.game.Coal;

public class CoalPowerPlant extends Building<CoalPowerPlant> implements CoalConsumer {
    private Array<Coal> coals = new Array<>();
    private int coalCap = 10;
    private float burnrate = 1;
    private float burningCoal;
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
        if (burningCoal <= 0 && coals.size > 0) {
            burningCoal = coals.pop().value;
        }
        if (burningCoal > 0) {
            burningCoal -= burnrate * delta;
            power += genPerSecond * delta;
            if (power > powerCap) power = powerCap;
            generating = true;
        } else {
            generating = false;
        }
    }

    @Override public String info () {
        StringBuilder sb = new StringBuilder(name);
        float allCoal = burningCoal;
        for (Coal coal : coals) {
            allCoal += coal.value;
        }
        sb.append("\nCoal = ").append(allCoal);
        sb.append("\nPower = ").append(power);
        if (!generating) {
            sb.append("\nNo coal to burn!");
        }
        if (coals.size > coalCap) {
            sb.append("\nCoal storage full!");
        }
        return sb.toString();
    }

    @Override public void drawDebug (ShapeRenderer shapes) {
        super.drawDebug(shapes);
        if (generating) {
            shapes.setColor(0, 0, 0, .5f);
            shapes.circle(cx() + MathUtils.random(-.5f, .5f), cy() + MathUtils.random(-.5f, .5f), MathUtils.random(.2f, .8f), 12);
        } else {
            float cx = cx();
            float cy = cy();
            shapes.setColor(Color.YELLOW);
            shapes.triangle(cx - .15f, cy + .6f, cx + .15f, cy + .6f, cx, cy - .3f);
            shapes.circle(cx, cy - .5f, .1f, 8);
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
