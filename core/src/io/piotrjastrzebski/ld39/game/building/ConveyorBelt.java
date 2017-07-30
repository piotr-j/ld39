package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import io.piotrjastrzebski.ld39.game.Coal;
import io.piotrjastrzebski.ld39.game.Map;

public class ConveyorBelt extends Building<ConveyorBelt> implements CoalConsumer{
    private Coal coal;
    private float timer;
    private final float moveTime = .5f;
    public ConveyorBelt (int x, int y) {
        super("Conveyor Belt", 10, x, y, 1, 1);
        tint.set(.45f, .45f, 0, 1);
    }

    @Override public void update (float delta) {
        super.update(delta);
        if (coal == null) return;
        timer += delta;
        if (timer >= moveTime) {
            timer = moveTime;
            CoalConsumer next = findNext();
            if (next != null) {
                if (next.accept(coal)) {
                    timer = 0;
                    coal = null;
                }
            }
        }
    }

    private CoalConsumer findNext () {
        Map.Tile tile = null;
        switch (direction) {
        case EAST: {
            tile = map.getTile(bounds.x + 1, bounds.y);
        } break;
        case SOUTH: {
            tile = map.getTile(bounds.x, bounds.y -1);
        } break;
        case WEST: {
            tile = map.getTile(bounds.x - 1, bounds.y);
        } break;
        case NORTH: {
            tile = map.getTile(bounds.x, bounds.y + 1);
        } break;
        }
        if (tile == null) return null;
        if (tile.building instanceof CoalConsumer) {
            return (CoalConsumer)tile.building;
        }
        return null;
    }

    @Override public void drawDebug (ShapeRenderer shapes) {
        super.drawDebug(shapes);
        shapes.setColor(.9f, .9f, 0, 1);
        float cx = cx();
        float cy = cy();
        float s = .3f;
        float s2 = s/2;
        switch (direction) {
        case EAST: {
            shapes.triangle(cx, cy - s2, cx, cy + s2, cx + s, cy);
            shapes.triangle(cx - s, cy - s2, cx - s, cy + s2, cx, cy);
        } break;
        case SOUTH: {
            shapes.triangle(cx, cy - s, cx + s2, cy, cx - s2, cy);
            shapes.triangle(cx, cy, cx + s2, cy + s, cx - s2, cy + s);
        } break;
        case WEST: {
            shapes.triangle(cx - s, cy, cx, cy - s2, cx, cy + s2);
            shapes.triangle(cx, cy, cx + s, cy - s2, cx + s, cy + s2);
        } break;
        case NORTH: {
            shapes.triangle(cx - s2, cy, cx + s2, cy, cx, cy + s);
            shapes.triangle(cx - s2, cy - s, cx + s2, cy - s, cx, cy);
        } break;
        }
    }

    @Override public void drawDebug2 (ShapeRenderer shapes) {
        super.drawDebug2(shapes);

        if (coal != null) {
            shapes.setColor(.25f, .2f, .2f, 1);
            float cx = cx();
            float cy = cy();
            float a = MathUtils.clamp(timer / moveTime, 0, 1);
            float x = 0;
            float y = 0;
            switch (direction) {
            case EAST: {
                x = Interpolation.linear.apply(cx - .5f, cx + .5f, a);
                y = cy;
            }
            break;
            case SOUTH: {
                x = cx;
                y = Interpolation.linear.apply(cy - .5f, cy + .5f, 1 - a);
            }
            break;
            case WEST: {
                x = Interpolation.linear.apply(cx - .5f, cx + .5f, 1 - a);
                y = cy;
            }
            break;
            case NORTH: {
                x = cx;
                y = Interpolation.linear.apply(cy - .5f, cy + .5f, a);
            }
            break;
            }
            shapes.circle(x, y, .3f, 6);
        }
        if (flooded) {
            drawFlooded(shapes);
        }
    }

    @Override public ConveyorBelt duplicate () {
        ConveyorBelt instance = new ConveyorBelt(bounds.x, bounds.y);
        return super.duplicate(instance);
    }

    @Override public boolean accept (Coal coal) {
        if (this.coal == null) {
            this.coal = coal;
            return true;
        }
        return false;
    }
}
