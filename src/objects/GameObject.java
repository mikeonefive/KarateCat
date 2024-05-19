package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {

    protected int x,y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, isActive = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(objectType)) {
                animationIndex = 0;

                // after barrel has been destroyed don't keep animating it
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    isActive = false;

                } else if (objectType == CANNON_LEFT || objectType == CANNON_RIGHT)
                    doAnimation = false;
            }
        }
    }

    public void reset() {
        animationIndex = 0;
        animationTick = 0;
        isActive = true;

        // box & barrel only animate when they get destroyed, cannons only animate when player in sight
        if (objectType == BARREL || objectType == BOX
                || objectType == CANNON_LEFT || objectType == CANNON_RIGHT) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width * Game.SCALE, height * Game.SCALE);
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        // for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    public int getObjectType() {
        return objectType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getAnimationTick() {
        return animationTick;
    }


}
