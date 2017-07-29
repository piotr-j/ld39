package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.ld39.game.Map;

public class CoalExtractor extends Building<CoalExtractor> {
    private final static int ext_size = 6;
    private final static int[] extractions = new int[]{
        0, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1,
        0, 1, 1, 1, 1, 0
    };
    private static final int extractTileCount;
    static {
        int count = 0;
        for (int ox = 0; ox < ext_size; ox++) {
            for (int oy = 0; oy < ext_size; oy++) {
                count += extractions[ox + oy * ext_size];
            }
        }
        extractTileCount = count;
    }
    private float extractPerSecond = 5;
    private float coal;
    private float coalCap = 50;
    public CoalExtractor (int x, int y) {
        super("Coal Extractor", x, y, 2, 2);
        tint.set(Color.DARK_GRAY);

    }

    private boolean fieldEmpty;
    @Override public void update (float delta) {
        super.update(delta);
        // TODO try to push coal
        if (coal >= coalCap) return;
        int bx = bounds.x - 2;
        int by = bounds.y - 2;
        float extracted = 0;
        float extractPerTile = extractPerSecond/extractTileCount * delta;
        for (int x = 0; x < ext_size; x++) {
            for (int y = 0; y < ext_size; y++) {
                int extracts = extractions[x + y * ext_size];
                if (extracts == 1) {
                    Map.Tile tile = map.getTile(bx + x, by + y);
                    if (tile == null || tile.coal <= 0) continue;
                    // we can extract nonexisting coal but who cares
                    tile.coal -= extractPerTile;
                    extracted += extractPerTile;
                }
            }
        }
        if (extracted > 0) {
            // we dont really care if we go over cap in here
            coal += extracted;
            if (coal >= coalCap) {
                Gdx.app.log("", "Reached coal cap!");
            }
        } else {
            fieldEmpty = true;
        }
    }

    @Override public void drawDebug (ShapeRenderer shapes) {
        int bx = bounds.x - 2;
        int by = bounds.y - 2;
        for (int x = 0; x < ext_size; x++) {
            for (int y = 0; y < ext_size; y++) {
                int extracts = extractions[x + y * ext_size];
                if (extracts == 1) {
                    Map.Tile tile = map.getTile(bx + x, by + y);
                    if (tile == null) continue;
                    if (tile.coal > 0) {
                        shapes.setColor(0, 1, 0, .5f);
                    } else {
                        shapes.setColor(1, 0, 0, .5f);
                    }
                    shapes.rect(bx + x + .1f, by + y + .1f, .8f, .8f);
                }
            }
        }
        super.drawDebug(shapes);
        if (fieldEmpty || coal >= coalCap) {
            float cx = bounds.x + bounds.width/2f;
            float cy = bounds.y + bounds.height/2f;
            shapes.setColor(Color.YELLOW);
            shapes.triangle(cx - .15f, cy + .6f, cx + .15f, cy + .6f, cx, cy - .3f);
            shapes.circle(cx, cy - .5f, .1f, 8);
        }
    }

    @Override public CoalExtractor duplicate () {
        CoalExtractor instance = new CoalExtractor(bounds.x, bounds.y);
        return super.duplicate(instance);
    }
}
