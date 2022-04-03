package ninja.trek;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

import ninja.trek.entity.EntityEngine;
import ninja.trek.entity.Floor;

public class StartLevel extends Level{

    public StartLevel(){
        color.set(.5f, .5f, .5f, 1f);
    }
    @Override
    public void start(World world, EntityEngine engine) {
        engine.add(Floor.class, 0, 0);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {

    }

    @Override
    public void renderFilled(float delta, ShapeRenderer shape) {

    }
}
