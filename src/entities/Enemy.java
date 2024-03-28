package entities;

import main.Game;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected int animationIndex, enemyState, enemyType;
    protected int animationTick, animationSpeed = 15;

    protected boolean firstUpdate = true;
    protected boolean inAir = false;

    protected float fallSpeed;
    protected float gravity = 0.05f * Game.SCALE;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
            }
        }

    }


    protected void changeWalkingDir() {
        if(walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }


    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

}
