package ninja.trek.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class FloorContactEntity extends Entity{
    public int groundContacts;
    public boolean onGround;
    public Vector2 normal = new Vector2();
    public boolean isWalking, wasWalking;

}
