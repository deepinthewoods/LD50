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
        gameScreen = new GameScreen(skin, this);
        menuScreen = new MenuScreen(skin, this);
        skin = new Skin(Gdx.files.internal("skin.json"));

        setScreen(gameScreen);
//        setScreen(menuScreen);
    }


}