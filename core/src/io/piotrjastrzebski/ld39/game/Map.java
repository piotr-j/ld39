package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ld39.game.building.Building;
import io.piotrjastrzebski.ld39.game.utils.IntRect;
import io.piotrjastrzebski.ld39.game.utils.Maths;

public class Map {
    private Tile[] tiles;
    public final int width;
    public final int height;
    public static final float COAL_MAX = 100;
    public static final float ELEV_MIN = -5;
    public static final float ELEV_MAX = 50;
    public IntRect bounds = new IntRect();
    private float baseSeaLevel = .25f;
    private float seaLevel = baseSeaLevel;
    private float baseHillLevel = .7f;
    private float hillLevel = baseHillLevel;
    private float seaLevelRaisingChange = 0.01f;
    private float seaLevelLoweringChange = -0.0025f;
    private float seaLevelChange = 0;
    public Map () {
        Pixmap terrain = new Pixmap(Gdx.files.internal("map2.png"));
        Pixmap coal = new Pixmap(Gdx.files.internal("coal2.png"));
        width = terrain.getWidth();
        height = terrain.getHeight();
        bounds.set(0, 0, width, height);
        tiles = new Tile[width * height];
        Color out = new Color();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color.rgba8888ToColor(out, terrain.getPixel(x, height - y - 1));
                // simple grey scale
                float elevation = (out.r + out.g + out.b)/3;
                Tile tile = new Tile(x, y, index(x, y), elevation);
                tiles[tile.index] = tile;
                Color.rgba8888ToColor(out, coal.getPixel(x, height - y - 1));
                tile.coal = out.a * COAL_MAX;
            }
        }

        updateSeaLevel();
    }

    private float lastSeaLevel = 0;
    private void updateSeaLevel () {
//        if (MathUtils.isEqual(lastSeaLevel, seaLevel)) return;
//        lastSeaLevel = seaLevel;
        for (Tile tile : tiles) {
            if (tile.rawElevation <= seaLevel) {
                if (tile.type != TILE_TYPE_WATER) {
                    if (tile.building != null) {
                        tile.building.flooded(true);
                    }
                }
                tile.type = TILE_TYPE_WATER;
                float a = tile.rawElevation/seaLevel;
                tile.color.set(0, .15f, .5f + a/3, 1);
            } else if (tile.rawElevation >= hillLevel) {
                if (tile.type == TILE_TYPE_WATER) {
                    if (tile.building != null) {
                        tile.building.flooded(false);
                    }
                }
                tile.type = TILE_TYPE_HILL;
                float a = (tile.rawElevation - hillLevel)/(1-hillLevel);
                tile.color.set(.5f + a/2, .5f + a/2, .5f + a/2, 1);
            } else {
                tile.type = TILE_TYPE_GROUND;
                float a = (tile.rawElevation - seaLevel)/(hillLevel - seaLevel);
                tile.color.set(.8f-a*.7f, .8f - a/3, 0, 1);
            }

        }
    }

    public Tile getTile (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return tiles[index(x, y)];
    }

    public int index (int x, int y) {
        return x + y * width;
    }

    public int x (int index) {
        return index % width;
    }

    public int y (int index) {
        return index / width;
    }

    public void drawDebug (ShapeRenderer shapes) {
        for (Tile tile : tiles) {
            shapes.setColor(tile.color);
            shapes.rect(tile.x, tile.y, 1, 1);
            if (coalOverlay && tile.type == TILE_TYPE_GROUND) {
                shapes.setColor(.15f, .1f, .1f, 1);
                shapes.circle(tile.x + .5f, tile.y + .5f, tile.coal / COAL_MAX * .4f, 6);
            }
        }
    }

    private boolean coalOverlay = true;
    private Vector2 sp = new Vector2();
    public void update (Vector2 tp, float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            coalOverlay = !coalOverlay;
        }
        if (seaLevelChange != 0) {
            seaLevel = MathUtils.clamp(seaLevel + seaLevelChange * delta, baseSeaLevel, 1);
//        seaLevel = .25f;
            // dont lower hill level
            hillLevel = Math.max(baseSeaLevel + seaLevel * .2f, hillLevel);
            if (seaLevelChange > 0) {
//                Gdx.app.log("", "Sea raising");
            } else {
//                Gdx.app.log("", "Sea stable");
            }
        } else {
//            Gdx.app.log("", "Sea stable");
        }
        updateSeaLevel();
    }

    public boolean isSeaLevelHigh() {
        return seaLevel >= hillLevel;
    }

    public static int TILE_TYPE_WATER = 0;
    public static int TILE_TYPE_GROUND = 1;
    public static int TILE_TYPE_HILL = 2;

    public void seaRising () {
        seaLevelChange = seaLevelRaisingChange;
    }

    public void seaLowering () {
        seaLevelChange = seaLevelLoweringChange;
    }

    public void seaStable () {
        seaLevelChange = 0;
    }

    public float seaLevel () {
        return Maths.map(seaLevel, 0, 1, ELEV_MIN, ELEV_MAX);
    }

    public static class Tile {
        public final int x, y, index;
        final float rawElevation;
        public final float elevation;
        public Color color = new Color();
        public float coal;
        public int type = TILE_TYPE_GROUND;
        public Building building;

        public Tile (int x, int y, int index, float elevation) {
            this.x = x;
            this.y = y;
            this.index = index;
            rawElevation = elevation;
            this.elevation = Maths.map(elevation, 0, 1, ELEV_MIN, ELEV_MAX);
        }

        @Override public String toString () {
            return "Tile{" + "x=" + x + ", y=" + y + '}';
        }
    }
}
