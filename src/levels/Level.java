package levels;

import entities.Ghost;
import entities.Monster;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;

public class Level {

    private final BufferedImage image;
    private final int [][] levelData;
    private final ArrayList<Monster> monsters = new ArrayList<>();
    private final ArrayList<Ghost> ghosts = new ArrayList<>();
    private final ArrayList<Potion> potions = new ArrayList<>();
    private final ArrayList<GameContainer> containers = new ArrayList<>();
    private final ArrayList<Spike> spikes = new ArrayList<>();
    private final ArrayList<Cannon> cannons = new ArrayList<>();

    private Point playerSpawnPosition;

    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;


    public Level(BufferedImage image) {
        this.image = image;
        levelData = new int[image.getHeight()][image.getWidth()];
        loadLevel();
        calculateLevelOffset();

    }



    private void loadLevel() {

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();     // get red value on that position
                int green = color.getGreen();
                int blue = color.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
        }

    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50)
            levelData[y][x] = 0;
        else
            levelData[y][x] = redValue;

    }

    private void loadEntities(int greenValue, int x, int y) {
        switch(greenValue) {    // get green value on that position, if it's 0 meaning MONSTER or 1 meaning GHOST then we add a new one to the list
            case MONSTER -> monsters.add(new Monster(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case GHOST -> ghosts.add(new Ghost(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 -> playerSpawnPosition = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, BLUE_POTION -> potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
        }
    }

    private void calculateLevelOffset() {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset;
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

    public ArrayList<Ghost> getGhostz() {
        return ghosts;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public Point getPlayerSpawnCoordinates() {
        return playerSpawnPosition;
    }
}
