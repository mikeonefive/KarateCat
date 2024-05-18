package levels;

import entities.Ghost;
import entities.Monster;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.getLevelData;
import static utilz.HelpMethods.getMonsters;
import static utilz.HelpMethods.getGhosts;
import static utilz.HelpMethods.getPlayerSpawnPosition;

public class Level {

    private BufferedImage image;
    private int [][] levelData;
    private ArrayList<Monster> monsters;
    private ArrayList<Ghost> ghosts;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;

    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;

    private Point playerSpawnPosition;


    public Level(BufferedImage image) {
        this.image = image;
        createLevelData();
        
        createEnemies();

        createPotions();
        createContainers();

        createSpikes();
        
        createCannons();

        calculateLevelOffset();
        calculatePlayerSpawnPosition();
    }

    private void createCannons() {
        cannons = HelpMethods.getCannons(image);
    }

    private void createSpikes() {
        spikes = HelpMethods.getSpikes(image);
    }

    private void createContainers() {
        containers = HelpMethods.getContainers(image);
    }

    private void createPotions() {
        potions = HelpMethods.getPotions(image);
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
        ghosts = getGhosts(image);
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

    public ArrayList<Ghost> getGhostz() {
        return ghosts;
    }

    public Point getPlayerSpawnCoordinates() {
        return playerSpawnPosition;
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
}
