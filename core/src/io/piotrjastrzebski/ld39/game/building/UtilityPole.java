package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StringBuilder;

public class UtilityPole extends Building<UtilityPole> implements PowerConnector {
    public final float MAX_DISTANCE = 8;

    public UtilityPole (int x, int y) {
        super("Utility Pole", 5, x, y, 1, 1);
        tint.set(Color.BROWN);
    }

    public void invalidate() {
        for (PowerConnector connector : connectors) {
            connector.disconnect(this);
        }

        connectors.clear();
        Array<Building> all = buildings.getAll();
        tmp.set(cx(), cy());
        for (int i = 0; i < all.size; i++) {
            Building other = all.get(i);
            if (!(other instanceof PowerConnector)) continue;
            if (other == this) continue;
            Building owner = ((PowerConnector)other).owner();
            if (tmp.dst(owner.cx(), owner.cy()) <= MAX_DISTANCE) {
                if (((PowerConnector)other).connect(this)) {
                    connect((PowerConnector)other);
                }
            }
        }
    }

    @Override public String info () {
        StringBuilder sb = new StringBuilder(name);
        if (connectors.size > 0) {
            sb.append("\nConnected =").append(connectors.size);
        } else {
            sb.append("\nNot connected!");
        }
        return sb.toString();
    }

    @Override public void drawDebug (ShapeRenderer shapes) {
        super.drawDebug(shapes);

    }

    @Override public void drawDebug2 (ShapeRenderer shapes) {
        super.drawDebug2(shapes);
        shapes.setColor(Color.BROWN);
        float cx = cx();
        float cy = cy();
        for (PowerConnector connector : connectors) {
            Building building = connector.owner();
            shapes.rectLine(cx, cy, building.cx(), building.cy(), .05f);
        }
        if (flooded) {
            drawFlooded(shapes);
        }
    }

    @Override public UtilityPole duplicate () {
        UtilityPole instance = new UtilityPole(bounds.x, bounds.y);
        return super.duplicate(instance);
    }

    private ObjectSet<PowerConnector> connectors = new ObjectSet<>();

    @Override public boolean connect (PowerConnector other) {
        connectors.add(other);
        return true;
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
}
