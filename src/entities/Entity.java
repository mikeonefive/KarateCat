package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {      // you can't create an object of an abstract class but player class extends entity


    protected float x, y;       // protected only classes that extend Entity can use these, seen in package and subclasses
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    public Entity (float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics g) {
        // for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

//    protected void updateHitbox() {     // protected because player/entity class can update this but not game class for example
//        hitbox.x = (int) x;
//        hitbox.y = (int) y;
//    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

}
