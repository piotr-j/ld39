package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;

public class ResearchLab extends Building<ResearchLab> {
    public ResearchLab (int x, int y) {
        super("Research Lab", x, y, 3, 3);
        tint.set(Color.LIGHT_GRAY);
    }

    @Override public ResearchLab duplicate () {
        ResearchLab instance = new ResearchLab(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
