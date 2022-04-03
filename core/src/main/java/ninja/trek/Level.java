package ninja.trek;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

import ninja.trek.entity.EntityEngine;

public abstract class Level {
    public Color color = new Color();

    public abstract void start(World world, EntityEngine engine);


    public abstract void render(float delta, SpriteBatch batch);

    public abstract void renderFilled(float delta, ShapeRenderer shape);
}
