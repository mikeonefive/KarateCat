package objects;

import main.Game;

public class Cannon extends GameObject {

    private int tileY;

    public Cannon(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(32, 32);
        hitbox.x -= (int)(4 * Game.SCALE); // this is to make sure the cannon is in the center of the tile we place it on
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }
}
