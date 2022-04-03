package ninja.trek.entity;

import static ninja.trek.entity.Player.BOW;
import static ninja.trek.entity.Player.SWORD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ninja.trek.Graphics;

public class Villager extends FloorContactEntity implements Hittable{
    private static final float JUMP_IMPULSE = 7f;
    private static final float WALK_ACCELERATION = .3f;
    private static final float WALK_SPEED_LIMIT = 7f;
    private static final String TAG = "vill";
    private static final float ARROW_SPEED = 20f;
    private static final float JUMP_HOLD_INTERVAL = 1f, JUMP_WAIT_INTERVAL = 5f;
    private static final float ARROW_DISTANCE = 14;
    private static final float SHOOT_TIME = 1f;
    static BodyDef bodyD;
    static FixtureDef fixD;
    static PolygonShape shape;
    static float h = 2, w = .5f;
    static Vector2[] verts = {
            new Vector2(-w, h),
            new Vector2(w, h),
            new Vector2(-w, 0),
            new Vector2(w, 0)
    };
    static Vector2[] sensorVerts = {
            new Vector2(-w, .2f),
            new Vector2(w, .2f),
            new Vector2(-w, -.2f),
            new Vector2(w, -.2f)
    };
    static Vector2 arrowStart = new Vector2(0.25f, 1.5f);

    private static final FixtureDef sensorD;

    private static final PolygonShape sensorShape;

    static {
        bodyD = new BodyDef();
        fixD = new FixtureDef();
        shape = new PolygonShape();
        shape.set(verts);
        fixD.shape = shape;
        fixD.friction = 0.1f;
        bodyD.type = BodyDef.BodyType.DynamicBody;
        bodyD.allowSleep = false;
        sensorD = new FixtureDef();
        sensorShape = new PolygonShape();
        sensorShape.set(sensorVerts);
        sensorD.shape = sensorShape;
        sensorD.isSensor = true;
        fixD.filter.categoryBits = ENEMY;
        fixD.filter.maskBits = (PLAYER_WEAPON + FLOOR + PLAYER);
    }

    public Vector2 touch = new Vector2(), touchAngle = new Vector2();
    public int selectedItem;

    public Animation<Sprite> itemAnim;
    private Fixture sensorFix;


    @Override
    public void onAdd(World world, float x, float y){
        body = world.createBody(bodyD);
        fix = body.createFixture(fixD);
        fix.setUserData(this);
        sensorFix = body.createFixture(sensorD);
        sensorFix.setUserData(this);

        body.setTransform(x, y, 0);
        position.set(body.getPosition());
        setAnimation(Graphics.playerWalk);

    }
    public Villager(){
        super();
    }

    float timeTarget = MathUtils.random(2f);
    boolean move[] = new boolean[4];
    private boolean touched, hasAimWeapon;
    public boolean isJumping;
    float jumpTime, groundTime, aiJumpTimer, aiShootTimer, shootTime;
    private boolean aiIsJumping;
    public static int M_UP = 0, M_DOWN = 1, M_LEFT = 2, M_RIGHT = 3;
    public Vector2 vel = new Vector2();
    public Entity player;
    @Override
    public void update(float delta, World world, EntityEngine engine) {
        super.update(delta, world, engine);
        ;
        if (hp <= 0) return;
        if (groundContacts > 0) groundTime = 0f;
        groundTime += delta;
        shootTime += delta;
        onGround = groundTime < .1f;
//        if (onGround)
//            Gdx.app.log("entity ", "update g " + hp);

        move[M_UP] = false;//Gdx.input.isKeyPressed(Input.Keys.W);
        move[M_DOWN] = false;//Gdx.input.isKeyPressed(Input.Keys.S);
        move[M_LEFT] = false;//Gdx.input.isKeyPressed(Input.Keys.A);
        move[M_RIGHT] = false;//Gdx.input.isKeyPressed(Input.Keys.D);
        if (selectedItem == SWORD){//CHARGE
            if (player.position.x > position.x)
                move[M_RIGHT] = true;
            else move[M_LEFT] = true;
        }
        if (selectedItem == BOW){//maintain distance
            float dist = Math.abs(player.position.x - position.x);
            boolean towards = true, shouldMove = true;
            if (dist < ARROW_DISTANCE) towards = false;
            else if (dist > ARROW_DISTANCE + 4) towards = true;
            else shouldMove = false;
            if (shouldMove){
                if (player.position.x > position.x )
                    move[towards?M_RIGHT:M_LEFT] = true;
                else move[towards?M_LEFT:M_RIGHT] = true;
                touched = false;
            } else {//face player
                flip =  (player.position.x > position.x);
                touched = true;
            }
            touchAngle.set(player.position.x, player.position.y + .5f).sub(position).nor();
            if (shootTime > SHOOT_TIME){
                shootTime = 0f;
                touchAngle.y += MathUtils.random(.3f);
                touchAngle.nor();
                touchUp(engine);
            }
        }


        aiJumpTimer -= delta;
//        Gdx.app.log(TAG, "jump timer" + aiJumpTimer);
        if (aiIsJumping){
            if (aiJumpTimer < 0){
                aiIsJumping = false;
                aiJumpTimer = MathUtils.random(JUMP_WAIT_INTERVAL+2);
//                Gdx.app.log(TAG, "change jump_"+aiJumpTimer);
            }
            move[M_UP] = true;
        } else {
            if (aiJumpTimer < 0){
//               Gdx.app.log(TAG, "change jump");
                aiIsJumping = true;
                aiJumpTimer = MathUtils.random(JUMP_HOLD_INTERVAL+2.5f);
            }


        }

        float walkDir = 0f;
        if (move[M_RIGHT]){
            walkDir += 1f;
            flip = true;
        }
        if (move[M_LEFT]){
            walkDir -= 1f;
            flip = false;
        }
        if ((move[M_RIGHT] && move[M_LEFT]) || (!move[M_RIGHT] && !move[M_LEFT]))
            isWalking = false;
        else isWalking = true;

        if (isWalking != wasWalking){
            if (isWalking){
               setAnimation();
//                fix.setFriction(0f);

            } else {
                setAnimation();

                if (onGround){
                    body.setLinearVelocity(0, body.linVelWorld.y);
                }
//                fix.setFriction(1f);
            }
        }

        if (!isJumping && onGround && move[M_UP]){
            isJumping = true;
//            onGround = false;
            //TODO jump animation
            jumpTime = 0f;
            body.applyLinearImpulse(0f, JUMP_IMPULSE, position.x, position.y, true);
//            Gdx.app.log(TAG, "jump");
        }
        if (isJumping && !move[M_UP]){
            vel.set(body.getLinearVelocity());

            isJumping = false;
            if (vel.y > 0){
//                Gdx.app.log(TAG, "stop jump");
                body.setLinearVelocity(vel.x, 0f);
            }
        }
        if ((move[M_LEFT] && body.getLinearVelocity().x > -WALK_SPEED_LIMIT) || (move[M_RIGHT] && body.getLinearVelocity().x < WALK_SPEED_LIMIT)){
            if (onGround)
                body.applyLinearImpulse(walkDir * normal.x, walkDir * normal.y, position.x, position.y, true);
            else
                body.applyLinearImpulse(walkDir * WALK_ACCELERATION, 0f, position.x, position.y, true);

        }
//            body.setLinearVelocity(walkDir * WALK_SPEED_LIMIT, body.linVelWorld.y);


        //if (onGround) Gdx.app.log(TAG, "onground");

//        if (time > timeTarget ) markedForRemoval = true;
        wasWalking = isWalking;
    }

    @Override
    public void reset() {
        super.reset();
        isJumping = false;
        onGround = false;
        groundContacts = 0;
        aiJumpTimer = 0f;
        hp = 1;
    }

    public void touchUp(EntityEngine engine) {
        touched = false;
//        setAnimation();
        if (selectedItem == BOW){
            Arrow arrow = engine.add(Arrow.class, position.x + arrowStart.x * (flip ? 1 : -1),
                    position.y + arrowStart.y);
            arrow.body.setGravityScale(.3f);
            arrow.body.setLinearVelocity(touchAngle.x * ARROW_SPEED, touchAngle.y * ARROW_SPEED);
            arrow.fix.getFilterData().categoryBits = ENEMY_WEAPON;
            arrow.fix.getFilterData().maskBits = ( PLAYER + FLOOR);

        }
    }

    public void touchDown() {
        touched = true;
        setAnimation();
    }

    public void setSelectedItem(int item) {
        selectedItem = item;
        if (selectedItem == BOW) {
            hasAimWeapon = true;
            //Gdx.app.log(TAG, "has aim weapon, bow");
        }
        else hasAimWeapon = false;
        setAnimation();
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        super.render(delta, batch);
        Animation<Sprite> anim = itemAnim;
        if (anim == null) return;
        Sprite s = anim.getKeyFrame(animTime);
        s.setPosition(position.x - SPRITE_OFFSET, position.y - SPRITE_OFFSET);
        s.setScale(flip?-1:1, 1);
        s.draw(batch);

    }

    @Override
    public void renderLine(float delta, ShapeRenderer shape) {
        if (hp <= 0) return;
        //shape.line(position.x, position.y, position.x + normal.x, position.y + normal.y);
        if (selectedItem == BOW && touched) shape.line(position.x + arrowStart.x * (flip?1:-1), position.y + arrowStart.y, position.x + arrowStart.x * (flip?1:-1) + touchAngle.x, position.y + arrowStart.y + touchAngle.y);
    }

    public void setAnimation(){
        if (hp == 0) throw new GdxRuntimeException("dead");
        animTime = 0f;
        if (touched && hasAimWeapon){
            //Gdx.app.log(TAG, "touched + aim");
            if (isWalking){
                setAnimation(Graphics.villagerWalkAim);
                itemAnim = Graphics.itemWalkAim[selectedItem];
            }
            else {
                setAnimation(Graphics.villagerStandAim);
                itemAnim = Graphics.itemStandAim[selectedItem];
            }
            return;
        }
        if (isWalking){
            setAnimation(Graphics.villagerWalk);
            itemAnim = Graphics.itemWalk[selectedItem];
        }
        else{
            setAnimation(Graphics.villagerStand);
            itemAnim = Graphics.itemStand[selectedItem];
        }
    };

    public void setTouch(float x, float y) {
        touch.set(x, y);
        touchAngle.set(x, y).sub(position).sub(arrowStart).nor();
    }


    @Override
    public void hit(int points) {
        hp -= points;
        Gdx.app.log("entity ", "hit " + hp);
        if (hp == 0){
            setAnimation(Graphics.villagerDie);
            itemAnim = Graphics.itemDie[selectedItem];
        }
    }
}
