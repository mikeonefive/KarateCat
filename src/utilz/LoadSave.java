package utilz;

import entities.Crabby;
import entities.Monster;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.EnemyConstants.MONSTER;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";

    public static final String MENU_BUTTON_ATLAS = "button_atlas.png";
    public static final String MENU_BOARD = "menu_board.png";
    public static final String MENU_BACKGROUND = "menu_background.png";

    public static final String LEVEL_COMPLETE_IMG = "level_completed_sprite.png";

    public static final String PAUSE_BACKGROUND = "pause_background.png";
    public static final String SOUND_BUTTONS = "sound_buttons.png";
    public static final String RSM_BUTTONS = "rsm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";

    public static final String PLAYGAME_BACKGROUND = "playgame_bg.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";

    public static final String MONSTER_SPRITES = "monster1_sprites.png";
    public static final String CRABBY_SPRITES = "crabby_sprites.png";

    public static final String STATUS_BAR = "lifeandattackbar.png";

    public static BufferedImage getSpriteAtlas(String fileName) {

        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);


        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    return img;
    }


    public static BufferedImage[] getAllLevels() {
        URL url = LoadSave.class.getResource("/levelfiles");
        File file;

        // url = location, URI = actual resource the folder
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // will go over all files in folder and list them inside the array
        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        // these loops sort the files for the filesSorted Array
        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }
        }

        // take the sorted files froma above and put them in this array of images
        BufferedImage[] images = new BufferedImage[filesSorted.length];
        for (int i = 0; i < images.length; i++) {
            try {
                images[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return images;


    }




}
