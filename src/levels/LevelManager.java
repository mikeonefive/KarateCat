package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private Level level1;

    public LevelManager(Game game) {
        this.game = game;
//        levelSprite = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        level1 = new Level(LoadSave.getLevelData());
    }

    private void importOutsideSprites() {

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48]; // width = 12, height = 4

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 12; x++) {
                int index = y * 12 + x;
                levelSprite[index] = img.getSubimage(x * 32, y * 32, 32, 32);

            }

        }
    }

    public void draw(Graphics g) {

        for (int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                int index = level1.getSpriteIndex(x, y);
                g.drawImage(levelSprite[index], x * Game.TILES_SIZE, y * Game.TILES_SIZE,
                        Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
            
        }

    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return level1;
    }
}
