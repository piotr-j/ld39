package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;

public class SolarPowerPlant extends Building<SolarPowerPlant> {
    public SolarPowerPlant (int x, int y) {
        super("Solar Power Plant", x, y, 2, 2);
        tint.set(Color.ROYAL);
    }

    @Override public SolarPowerPlant duplicate () {
        SolarPowerPlant instance = new SolarPowerPlant(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
