package ninja.trek.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import ninja.trek.Level;

public abstract class Entity implements Pool.Poolable {
    public static final short PLAYER_WEAPON = 1, PLAYER = 2, ENEMY_WEAPON = 4, ENEMY = 8, FLOOR = 16;
    public static final float SPRITE_SIZE = 12.8f, SPRITE_OFFSET = SPRITE_SIZE/2f;
    public boolean markedForRemoval;
    public Body body;
    public Fixture fix;
    public Vector2 position = new Vector2();
    public float time, animTime;
    public Entity(){
        reset();
    };
    private Animation<Sprite> anim;
    protected boolean flip;
    public int hp;
    @Override
    public void reset() {
        markedForRemoval = false;
        body = null;
        position.set(0, 0);
        time = 0f;
        animTime = 0f;
    }

    public void update(float delta, World world, EntityEngine entityEngine) {
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
        s.setScale(flip?-1:1, 1);
        s.draw(batch);
    }

    public void onRemove(World world) {
        if (body != null){
            world.destroyBody(body);
            body = null;
            fix = null;
        }
    }
    public abstract void onAdd(World world, float x, float y);

    public void renderLine(float delta, ShapeRenderer shape){};

    public void renderFilled(float delta, ShapeRenderer shape, Level level) {}

    public void setAnimation(Animation<Sprite> anim) {
        this.anim = anim;
        animTime = 0f;
    }
}
