package objects;

import main.Game;

import java.awt.geom.Rectangle2D;
import static utilz.Constants.Projectiles.*;

public class Projectile {

    private Rectangle2D.Float hitbox;
    private int direction;
    private boolean isActive = true;

    public Projectile(int x, int y, int direction) {

        int xOffset = (int)(-3 * Game.SCALE);
        int yOffset = (int)(8 * Game.SCALE);

        if (direction == 1)
            xOffset = (int)(15 * Game.SCALE);


        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_ARROW_WIDTH, CANNON_ARROW_HEIGHT);
        this.direction = direction;
    }

    public void updatePosition() {
        hitbox.x += direction * SPEED;
    }

    public void setPosition(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public int getDirection() {
        return direction;
    }

}

