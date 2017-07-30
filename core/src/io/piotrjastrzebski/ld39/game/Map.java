package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    public Map () {
        Pixmap terrain = new Pixmap(Gdx.files.internal("map.png"));
        Pixmap coal = new Pixmap(Gdx.files.internal("coal.png"));
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
                if (elevation <= .25f) {
                    tile.type = TILE_TYPE_WATER;
                    if (elevation <= .15f) {
                        tile.color.set(Color.NAVY);
                    } else {
                        tile.color.set(Color.ROYAL);
                    }
                } else if (elevation > .65f) {
                    tile.type = TILE_TYPE_HILL;
                    tile.color.set(.6f, .6f, .6f, 1);
                    if (elevation > .85f) {
                        tile.color.set(.8f, .8f, .8f, 1);
                    }
                } else {
                    tile.type = TILE_TYPE_GROUND;
                    tile.color.set(Color.FOREST);
                }
                Color.rgba8888ToColor(out, coal.getPixel(x, height - y - 1));
                tile.coal = out.a * COAL_MAX;
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
            if (coalOverlay) {
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
    }

    public static int TILE_TYPE_WATER = 0;
    public static int TILE_TYPE_GROUND = 1;
    public static int TILE_TYPE_HILL = 2;
    public static class Tile {
        public final int x, y, index;
        public final float elevation;
        public Color color = new Color();
        public float coal;
        public int type = TILE_TYPE_GROUND;
        public Building building;

        public Tile (int x, int y, int index, float elevation) {
            this.x = x;
            this.y = y;
            this.index = index;
            this.elevation = Maths.map(elevation, 0, 1, ELEV_MIN, ELEV_MAX);
        }

        @Override public String toString () {
            return "Tile{" + "x=" + x + ", y=" + y + '}';
        }
    }
}
