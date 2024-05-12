package objects;

import main.Game;

public class Spike extends GameObject {

    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32, 15); // width 32, height 15
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 15);
        hitbox.y += yDrawOffset;
    }

}
