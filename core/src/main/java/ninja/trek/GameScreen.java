package ninja.trek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ninja.trek.entity.BasicEntity;
import ninja.trek.entity.Entity;
import ninja.trek.entity.EntityEngine;
import ninja.trek.entity.Floor;
import ninja.trek.entity.Player;
import ninja.trek.entity.Villager;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private static final String TAG = "Game";
    private final LudumMain game;
    private Skin skin;
    private Stage stage;
    private OrthographicCamera camera;
    private World world;
    private Vector2 gravity = new Vector2(0, -20f);
    private Box2DDebugRenderer debugRenderer;
    private EntityEngine engine;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private Player player;
    private Level currentLevel;
    private StartLevel startLevel = new StartLevel();

    public GameScreen(Skin skin, LudumMain game) {
        this.skin = skin;
        this.game = game;
    }

    @Override
    public void show() {
        Graphics.init();
        // Prepare your screen here.
        stage = new Stage();
        camera = new OrthographicCamera(20*1, 15*1);
        camera.zoom = 2f;
        world = new World(gravity, true);
        debugRenderer = new Box2DDebugRenderer();
        engine = new EntityEngine(world);
        batch = new SpriteBatch();

//        Entity testE = engine.add(BasicEntity.class, 0, 0);
//        testE.body.setGravityScale(0.1f);
//        testE.setAnimation(Graphics.playerWalk);

        player = engine.add(Player.class, 3, 0);

        Villager vill = engine.add(Villager.class, 13, 0);
        vill.player = player;
        vill.setSelectedItem(Player.BOW);
//        engine.add(Villager.class, 23, 0).player = player;

        initLevel(startLevel);

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
            Vector3 v = new Vector3();

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                v.set(screenX, screenY, 0);
                camera.unproject(v);
                player.setTouch(v.x, v.y);
                player.touchDown();
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                v.set(screenX, screenY, 0);
                camera.unproject(v);
                player.setTouch(v.x, v.y);
                return super.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                v.set(screenX, screenY, 0);
                camera.unproject(v);

                player.setTouch(v.x, v.y);
                player.touchUp(engine);
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
        Gdx.input.setInputProcessor(mux);
        initStage();
    }

    private void initLevel(StartLevel level) {
        currentLevel = level;
        level.start(world, engine);
    }

    private void initStage() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Table topTable = new Table();
        table.add(topTable);
        table.add(new Table()).expandX().fillX().row();
        table.add(new Table()).expandY().fillY();

        TextButton fistBtn = new TextButton("fist", skin);
        topTable.add(fistBtn).top().left();
        TextButton swordBtn = new TextButton("sword", skin);
        topTable.add(swordBtn).top().left();
        TextButton bowBtn = new TextButton("bow", skin);
        topTable.add(bowBtn).top().left();

        swordBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                player.setSelectedItem(Player.SWORD);
            }
        });
        bowBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                player.setSelectedItem(Player.BOW);
            }
        });
        fistBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                player.setSelectedItem(Player.FIST);
            }
        });
//        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
//        Gdx.app.log("app ", "start");

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        camera.position.set(0, 0, 0);
//        camera.update();
        world.step(delta, 2, 2);
        engine.update(delta, world);
        camera.position.set(player.position.x, player.position.y, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        currentLevel.render(delta, batch);
        engine.render(delta, batch);
        batch.end();

        shape.setProjectionMatrix(camera.combined);
        shape.setColor(Color.RED);
        shape.begin(ShapeRenderer.ShapeType.Line);
        engine.renderLine(delta, shape);
        shape.end();
        shape.setColor(currentLevel.color);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        engine.renderFilled(delta, shape, currentLevel);
        currentLevel.renderFilled(delta, shape);
        shape.end();
//        debugRenderer.render(world, camera.combined);
        stage.draw();

        BitmapFont font = skin.get("font", BitmapFont.class);
        String playerState = "";
        if (player.onGround) playerState += "ground ";
        if (player.isJumping) playerState += "jump ";
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
//        font.draw(batch, playerState, 1, 20, 100, 0, false);
        batch.end();
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