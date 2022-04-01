package ninja.trek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ninja.trek.entity.BasicEntity;
import ninja.trek.entity.Entity;
import ninja.trek.entity.EntityEngine;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private final LudumMain game;
    private Skin skin;
    private Stage stage;
    private OrthographicCamera camera;
    private World world;
    private Vector2 gravity = new Vector2(0, -10f);
    private Box2DDebugRenderer debugRenderer;
    private EntityEngine engine;
    private SpriteBatch batch;
    private ShapeRenderer shape;

    public GameScreen(Skin skin, LudumMain game) {
        this.skin = skin;
        this.game = game;
    }

    @Override
    public void show() {
        Graphics.init();
        // Prepare your screen here.
        stage = new Stage();
        camera = new OrthographicCamera(40, 30);
        world = new World(gravity, true);
        debugRenderer = new Box2DDebugRenderer();
        engine = new EntityEngine(world);
        batch = new SpriteBatch();

        Entity testE = engine.add(BasicEntity.class, 0, 0);
        testE.body.setGravityScale(0.1f);
        testE.setAnimation(Graphics.playerWalk);

        for (int i = 0; i < 30; i++){
            //engine.add(BasicEntity.class, MathUtils.random(10), MathUtils.random(10)).body.setLinearVelocity(MathUtils.random(1f), MathUtils.random(1f));
        }
        world.setContactListener(new EntityContactListener());
        shape = new ShapeRenderer();

        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(stage);
        mux.addProcessor(new EntityInputProcessor(){
            @Override
            public boolean keyDown(int keycode) {


                return super.keyDown(keycode);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {


                return super.touchDown(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void render(float delta) {
//        Gdx.app.log("app ", "start");
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(0, 0, 0);
        camera.update();
        world.step(delta, 2, 2);
        engine.update(delta, world);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        engine.render(delta, batch);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        engine.renderLine(delta, shape);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        engine.renderFilled(delta, shape);
        shape.end();
        debugRenderer.render(world, camera.combined);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        //camera.setToOrtho(false, width, height);
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}