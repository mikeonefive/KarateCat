package utilz;

import main.Game;
import org.w3c.dom.css.Rect;

import java.awt.geom.Rectangle2D;

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

        int value = levelData[(int)yIndex][(int)xIndex];

        // 48 and up is not a tile, also less than 0 ain't a tile and 11 is a transparent sprite, so not 11 means solid
        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }
        return false;
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
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);     // current tile our player is in in pixels
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
        if(isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
            if(isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
    }
}
