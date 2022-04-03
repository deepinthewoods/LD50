package ninja.trek.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ninja.trek.Level;

public class Floor extends Entity{
    private static final String TAG = "floor";
    BodyDef bodyD;
     FixtureDef fixD;
     ChainShape shape;
     float r = 10;
    private static final int VERTS_X_COUNT = 100;
    Vector2[] verts = new Vector2[VERTS_X_COUNT + 2];
    private  float[] renderVerts;

    public Floor() {
        super();
        bodyD = new BodyDef();
        fixD = new FixtureDef();
//        shape = new PolygonShape();
        shape = new ChainShape();
        int x = 0;
        for (; x < VERTS_X_COUNT; x++){
            verts[x] = new Vector2(x*4, -3 +MathUtils.random(2f));
        }
        verts[x++] = new Vector2(VERTS_X_COUNT, -20);
        verts[x++] = new Vector2(0, -20);
        shape.createLoop(verts);
        renderVerts = new float[verts.length * 2];
        for (int i = 0; i < verts.length; i++){
            renderVerts[i*2] = verts[i].x;
            renderVerts[i*2+1] = verts[i].y;
        }

//        shape.set(verts);
        fixD.shape = shape;

        fixD.friction = 1f;
        bodyD.type = BodyDef.BodyType.StaticBody;
        fixD.filter.maskBits = Short.MAX_VALUE;
        fixD.filter.categoryBits = FLOOR;
    }
    @Override
    public void onAdd(World world, float x, float y){
        body = world.createBody(bodyD);
        fix = body.createFixture(fixD);
        fix.setUserData(this);
        body.setTransform(x, y, 0);
        position.set(body.getPosition());
    }


    float timeTarget = MathUtils.random(2f);
    @Override
    public void update(float delta, World world, EntityEngine entityEngine) {
        super.update(delta, world, entityEngine);
        //Gdx.app.log("entity ", "update " + time);
//        if (time > timeTarget ) markedForRemoval = true;
    }

    Vector2 third = new Vector2(), fourth = new Vector2(), start = new Vector2();

    @Override
    public void renderFilled(float delta, ShapeRenderer shape, Level level) {
        //shape.polygon(renderVerts);

        for (int i = 0; i < verts.length-3; i++){
            if (verts[i].y > verts[i+1].y) {
                third.set(verts[i].x, verts[i+1].y);
                fourth.set(verts[i+1]);
            }
            else {
                third.set(verts[i+1].x, verts[i].y);
                fourth.set(verts[i]);
            }
//            if (i %2 == 0) continue;
            float w = verts[i+1].x - verts[i].x;
            float h = 100;
            start.set((verts[i].x), third.y -h);
//            Gdx.app.log(TAG, "rect " + start + w + ", " + h);
            shape.triangle(verts[i].x, verts[i].y, verts[i+1].x, verts[i+1].y, third.x, third.y, level.color, level.color, level.color);
            shape.rect(start.x, start.y, w, h);
        }
//            shape.line(verts[i], verts[i+1]);
//        shape.line(0, 0, 10, 10);

    }
}
