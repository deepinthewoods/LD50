package ninja.trek;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ninja.trek.entity.Entity;

public class EntityContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Object ua = contact.getFixtureA().getUserData();
        Object ub = contact.getFixtureB().getUserData();
        if (ua instanceof Entity && ub instanceof Entity){
            Entity a = (Entity) ua;
            a.markedForRemoval = true;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
