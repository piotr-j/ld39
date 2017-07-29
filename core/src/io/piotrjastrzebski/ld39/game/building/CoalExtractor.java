package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;

public class CoalExtractor extends Building<CoalExtractor> {
    public CoalExtractor (int x, int y) {
        super("Coal Extractor", x, y, 2, 2);
        tint.set(Color.DARK_GRAY);
    }

    @Override public CoalExtractor duplicate () {
        CoalExtractor instance = new CoalExtractor(bounds.x, bounds.y);
        return instance;
    }
}
