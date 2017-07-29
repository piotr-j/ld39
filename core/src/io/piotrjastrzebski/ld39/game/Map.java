package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Map {
    private Tile[] tiles;
    public final int width;
    public final int height;
    public Map () {
        Pixmap raw = new Pixmap(Gdx.files.internal("map.png"));
        width = raw.getWidth();
        height = raw.getHeight();
        tiles = new Tile[width * height];
        Color out = new Color();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = raw.getPixel(x, height - y - 1);
                Color.rgba8888ToColor(out, pixel);
                // simple greyscale
                float elevation = (out.r + out.g + out.b)/3;
                Tile tile = new Tile(x, y, index(x, y), elevation);
                tiles[tile.index] = tile;
                if (elevation <= .25f) {
                    if (elevation <= .15f) {
                        tile.color.set(Color.NAVY);
                    } else {
                        tile.color.set(Color.ROYAL);
                    }
                } else if (elevation > .65f) {
                    tile.color.set(.6f, .6f, .6f, 1);
                    if (elevation > .85f) {
                        tile.color.set(.8f, .8f, .8f, 1);
                    }
                } else {
                    tile.color.set(Color.FOREST);
                }
            }
        }
    }

    public Tile getTile (int x, int y) {
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
        }
    }

    private Vector2 sp = new Vector2();
    public void update (Vector2 tp, float delta) {

    }

    public static class Tile {
        public final int x, y, index;
        public final float elevation;
        public Color color = new Color();

        public Tile (int x, int y, int index, float elevation) {
            this.x = x;
            this.y = y;
            this.index = index;
            this.elevation = elevation;
        }
    }
}
