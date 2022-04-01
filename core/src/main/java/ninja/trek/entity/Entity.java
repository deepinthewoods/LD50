package ninja.trek.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

public abstract class Entity implements Pool.Poolable {
    public static final float SPRITE_SIZE = 25.6f, SPRITE_OFFSET = SPRITE_SIZE/2f;
    public boolean markedForRemoval;
    public Body body;
    public Vector2 position = new Vector2();
    public float time, animTime;
    public Entity(){
        reset();
    };
    private Animation<Sprite> anim;

    @Override
    public void reset() {
        markedForRemoval = false;
        body = null;
        position.set(0, 0);
        time = 0f;
        animTime = 0f;
    }

    public void update(float delta, World world) {
        if (body != null){
            position.set(body.getPosition());
        }
        time += delta;
        animTime += delta;
    }

    public void render(float delta, SpriteBatch batch) {
        if (anim == null) return;
        Sprite s = anim.getKeyFrame(animTime);
        s.setPosition(position.x - SPRITE_OFFSET, position.y - SPRITE_OFFSET);
        s.draw(batch);
    }

    public void onRemove(World world) {
        if (body != null){
            world.destroyBody(body);
            body = null;
        }
    }
    public abstract void onAdd(World world, float x, float y);

    public void renderLine(float delta, ShapeRenderer shape){};

    public void renderFilled(float delta, ShapeRenderer shape) {}

    public void setAnimation(Animation<Sprite> anim) {
        this.anim = anim;
        animTime = 0f;
    }
}
