package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;

public class UtilityPole extends Building<UtilityPole> {
    public UtilityPole (int x, int y) {
        super("Utility Pole", x, y, 1, 1);
        tint.set(Color.BROWN);
    }

    @Override public UtilityPole duplicate () {
        UtilityPole instance = new UtilityPole(bounds.x, bounds.y);
        return instance;
    }
}
