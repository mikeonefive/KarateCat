package utilz;

import entities.Monster;
import main.Game;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.MONSTER;

import static utilz.Constants.ObjectConstants.*;

public class HelpMethods {

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] levelData) {

        // if any of our top left, top right, bottom left, bottom right return true we can move there
        if (!isSolid(x, y, levelData)) {
            // bottom right of tile
            if ((!isSolid(x + width, y + height, levelData))) {
                // top right
                if ((!isSolid(x + width, y, levelData))) {
                    // bottom left
                    if ((!isSolid(x, y + height, levelData))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSolid(float x, float y, int[][] levelData) {

        int maxWidth = levelData[0].length * Game.TILES_SIZE;

        // check if we are in the game window
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return isTileSolid((int)xIndex, (int)yIndex, levelData);

    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] levelData) {

        int value = levelData[yTile][xTile];

        // 48 and up is not a tile, also less than 0 ain't a tile and 11 is a transparent sprite, so not 11 means solid
        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }

        return false;

    }

    public static boolean isProjectileHittingLevelBorder(Projectile p, int[][] levelData) {

        // width & height divided by 2 because we want the middle part of the object not the upper left or something
        return isSolid(p.getHitbox().x + p.getHitbox().width / 2,
                p.getHitbox().y + p.getHitbox().height / 2, levelData);
    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);     // current tile our player is in in pixels
        if (xSpeed > 0) {       // if the colliding tile is to the right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);    // difference between size of tile and size of player
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);     // current tile our player is in pixels
        if (airSpeed > 0) {       // are going we down? falling, touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1; // if we don't put -1 here we might already be inside the tile
            // puts us 1px above the floor
        } else {
            // jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {

        // check pixel below bottom left and bottom right (corners of our entity)
        // if both not solid -> we are in the air
        if (isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
            if (isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        if (xSpeed > 0) {   // if we're going to the right
            return isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData);
        }

        return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
    }

    public static boolean canCannonSeePlayer(int[][] levelData, Rectangle2D.Float hitboxObject1,
                                             Rectangle2D.Float hitboxObject2, int yTileCurrent) {

        int xTileObject1 = (int) (hitboxObject1.x / Game.TILES_SIZE);
        int xTileObject2 = (int) (hitboxObject2.x / Game.TILES_SIZE);

        // we have to check which tile is greater because in the loop we have to know which direction we wanna check
        if (xTileObject1 > xTileObject2)
        {
            return areAllTilesClear(xTileObject2, xTileObject1, yTileCurrent, levelData);
        } else {
            return areAllTilesClear(xTileObject1, xTileObject2, yTileCurrent, levelData);
        }
    }

    public static boolean areAllTilesClear(int xStart, int xEnd, int y, int[][] levelData) { // meaning no walkable tiles between the 2 points
        for (int i = 0; i < xEnd - xStart; i++)
            // check the difference between object1 and object2 xTile and if any of the tiles between them are solid
            if (isTileSolid(xStart + i, y, levelData))
                return false;

        return true;
    }

    public static boolean areAllCurrentTilesWalkable(int xStart, int xEnd, int y, int[][] levelData) {

        if (areAllTilesClear(xStart, xEnd, y, levelData)) {
            for (int i = 0; i < xEnd - xStart; i++) {

                // check if tile underneath the current one is walkable, if it isn't then return false
                if (!isTileSolid(xStart + i, y + 1, levelData)) {
                    return false;
                }

            }
        }
        return true;
    }



    public static boolean isSightClear(int[][] levelData, Rectangle2D.Float hitboxObject1,
                                       Rectangle2D.Float hitboxObject2, int yTileCurrent)
    {

        int xTileObject1 = (int) (hitboxObject1.x / Game.TILES_SIZE);
        int xTileObject2 = (int) (hitboxObject2.x / Game.TILES_SIZE);

        // we have to check which tile is greater because in the loop we have to know which direction we wanna check
        if (xTileObject1 > xTileObject2)
        {
            return areAllCurrentTilesWalkable(xTileObject2, xTileObject1, yTileCurrent, levelData);
        } else {
            return areAllCurrentTilesWalkable(xTileObject1, xTileObject2, yTileCurrent, levelData);
        }
    }

    public static int[][] getLevelData(BufferedImage image) {

        int[][] levelData = new int[image.getHeight()][image.getWidth()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color color = new Color(image.getRGB(x, y));
                int value = color.getRed();     // get red value on that position
                if (value >= 48) {
                    value = 0;
                }
                levelData[y][x] = value;
            }
        }
        return levelData;
    }

    public static ArrayList<Monster> getMonsters(BufferedImage img) {

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




    public static Point getPlayerSpawnPosition(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
            }

        return new Point(Game.TILES_SIZE, Game.TILES_SIZE);
    }

    public static ArrayList<Potion> getPotions(BufferedImage img) {

        ArrayList<Potion> potionsList = new ArrayList<>();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Color color = new Color(img.getRGB(x, y));
                int value = color.getBlue();     // get blue value on that position
                if (value == RED_POTION || value == BLUE_POTION) {
                    potionsList.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, value));
                }
            }
        }
        return potionsList;
    }

    public static ArrayList<GameContainer> getContainers(BufferedImage img) {

        ArrayList<GameContainer> containersList = new ArrayList<>();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Color color = new Color(img.getRGB(x, y));
                int value = color.getBlue();     // get blue value on that position
                if (value == BOX || value == BARREL) {
                    containersList.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, value));
                }
            }
        }
        return containersList;
    }

    public static ArrayList<Spike> getSpikes(BufferedImage image) {
        ArrayList<Spike> spikesList = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color color = new Color(image.getRGB(x, y));
                int value = color.getBlue();     // get blue value on that position
                if (value == SPIKE) {
                    spikesList.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
                }
            }
        }
        return spikesList;

    }

    public static ArrayList<Cannon> getCannons(BufferedImage image) {
        ArrayList<Cannon> list = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color color = new Color(image.getRGB(x, y));
                int value = color.getBlue();     // get blue value on that position
                if (value == CANNON_LEFT || value == CANNON_RIGHT) {
                    list.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, value));
                }
            }
        }
        return list;

    }
}