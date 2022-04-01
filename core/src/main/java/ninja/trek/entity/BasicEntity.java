package ninja.trek.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BasicEntity extends Entity{
    static BodyDef bodyD;
    static FixtureDef fixD;
    static PolygonShape shape;
    static float r = 1;
    static Vector2[] verts = {
            new Vector2(-r, r),
            new Vector2(r, r),
            new Vector2(-r, -r),
            new Vector2(r, -r)
    };

    static {
        bodyD = new BodyDef();
        fixD = new FixtureDef();
        shape = new PolygonShape();
        shape.set(verts);
        fixD.shape = shape;
        bodyD.type = BodyDef.BodyType.DynamicBody;
    }
    @Override
    public void onAdd(World world, float x, float y){
        body = world.createBody(bodyD);
        body.createFixture(fixD).setUserData(this);
        body.setTransform(x, y, 0);
        position.set(body.getPosition());
    }
    public BasicEntity(){
        super();
    }

    float timeTarget = MathUtils.random(2f);
    @Override
    public void update(float delta, World world) {
        super.update(delta, world);
        Gdx.app.log("entity ", "update " + time);
//        if (time > timeTarget ) markedForRemoval = true;
    }
}
