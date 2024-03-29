package utilz;

import entities.Crabby;
import entities.Monster;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.EnemyConstants.MONSTER;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    // public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";

    public static final String MENU_BUTTON_ATLAS = "button_atlas.png";
    public static final String MENU_BOARD = "menu_board.png";
    public static final String MENU_BACKGROUND = "menu_bg.png";

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

    public static ArrayList<Monster> getMonsters() {
        BufferedImage img = getSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Monster> monsterList = new ArrayList<>();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Color color = new Color(img.getRGB(x, y));
                int value = color.getGreen();     // get green value on that position, if it's 0 meaning SKULL then we add a new one to the list
                if (value == MONSTER) {
                    monsterList.add(new Monster(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                }

            }
        }
        return monsterList;

    }


    public static ArrayList<Crabby> getCrabs() {
        BufferedImage img = getSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == CRABBY)
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;

    }



    public static int[][] getLevelData() {

        BufferedImage img = getSpriteAtlas(LEVEL_ONE_DATA);
        int[][] levelData = new int[img.getHeight()][img.getWidth()];

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Color color = new Color(img.getRGB(x, y));
                int value = color.getRed();     // get red value on that position
                if (value >= 48) {
                    value = 0;
                }
                levelData[y][x] = value;
            }
        }
        return levelData;
    }
}
