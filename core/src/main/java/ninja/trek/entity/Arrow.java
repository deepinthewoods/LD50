package ninja.trek.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Arrow extends Entity {
    static BodyDef bodyD;
    static FixtureDef fixD;
    static CircleShape shape;
    static float r = .25f;




    static {
        bodyD = new BodyDef();
        fixD = new FixtureDef();
        shape = new CircleShape();
        shape.setRadius(r);
        //shape.set(verts);
        fixD.shape = shape;
        bodyD.type = BodyDef.BodyType.DynamicBody;
        fixD.filter.categoryBits = 0;
    }
    @Override
    public void onAdd(World world, float x, float y){
        body = world.createBody(bodyD);
        fix = body.createFixture(fixD);
        fix.setUserData(this);
        body.setTransform(x, y, 0);
        position.set(body.getPosition());
    }
    public Arrow(){
        super();
    }

    float timeTarget = MathUtils.random(2f);
    @Override
    public void update(float delta, World world, EntityEngine entityEngine) {
        super.update(delta, world, entityEngine);
        //Gdx.app.log("entity ", "update " + time);
//        if (time > timeTarget ) markedForRemoval = true;
    }

    static Vector2 end = new Vector2();
    @Override
    public void renderLine(float delta, ShapeRenderer shape) {
        end.set(body.getLinearVelocity()).nor().add(position);
        shape.line(position, end);
        super.renderLine(delta, shape);
    }
}
