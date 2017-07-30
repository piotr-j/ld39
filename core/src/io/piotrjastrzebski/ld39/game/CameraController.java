package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraController implements InputProcessor {
    private final static float MIN_ZOOM = .5f;
    private final static float MAX_ZOOM = 3f;
    private final int MOVE_BUTTON = Input.Buttons.LEFT;

    private Map map;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Rectangle bounds = new Rectangle();

    public CameraController (Map map, Viewport viewport) {
        this.map = map;
        this.viewport = viewport;
        this.camera = (OrthographicCamera)viewport.getCamera();
//        bounds.set(-map.width/2f, -map.height/2f, map.width * 2, map.height * 2);
        bounds.set(0, 0, map.width, map.height);
        camera.position.set(bounds.width/2, bounds.height/2, 0);
    }

    private Vector2 sp = new Vector2();
    private Vector2 tmp = new Vector2();
    private boolean moving = false;
    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        viewport.unproject(tmp.set(screenX, screenY));
        if (button == MOVE_BUTTON) {
            moving = true;
            sp.set(tmp);
        }
        return false;
    }

    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        viewport.unproject(tmp.set(screenX, screenY));
        if (moving) {
            camera.position.add(sp.x - tmp.x, sp.y- tmp.y, 0);
            if (camera.position.x < bounds.x) camera.position.x = bounds.x;
            if (camera.position.x > bounds.x + bounds.width) camera.position.x = bounds.x + bounds.width;
            if (camera.position.y < bounds.y) camera.position.y = bounds.y;
            if (camera.position.y > bounds.y + bounds.height) camera.position.y = bounds.y + bounds.height;
        }
        return false;
    }

    @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        viewport.unproject(tmp.set(screenX, screenY));
        if (moving && button == MOVE_BUTTON) {
            moving = false;
        }
        return false;
    }

    @Override public boolean scrolled (int amount) {
        float zoom = camera.zoom + camera.zoom * amount * .1f;
        zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);
        camera.zoom = zoom;
        return false;
    }

    @Override public boolean keyDown (int keycode) {
        if (keycode == Input.Keys.SPACE) {
            camera.position.set(map.width/2, map.height/2, 0);
        }
        return false;
    }

    @Override public boolean keyUp (int keycode) {
        return false;
    }

    @Override public boolean keyTyped (char character) {
        return false;
    }

    @Override public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }
}
