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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.ld39.game.CameraController;
import io.piotrjastrzebski.ld39.game.Entity;
import io.piotrjastrzebski.ld39.game.Map;
import io.piotrjastrzebski.ld39.game.building.Building;
import io.piotrjastrzebski.ld39.game.building.Buildings;

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
    private Table root;
    private Stage stage;

    private Map map;
    private Buildings buildings;

    public GameScreen (LD39Game game) {
        batch = game.batch;
        Globals.init(32, 1280, 720);

        gameViewport = new ExtendViewport(Globals.WIDTH, Globals.HEIGHT);
        guiViewport = new ScreenViewport();
        shapes = new ShapeRenderer();
        stage = new Stage(guiViewport, batch);
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        entity(1, 0, 0, 5, 45, Color.CYAN);
        entity(0,1, 1, 6, 30, Color.RED);
        entity(2,-2, 4, 4, 15, Color.GREEN);
        entity(0,2, 0, 3, 90, Color.BLUE);
        entity(3,3, -2, 2, 120, Color.BLUE);
        entity(0,5, 1, 1, 75, Color.BLUE);
        entity(5,-4, 5, .5f, 90 + 15, Color.BLUE);

        map = new Map();
        buildings = new Buildings(gameViewport, map);

        InputMultiplexer multiplexer = new InputMultiplexer();
        cameraController = new CameraController(map, gameViewport);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(cameraController);
        multiplexer.addProcessor(buildings);
        Gdx.input.setInputProcessor(multiplexer);

        {
            Table buildMenu = new Table();
            root.add(buildMenu).expand().bottom();
            for (final Building building : buildings.templates()) {
                VisTextButton build = new VisTextButton(building.name);
                buildMenu.add(build).pad(4);
                build.addListener(new ClickListener(){
                    @Override public void clicked (InputEvent event, float x, float y) {
                        Gdx.app.log("build", building.name);
                        buildings.build(building);
                    }
                });
            }
            VisTextButton demolish = new VisTextButton("Demolish");
            demolish.setColor(Color.RED);
            buildMenu.add(demolish).pad(4);
            demolish.addListener(new ClickListener(){
                @Override public void clicked (InputEvent event, float x, float y) {
                    buildings.demolish();
                }
            });
        }
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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        gameViewport.getCamera().update();
        tp.set(Gdx.input.getX(), Gdx.input.getY());
        gameViewport.unproject(tp);

        // cap delta so it never goes too low
        delta = Math.min(delta, 1f/15f);
        map.update(tp, delta);
        buildings.update(delta);
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

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.setProjectionMatrix(gameViewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        map.drawDebug(shapes);
        buildings.drawDebug(shapes);
        shapes.end();

        shapes.setProjectionMatrix(gameViewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Line);
        for (Entity entity : entities) {
            entity.drawDebug(shapes);
        }
        shapes.end();

        stage.act(delta);
        stage.draw();
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
