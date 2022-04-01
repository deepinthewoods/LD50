package ninja.trek.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class PackTextures {
    public static void main(String[] args){
        TexturePacker.Settings settings = new TexturePacker.Settings();
        String folder = System.getProperty("user.dir");
        String input = folder + "\\blender\\sprites";
        String output = folder + "\\assets";
        String packFileName = "tiles";

        System.out.println(input);
        settings.stripWhitespaceX = true;
        settings.stripWhitespaceY = true;
        settings.filterMag = Texture.TextureFilter.Nearest;
        settings.filterMin = Texture.TextureFilter.Nearest;
        settings.paddingX = 1;
        settings.paddingY = 1;
        settings.duplicatePadding = true;
        settings.pot = true;
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;

        TexturePacker.process(settings, input, output, packFileName);
        System.out.println("DONE");
    }
}
