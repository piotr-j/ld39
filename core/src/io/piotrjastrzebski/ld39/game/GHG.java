package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.piotrjastrzebski.ld39.game.building.Buildings;
import io.piotrjastrzebski.ld39.game.utils.Maths;

public class GHG {
    private final ExtendViewport viewport;
    private final Map map;
    private final Buildings buildings;
    final static float BORDER_START = 0f;
    final static float BORDER_END = 3f;
    final static float LIGHT_START = -.5f;
    final static float LIGHT_END = 0.4f;
    final static float DARK_START = 0.2f;
    final static float DARK_END = 0.8f;
    private static final float GHG_DECAY = 0.01f;
    // 0-1
    float ghg = 0f;
    float ghgSeaRaise = .4f;
    float ghgSeaLower = .2f;
    public GHG (ExtendViewport viewport, Map map, Buildings buildings) {
        this.viewport = viewport;
        this.map = map;
        this.buildings = buildings;
        buildings.setGHG(this);
    }

    public void update (float delta) {
        addGHG(- ghg * GHG_DECAY * delta);
        light.a = MathUtils.clamp(LIGHT_START + (LIGHT_END - LIGHT_START) * ghg, 0, 1);
        dark.a = MathUtils.clamp(DARK_START + (DARK_END - DARK_START) * ghg, 0, 1);
        border = BORDER_START + (BORDER_END - BORDER_START) * ghg;

        if (ghg >= ghgSeaRaise) {
            map.seaRising();
        } else if (ghg <= ghgSeaLower){
            map.seaLowering();
        } else {
            map.seaStable();
        }
    }

    public void addGHG (float value) {
        ghg = MathUtils.clamp(ghg + value, 0, 1);
    }

    private float border = 2f;
    private Color light = new Color(0, 0, 0, .25f);
    private Color dark = new Color(0, 0, 0, .5f);
    public void drawDebug (ShapeRenderer shapes) {
        OrthographicCamera camera = (OrthographicCamera)viewport.getCamera();
        float sx = camera.position.x - camera.viewportWidth / 2 * camera.zoom;
        float sy = camera.position.y - camera.viewportHeight / 2 * camera.zoom;
        float ex = camera.position.x + camera.viewportWidth / 2 * camera.zoom;
        float ey = camera.position.y + camera.viewportHeight / 2 * camera.zoom;
        float border = this.border * camera.zoom;
        // left
        shapes.triangle(
            sx, sy,
            sx + border, sy + border,
            sx, ey,
            dark, light, dark
        );
        shapes.triangle(
            sx, ey,
            sx + border, sy + border,
            sx + border, ey - border,
            dark, light, light
        );
        // right
        shapes.triangle(
            ex - border, sy + border,
            ex - border, ey - border,
            ex, sy,
            light, light, dark
        );
        shapes.triangle(
            ex - border, ey - border,
            ex, sy,
            ex, ey,
            light, dark, dark
        );
        //top
        shapes.triangle(
            sx, ey,
            sx + border, ey - border,
            ex - border, ey - border,
            dark, light, light
        );
        shapes.triangle(
            sx, ey,
            ex - border, ey - border,
            ex, ey,
            dark, light, dark
        );
        // bottom
        shapes.triangle(
            sx, sy,
            ex, sy,
            sx + border, sy + border,
            dark, dark, light
        );
        shapes.triangle(
            sx + border, sy + border,
            ex, sy,
            ex - border, sy + border,
            light, dark, light
        );
        // center
        shapes.setColor(light);
        shapes.rect(
            sx + border, sy + border, camera.viewportWidth * camera.zoom - border * 2, camera.viewportHeight * camera.zoom - border * 2);
    }

    public float ghgLevel () {
        return Maths.map(ghg, 0, 1, 350, 600);
    }
}
