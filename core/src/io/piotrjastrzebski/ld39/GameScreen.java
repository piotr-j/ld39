package io.piotrjastrzebski.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.ld39.game.CameraController;
import io.piotrjastrzebski.ld39.game.Entity;
import io.piotrjastrzebski.ld39.game.Map;

import java.util.Comparator;

/**
 * Created by PiotrJ on 16/04/16.
 */
public class GameScreen extends ScreenAdapter {
    private final ExtendViewport gameViewport;
    private final ScreenViewport guiViewport;
    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private Array<Entity> entities = new Array<>();
    private CameraController cameraController;

    private Map map;

    public GameScreen (LD39Game game) {
        batch = game.batch;
        Globals.init(32, 1280, 720);

        gameViewport = new ExtendViewport(Globals.WIDTH, Globals.HEIGHT);
        guiViewport = new ScreenViewport();
        shapes = new ShapeRenderer();

        entity(1, 0, 0, 5, 45, Color.CYAN);
        entity(0,1, 1, 6, 30, Color.RED);
        entity(2,-2, 4, 4, 15, Color.GREEN);
        entity(0,2, 0, 3, 90, Color.BLUE);
        entity(3,3, -2, 2, 120, Color.BLUE);
        entity(0,5, 1, 1, 75, Color.BLUE);
        entity(5,-4, 5, .5f, 90 + 15, Color.BLUE);

        map = new Map();
        gameViewport.getCamera().position.set(map.width/2, map.height/2, 0);
        gameViewport.getCamera().update();

        InputMultiplexer multiplexer = new InputMultiplexer();
        cameraController = new CameraController(map, gameViewport);
        multiplexer.addProcessor(cameraController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private Entity entity (int layer, float x, float y, float radius, float rotation, Color color) {
        Entity entity = new Entity();
        entity.layer = layer;
        entity.setBounds(x, y, radius);
        entity.rotation = rotation;
        entity.color.set(color);
        entities.add(entity);
        return entity;
    }

    private Entity over;
    private Entity selected;
    private Vector2 tp = new Vector2();
    @Override public void render (float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameViewport.getCamera().update();
        tp.set(Gdx.input.getX(), Gdx.input.getY());
        gameViewport.unproject(tp);

        // cap delta so it never goes too low
        delta = Math.min(delta, 1f/15f);
        map.update(tp, delta);
        entities.sort(new Comparator<Entity>() {
            @Override public int compare (Entity o1, Entity o2) {
                return o2.layer - o1.layer;
            }
        });

        Entity nextOver = null;
        Entity nextSelected = selected;
        for (Entity entity : entities) {
            entity.update(delta);
            if (nextOver == null) {
                if (entity.bounds.contains(tp)) {
                    nextOver = entity;
                }
            }
            /*
            if (nextOver != null) continue;
            if (entity.bounds.contains(tp)) {
                if (over != null && over != entity) {
                    over.exit(tp);
                }
                nextOver = entity;
                if (!entity.over) {
                    entity.enter(tp);
                }
                entity.over(tp);
            } else {
                if (entity.over) {
                    entity.exit(tp);
                }
            }
            */
        }
        boolean m1 = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean m2 = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);



        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        batch.enableBlending();
        batch.begin();
        for (Entity entity : entities) {
            entity.draw(batch);
        }
        batch.end();

        shapes.setProjectionMatrix(gameViewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        map.drawDebug(shapes);
        shapes.end();

        shapes.setProjectionMatrix(gameViewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Line);
        for (Entity entity : entities) {
            entity.drawDebug(shapes);
        }
        shapes.end();
    }

    @Override public void resize (int width, int height) {
        gameViewport.update(width, height, false);
        guiViewport.update(width, height);
    }

    @Override public void dispose () {
        shapes.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
