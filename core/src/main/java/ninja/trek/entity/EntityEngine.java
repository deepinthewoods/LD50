package ninja.trek.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class EntityEngine {
    private final World world;
    public Array<Entity> entities = new Array<Entity>();

    public EntityEngine(World world) {
        this.world = world;
    }

    public void update(float delta, World world) {
        Array.ArrayIterator<Entity> i = entities.iterator();
        while (i.hasNext()){
            Entity e = i.next();
            e.update(delta, world);
            if (e.markedForRemoval){
                i.remove();
                e.onRemove(world);
                Pools.free(e);
            }
        }
    }

    public void render(float delta, SpriteBatch batch) {
        for (Entity e : entities){
            e.render(delta, batch);
        }
    }

    public <T extends Entity> T add(Class<T> t, float x, float y){
        T e = Pools.obtain(t);
        e.onAdd(world, x, y);
        entities.add(e);
        return e;
    }

    public void renderLine(float delta, ShapeRenderer shape) {
        for (Entity e : entities){
            e.renderLine(delta, shape);
        }
    }

    public void renderFilled(float delta, ShapeRenderer shape) {
        for (Entity e : entities){
            e.renderFilled(delta, shape);
        }
    }
}
