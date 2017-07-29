package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.Color;

public class ConveyorBelt extends Building<ConveyorBelt> {
    public ConveyorBelt (int x, int y) {
        super("Conveyor Belt", x, y, 1, 1);
        tint.set(Color.YELLOW);
    }

    @Override public ConveyorBelt duplicate () {
        ConveyorBelt instance = new ConveyorBelt(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
