package ninja.trek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LudumMain extends Game {
    private Skin skin;

    public Screen gameScreen, menuScreen;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("neon/skin/neon-ui.json"));
        gameScreen = new GameScreen(skin, this);
        menuScreen = new MenuScreen(skin, this);

        setScreen(gameScreen);
//        setScreen(menuScreen);
    }


}