package ninja.trek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ninja.trek.entity.Entity;
import ninja.trek.entity.Player;

public class Graphics {

    public static Animation<Sprite> playerWalk;
    public static Animation<Sprite> playerWalkAim;
    public static Animation<Sprite> playerStandAim;
    public static Animation<Sprite> playerStand, playerDie;

    public static Animation<Sprite>
            elderWalk, elderStand, elderWalkAim, elderStandAim, elderDie
            , villagerWalk, villagerWalkAim, villagerStand, villagerStandAim, villagerDie
            ;


    public static Animation<Sprite>[] itemWalk = new Animation[4];
    public static Animation<Sprite>[] itemWalkAim = new Animation[4];
    public static Animation<Sprite>[] itemStandAim = new Animation[4];
    public static Animation<Sprite>[] itemStand = new Animation[4];
    public static Animation<Sprite>[] itemDie = new Animation[4];

    public static void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("tiles.atlas"));
        playerWalk = createAnim(atlas, "player", 0.06f, 2, 15);
        playerWalkAim = createAnim(atlas, "player", 0.06f, 18, 16);
        playerStand = createAnim(atlas, "player", 0.06f, 0, 1);
        playerStandAim = createAnim(atlas, "player", 0.06f, 1, 1);
        playerDie = createAnim(atlas, "player", 0.06f, 35, 8);
        playerDie.setPlayMode(Animation.PlayMode.NORMAL);

        villagerWalk = createAnim(atlas, "villager", 0.06f, 2, 15);
        villagerWalkAim = createAnim(atlas, "villager", 0.06f, 18, 16);
        villagerStand = createAnim(atlas, "villager", 0.06f, 0, 1);
        villagerStandAim = createAnim(atlas, "villager", 0.06f, 1, 1);
        villagerDie = createAnim(atlas, "villager", 0.06f, 35, 8);
        villagerDie.setPlayMode(Animation.PlayMode.NORMAL);

        elderWalk = createAnim(atlas, "elder", 0.06f, 2, 15);
        elderWalkAim = createAnim(atlas, "elder", 0.06f, 18, 16);
        elderStand = createAnim(atlas, "elder", 0.06f, 0, 1);
        elderStandAim = createAnim(atlas, "elder", 0.06f, 1, 1);
        elderDie = createAnim(atlas, "elder", 0.06f, 35, 8);
        elderDie.setPlayMode(Animation.PlayMode.NORMAL);

        itemWalk[Player.BOW] = createAnim(atlas, "bow", 0.06f, 2, 15);
        itemWalkAim[Player.BOW] = createAnim(atlas, "bow", 0.06f, 18, 16);
        itemStand[Player.BOW] = createAnim(atlas, "bow", 0.06f, 0, 1);
        itemStandAim[Player.BOW] = createAnim(atlas, "bow", 0.06f, 1, 1);
        itemDie[Player.BOW] = createAnim(atlas, "bow", 0.06f, 35, 8);
        itemDie[Player.BOW].setPlayMode(Animation.PlayMode.NORMAL);

        itemWalk[Player.SWORD] = createAnim(atlas, "sword", 0.06f, 2, 15);
        itemWalkAim[Player.SWORD] = createAnim(atlas, "sword", 0.06f, 18, 16);
        itemStand[Player.SWORD] = createAnim(atlas, "sword", 0.06f, 0, 1);
        itemStandAim[Player.SWORD] = createAnim(atlas, "sword", 0.06f, 1, 1);
        itemDie[Player.SWORD] = createAnim(atlas, "sword", 0.06f, 35, 8);
        itemDie[Player.SWORD].setPlayMode(Animation.PlayMode.NORMAL);

    }

    private static Animation<Sprite> createAnim(TextureAtlas atlas, String name, float delta) {
        Array<Sprite> sprites = atlas.createSprites(name);
        if (sprites.size == 0) throw new GdxRuntimeException("No sprites found for "+name);
        for (Sprite s : sprites){
            s.setSize(Entity.SPRITE_SIZE, Entity.SPRITE_SIZE);
            s.setOrigin(Entity.SPRITE_OFFSET, Entity.SPRITE_OFFSET);

        }
        Animation<Sprite> anim = new Animation<Sprite>(delta, sprites);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }

    private static Animation<Sprite> createAnim(TextureAtlas atlas, String name, float delta, int startFrame, int frameCount) {
        Array<Sprite> sprites = atlas.createSprites(name);
        if (sprites.size == 0) throw new GdxRuntimeException("No sprites found for "+name);
        for (Sprite s : sprites){
            s.setSize(Entity.SPRITE_SIZE, Entity.SPRITE_SIZE);
            s.setOrigin(Entity.SPRITE_OFFSET, Entity.SPRITE_OFFSET);
        }
        Sprite[] newSprites = new Sprite[frameCount];
        for (int i = 0; i < frameCount; i++)
            newSprites[i] = sprites.get(i + startFrame);
        Animation<Sprite> anim = new Animation<Sprite>(delta, newSprites);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }
}
