package io.piotrjastrzebski.ld39.game.utils;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class IntRect {
    public int x, y;
    public int width, height;

    public IntRect () {}

    public IntRect (int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public IntRect (IntRect rect) {
        x = rect.x;
        y = rect.y;
        width = rect.width;
        height = rect.height;
    }

    public IntRect set (int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public IntRect position (int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public IntRect position (float x, float y) {
        this.x = (int)x;
        this.y = (int)y;
        return this;
    }

    public boolean contains (float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    public boolean contains (Vector2 point) {
        return contains(point.x, point.y);
    }

    public boolean contains (Circle circle) {
        return (circle.x - circle.radius >= x) && (circle.x + circle.radius <= x + width)
            && (circle.y - circle.radius >= y) && (circle.y + circle.radius <= y + height);
    }

    public boolean contains (Rectangle rectangle) {
        float xmin = rectangle.x;
        float xmax = xmin + rectangle.width;

        float ymin = rectangle.y;
        float ymax = ymin + rectangle.height;

        return ((xmin > x && xmin < x + width) && (xmax > x && xmax < x + width))
            && ((ymin > y && ymin < y + height) && (ymax > y && ymax < y + height));
    }

    public boolean contains (IntRect rectangle) {
        float xmin = rectangle.x;
        float xmax = xmin + rectangle.width;

        float ymin = rectangle.y;
        float ymax = ymin + rectangle.height;

        return ((xmin > x && xmin < x + width) && (xmax > x && xmax < x + width))
            && ((ymin > y && ymin < y + height) && (ymax > y && ymax < y + height));
    }

    public boolean overlaps (Rectangle r) {
        return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
    }

    public boolean overlaps (IntRect r) {
        return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
    }
}
