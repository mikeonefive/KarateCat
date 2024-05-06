package objects;

import main.Game;

public class Potion extends GameObject {


    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true; // potion animates as long as it's active
        initHitbox(7, 14);
        xDrawOffset = (int)(3 * Game.SCALE); // 3 pixels to left of sprite atlas
        yDrawOffset = (int)(2 * Game.SCALE); // 2 pixels from top of sprite atlas
    }

    public void update() {
        updateAnimationTick();
    }

}
