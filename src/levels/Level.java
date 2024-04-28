package levels;

import entities.Monster;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.getLevelData;
import static utilz.HelpMethods.getMonsters;
import static utilz.HelpMethods.getPlayerSpawnPosition;

public class Level {

    private BufferedImage image;
    private int [][] levelData;
    private ArrayList<Monster> monsters;

    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;

    private Point playerSpawnPosition;


    public Level(BufferedImage image) {
        this.image = image;
        createLevelData();
        createEnemies();
        calculateLevelOffset();
        calculatePlayerSpawnPosition();
    }

    private void calculatePlayerSpawnPosition() {

        playerSpawnPosition = getPlayerSpawnPosition(image);
    }

    private void calculateLevelOffset() {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {

        monsters = getMonsters(image);
    }

    private void createLevelData() {

        levelData = getLevelData(image);

    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLvlData() {
        return levelData;
    }

    public int getLvlOffset() {
        return maxLevelOffsetX;
    }

    public ArrayList<Monster> getMonstaz() {
        return monsters;
    }

    public Point getPlayerSpawnCoordinates() {
        return playerSpawnPosition;
    }
}
