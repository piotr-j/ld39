package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.piotrjastrzebski.ld39.game.Map;

import java.util.Iterator;

public class Buildings implements InputProcessor {
    private Array<Building> templates = new Array<>();
    private Array<Building> buildings = new Array<>();

    private Viewport viewport;
    private Map map;
    private GestureDetector detector;
    public Buildings (Viewport viewport, Map map) {
        this.viewport = viewport;
        this.map = map;

        templates.add(new CoalExtractor(0, 0));
        templates.add(new CoalPowerPlant(0, 0));
        templates.add(new ResearchLab(0, 0));
        templates.add(new ConveyorBelt(0, 0));
        templates.add(new ResearchLab(0, 0));
        templates.add(new SolarPowerPlant(0, 0));
        templates.add(new UtilityPole(0, 0));
        detector = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override public boolean tap (float x, float y, int count, int button) {
                if (build != null) {
                    if (button == Input.Buttons.LEFT) {
                        return finishBuilding();
                    } else {
                        cancelBuilding();
                    }
                } else if (demolish) {
                    if (button == Input.Buttons.LEFT) {
                        return executeDemolish();
                    } else {
                        cancelDemolish();
                    }
                }
                return false;
            }
        });
    }

    private boolean executeDemolish () {
        Iterator<Building> it = buildings.iterator();
        while (it.hasNext()) {
            Building next = it.next();
            if (next.bounds.contains(tmp)) {
                it.remove();
                break;
            }
        }
        return true;
    }

    private void cancelDemolish () {
        demolish = false;
    }

    private boolean finishBuilding () {
        if (build == null) return false;
        build.tint.a = 1;
        // dont overlap buildings
        for (Building building : buildings) {
            if (building.bounds.overlaps(build.bounds)) {
                return false;
            }
        }
        buildings.add(build.duplicate());
        return false;
    }

    private void cancelBuilding () {
        build = null;
    }

    public Array<Building> templates () {
        return templates;
    }

    boolean demolish;
    Building build;
    public void build (Building building) {
        demolish = false;
        build = building.duplicate();
        build.tint.a = .5f;
    }

    public void demolish () {
        demolish = true;
    }

    public void update (float delta) {
        for (Building building : buildings) {
            building.update(delta);
        }
        if (build != null) {
            build.bounds.position(tmp.x, tmp.y);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            build.rotateCCW();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            build.rotateCW();
        }
    }

    public void drawDebug (ShapeRenderer shapes) {
        for (Building building : buildings) {
            building.drawDebug(shapes);
        }
        if (build != null) {
            shapes.setColor(0, 1, 0, .7f);
            for (Building building : buildings) {
                if (building.bounds.overlaps(build.bounds)) {
                    shapes.setColor(1, 0, 0, .7f);
                    break;
                }
            }
            shapes.rect(build.bounds.x -.1f, build.bounds.y -.1f, build.bounds.width + .2f, build.bounds.height + .2f);
            build.drawDebug(shapes);
        }
        if (demolish) {
            shapes.setColor(1, 0, 0, .7f);
            float dx = .5f + (int)tmp.x;
            float dy = .5f + (int)tmp.y;
            for (Building building : buildings) {
                if (building.bounds.contains(dx, dy)) {
                    shapes.rect(building.bounds.x -.1f, building.bounds.y -.1f, building.bounds.width + .2f, building.bounds.height + .2f);
                    break;
                }
            }
            shapes.circle(dx, dy, .3f, 12);
        }
    }

    private Vector2 tmp = new Vector2();
    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        viewport.unproject(tmp.set(screenX, screenY));
        detector.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        viewport.unproject(tmp.set(screenX, screenY));
        detector.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        viewport.unproject(tmp.set(screenX, screenY));
        detector.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override public boolean mouseMoved (int screenX, int screenY) {
        viewport.unproject(tmp.set(screenX, screenY));
        return false;
    }

    @Override public boolean keyDown (int keycode) {
        return false;
    }

    @Override public boolean keyUp (int keycode) {
        return false;
    }

    @Override public boolean keyTyped (char character) {
        return false;
    }

    @Override public boolean scrolled (int amount) {
        return false;
    }
}
