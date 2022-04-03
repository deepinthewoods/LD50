package ninja.trek;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ninja.trek.entity.Arrow;
import ninja.trek.entity.Entity;
import ninja.trek.entity.Floor;
import ninja.trek.entity.FloorContactEntity;
import ninja.trek.entity.Hittable;

public class EntityContactListener implements ContactListener {
    private static final String TAG = "listener";

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Object ua = fa.getUserData();
        Object ub = fb.getUserData();

        if (fa.isSensor() && ua instanceof FloorContactEntity && ub instanceof Floor){
            FloorContactEntity a = ((FloorContactEntity) ua);
            a.groundContacts++;
        } else if (fb.isSensor() && ub instanceof FloorContactEntity && ua instanceof Floor){
            FloorContactEntity b = ((FloorContactEntity) ub);
            b.groundContacts++;
        }

        if (!fa.isSensor() && ua instanceof FloorContactEntity && ub instanceof Floor){
            FloorContactEntity a = ((FloorContactEntity) ua);
            a.normal.set(contact.getWorldManifold().getNormal()).rotate90(-1);
            if (!a.isWalking) a.body.setLinearVelocity(0, a.body.linVelWorld.y);
        } else if (!fb.isSensor() && ub instanceof FloorContactEntity && ua instanceof Floor){
            FloorContactEntity b = ((FloorContactEntity) ub);
            b.normal.set(contact.getWorldManifold().getNormal()).rotate90(-1);
            if (!b.isWalking) b.body.setLinearVelocity(0, b.body.linVelWorld.y);
        }

        if (ua instanceof Arrow && ub instanceof Floor){
            ((Arrow) ua).markedForRemoval = true;
        } else if (ub instanceof  Arrow && ua instanceof Floor){
            ((Arrow) ub).markedForRemoval = true;
        }



        if ((ua instanceof Arrow ) && ub instanceof Hittable){
            ((Entity) ua).markedForRemoval = true;
            ((Hittable) ub).hit(1);
        } else if ((ub instanceof Arrow ) && ua instanceof Hittable){
            ((Entity) ub).markedForRemoval = true;
            ((Hittable) ua).hit(1);
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Object ua = fa.getUserData();
        Object ub = fb.getUserData();

        if (fa.isSensor() && ua instanceof FloorContactEntity && ub instanceof Floor){
            FloorContactEntity a = ((FloorContactEntity) ua);
            a.groundContacts--;;
        } else if (fb.isSensor() && ub instanceof FloorContactEntity && ua instanceof Floor){
            FloorContactEntity b = ((FloorContactEntity) ub);
            b.groundContacts--;
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
