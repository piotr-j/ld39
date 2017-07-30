package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SolarPowerPlant extends Building<SolarPowerPlant> {
    public SolarPowerPlant (int x, int y) {
        super("Solar Power Plant", x, y, 2, 2);
        tint.set(Color.ROYAL);
    }

    @Override public void drawDebug2 (ShapeRenderer shapes) {
        super.drawDebug2(shapes);
        if (flooded) {
            drawFlooded(shapes);
        }
    }

    @Override public SolarPowerPlant duplicate () {
        SolarPowerPlant instance = new SolarPowerPlant(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
