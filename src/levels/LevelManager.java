package levels;

import gamestates.GameState;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
//        levelSprite = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel() {
        levelIndex ++;
        if (levelIndex >= levels.size()) {
            levelIndex = 0;
            System.out.println("No more levels. You beat the game!");
            GameState.state = GameState.MENU;
        }

        Level newLevel = levels.get(levelIndex);
        game.getPlayGame().getEnemyManager().loadEnemies(newLevel);
        game.getPlayGame().getPlayer().loadLevelData(newLevel.getLvlData());
        game.getPlayGame().setMaxLevelOffset(newLevel.getLvlOffset());
        game.getPlayGame().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() {

        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage image : allLevels) {
            levels.add(new Level(image));
        }

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

    public void draw(Graphics g, int levelOffset) {

        for (int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < levels.get(levelIndex).getLvlData()[0].length; x++) {
                int index = levels.get(levelIndex).getSpriteIndex(x, y);
                g.drawImage(levelSprite[index], x * Game.TILES_SIZE - levelOffset, y * Game.TILES_SIZE,
                        Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
            
        }

    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}
