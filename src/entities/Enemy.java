package entities;

import main.Game;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {

    protected int animationIndex, enemyState, enemyType;
    protected int animationTick, animationSpeed = 15;

    protected boolean firstUpdate = true;
    protected boolean inAir = false;

    protected float fallSpeed;
    protected float gravity = 0.05f * Game.SCALE;

    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;

    protected int tileY;

    protected float attackDistance = Game.TILES_SIZE;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    protected void firstUpdateCheck(int[][] levelData) {

        if (!isEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }
        firstUpdate = false;

    }

    protected void updateInAir(int[][] levelData) {

        if (canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData))
        {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] levelData) {
        float xSpeed;

        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            if(isFloor(hitbox, xSpeed, levelData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkingDir();
    }


    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }


    protected boolean canSeePlayer(int[][] levelData, Player player) {

        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
        if (tileY == playerTileY) {
            if (isPlayerInRange(player)) {
                if (isSightClear(levelData, hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;

    }


    protected boolean isPlayerInRange(Player player) {

        int absDistance = (int) Math.abs(player.hitbox.x - hitbox.x); // returns the absolute distance between player and enemy
        return absDistance <= attackDistance * 10;
    }


    protected boolean isPlayerCloseForAttack(Player player) {
        int absDistance = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absDistance <= attackDistance;
    }


    protected void changeToNewEnemyState(int enemyState) {
        // when changing the state, we also reset animation variables to start from 1st frame
        this.enemyState = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }


    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;

                if (enemyState == ATTACK) {     // enemy only attacks once goes to idle and then loop (can see player etc.) starts all over
                    enemyState = IDLE;
                }
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
