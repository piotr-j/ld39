package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectSet;

public class ResearchLab extends Building<ResearchLab> implements PowerConnector, PowerConsumer {
    private float powerRequiredToRun = 100;
    private float powerRequiredToRunFrame;
    private boolean powered;
    private float powerTimer;
    public ResearchLab (int x, int y) {
        super("Research Lab", 1000, x, y, 3, 3);
        tint.set(Color.LIGHT_GRAY);
    }

    @Override public ResearchLab duplicate () {
        ResearchLab instance = new ResearchLab(bounds.x, bounds.y);
        return super.duplicate(instance);
    }

    @Override public void update (float delta) {
        super.update(delta);
        powerRequiredToRunFrame = powerRequiredToRun * delta;
        if (powered) {
            powerTimer -= delta;
            if (powerTimer < 0) {
                powered = false;
            }
        }
    }

    @Override public String info () {
        return name + "\n" + (powered?"Working":"No power!");
    }

    @Override public void drawDebug2 (ShapeRenderer shapes) {
        super.drawDebug2(shapes);
        if (powered) {
            shapes.setColor(Color.ROYAL);
            shapes.circle(cx() + MathUtils.random(-.5f, .5f), cy() + MathUtils.random(-.5f, .5f), MathUtils.random(.2f, .8f), MathUtils.random(3, 6));
        }
        if (flooded) {
            drawFlooded(shapes);
        }
    }

    private ObjectSet<PowerConnector> connectors = new ObjectSet<>();
    @Override public boolean connect (PowerConnector other) {
        if (other instanceof UtilityPole) {
            connectors.add(other);
            return true;
        }
        return false;
    }

    @Override public ObjectSet<PowerConnector> connected () {
        return connectors;
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

    @Override public Building owner () {
        return this;
    }

    @Override public float required () {
        return powerRequiredToRunFrame;
    }

    @Override public void provide () {
        powered = true;
        powerTimer = 1;
    }

    public boolean isPowered () {
        return powered;
    }
}
