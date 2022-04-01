package ninja.trek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ninja.trek.entity.Entity;

public class Graphics {

    public static Animation<Sprite> playerWalk;

    public static void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("tiles.atlas"));
        playerWalk = createAnim(atlas, "cube", 0.5f);
    }

    private static Animation<Sprite> createAnim(TextureAtlas atlas, String name, float delta) {
        Array<Sprite> sprites = atlas.createSprites(name);
        if (sprites.size == 0) throw new GdxRuntimeException("No sprites found for "+name);
        for (Sprite s : sprites){
            s.setSize(Entity.SPRITE_SIZE, Entity.SPRITE_SIZE);
        }
        Animation<Sprite> anim = new Animation<Sprite>(delta, sprites);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }
}
