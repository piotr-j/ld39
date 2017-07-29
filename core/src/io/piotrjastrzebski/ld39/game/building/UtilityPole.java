package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class UtilityPole extends Building<UtilityPole> {
    private ObjectSet<UtilityPole> connected = new ObjectSet<>();
    public final float MAX_DISTANCE = 8;

    public UtilityPole (int x, int y) {
        super("Utility Pole", x, y, 1, 1);
        tint.set(Color.BROWN);
    }

    public void invalidate() {
        connected.clear();
        Array<Building> all = buildings.getAll();
        tmp.set(cx(), cy());
        for (int i = 0; i < all.size; i++) {
            Building other = all.get(i);
            if (!(other instanceof UtilityPole)) continue;
            if (other == this) continue;
            UtilityPole up = (UtilityPole)other;
            if (tmp.dst(up.cx(), up.cy()) <= MAX_DISTANCE) {
                connected.add(up);
            }
        }
    }

    @Override public void drawDebug (ShapeRenderer shapes) {
        super.drawDebug(shapes);
        shapes.setColor(Color.BROWN);
        float cx = cx();
        float cy = cy();
        for (UtilityPole pole : connected) {
            shapes.rectLine(cx, cy, pole.cx(), pole.cy(), .05f);
        }
    }

    @Override public UtilityPole duplicate () {
        UtilityPole instance = new UtilityPole(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
