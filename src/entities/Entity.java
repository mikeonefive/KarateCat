package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {      // you can't create an object of an abstract class but player class extends entity


    protected float x, y;       // protected only classes that extend Entity can use these, seen in package and subclasses
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected float walkSpeed;

    protected int animationTick, animationIndex;
    protected int state;
    protected float airSpeed;
    protected boolean isInAir = false;

    protected int maxHealth;
    protected int currentHealth;
    protected boolean wasJustHit;

    // attackBox, area that the player attacks and if there's an enemy there it gets a can of whoopass (and vice versa)
    protected Rectangle2D.Float attackBox;

    public Entity (float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics g, int xLvlOffset) {
        // for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void drawAttackBox(Graphics graphics, int levelOffsetX) {
        graphics.setColor(Color.red);
        graphics.drawRect((int)attackBox.x - levelOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);

    }

    public void justGotHit() {
        wasJustHit = true;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

//    protected void updateHitbox() {     // protected because player/entity class can update this but not game class for example
//        hitbox.x = (int) x;
//        hitbox.y = (int) y;
//    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

}
