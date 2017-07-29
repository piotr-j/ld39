package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;

public class CoalPowerPlant extends Building<CoalPowerPlant> {
    public CoalPowerPlant (int x, int y) {
        super("Coal Power Plant", x, y, 3, 2);
        tint.set(Color.FIREBRICK);
    }

    @Override public CoalPowerPlant duplicate () {
        CoalPowerPlant instance = new CoalPowerPlant(bounds.x, bounds.y);
        return instance;
    }
}
