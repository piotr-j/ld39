package io.piotrjastrzebski.ld39;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.piotrjastrzebski.jam.ecs.ArtemisUtils;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.jam.ecs.processors.gameplay.CameraFollower;
import io.piotrjastrzebski.jam.ecs.processors.gameplay.CursorProcessor;
import io.piotrjastrzebski.jam.ecs.processors.rendering.DebugGridRenderer;
import io.piotrjastrzebski.jam.ecs.processors.rendering.DebugShapeRenderer;
import io.piotrjastrzebski.jam.ecs.processors.rendering.Stage;
import io.piotrjastrzebski.jam.ecs.processors.rendering.ViewBounds;

/**
 * Created by PiotrJ on 16/04/16.
 */
public class GameScreen extends ScreenAdapter {
    private final World world;
    private final ExtendViewport gameViewport;
    private final ScreenViewport guiViewport;
    private final ShapeRenderer shapes;

    public GameScreen (LD39Game game) {
        Globals.init(32, 1280, 720);

        gameViewport = new ExtendViewport(Globals.WIDTH, Globals.HEIGHT);
        guiViewport = new ScreenViewport();
        shapes = new ShapeRenderer();

        WorldConfiguration config = new WorldConfiguration();
        config.register(game.batch);
        config.register(shapes);
        config.register(Globals.WIRE_GAME_VP, gameViewport);
        config.register(Globals.WIRE_GAME_CAM, gameViewport.getCamera());
        config.register(Globals.WIRE_GUI_VP, guiViewport);
        config.register(Globals.WIRE_GUI_CAM, guiViewport.getCamera());

        config.setSystem(CursorProcessor.class);
        config.setSystem(CameraFollower.class);
        config.setSystem(ViewBounds.class);
        config.setSystem(new DebugGridRenderer(1, 1, 1, 1, .25f));

        config.setSystem(DebugShapeRenderer.class);
        config.setSystem(Stage.class);

        world = new World(config);
        Gdx.input.setInputProcessor(ArtemisUtils.registerInput(world));
    }

    @Override public void render (float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // 15fps max delta
        world.delta = Math.min(delta, 1f/15f);
        world.process();
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.CYAN);
        shapes.line(-1.5f, 0, 1.5f, 0);
        shapes.line(0, -1.5f, 0, 1.5f);
        shapes.end();
    }

    @Override public void resize (int width, int height) {
        gameViewport.update(width, height, true);
        guiViewport.update(width, height);
    }

    @Override public void dispose () {
        world.dispose();
        shapes.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
