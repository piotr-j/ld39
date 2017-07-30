package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ObjectSet;
import io.piotrjastrzebski.ld39.game.Research;

public class SolarPanel extends Building<SolarPanel> implements PowerProducer, PowerConnector {
    protected final static float powerPerSecond = 10; // pre efficiency scale
    private float powerCap = 50;
    private float power;

    public SolarPanel (int x, int y) {
        super("Solar Panel", 500, x, y, 1, 2);
        tint.set(Color.ROYAL);
    }

    @Override public void update (float delta) {
        super.update(delta);
        if (power < powerCap) {
            power += powerPerSecond * delta * Research.efficiency;
        }
    }

    @Override public String info () {
        return name +"\nPower = " + power;
    }

    @Override public void drawDebug2 (ShapeRenderer shapes) {
        super.drawDebug2(shapes);
        if (flooded) {
            drawFlooded(shapes);
        }
    }

    @Override public SolarPanel duplicate () {
        SolarPanel instance = new SolarPanel(bounds.x, bounds.y);
        return super.duplicate(instance);
    }

    private ObjectSet<PowerConnector> connectors = new ObjectSet<>();

    @Override public boolean connect (PowerConnector other) {
        if (other instanceof UtilityPole) {
            connectors.add(other);
            return true;
        }
        return false;
    }

    @Override public void disconnect (PowerConnector connector) {
        connectors.remove(connector);
    }

    @Override public void disconnectAll () {
        for (PowerConnector connector : connectors) {
            connector.disconnect(this);
        }
        connectors.clear();
    }

    @Override public ObjectSet<PowerConnector> connected () {
        return connectors;
    }

    @Override public Building owner () {
        return this;
    }

    @Override public float storage () {
        return power;
    }

    @Override public float consume (float totalPower) {
        float consumed = power - totalPower;
        power -= totalPower;
        if (consumed < 0) {
            power = 0;
            return -consumed;
        }
        return 0;
    }
}
