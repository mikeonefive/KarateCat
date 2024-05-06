package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

import static utilz.HelpMethods.*;



public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true;

    protected int walkDir = LEFT;

    protected int tileY;

    protected float attackDistance = Game.TILES_SIZE;

    protected boolean isActive = true;
    protected boolean checkedAttackAlready;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        walkSpeed = 0.35f * Game.SCALE;

        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] levelData) {

        if (!isEntityOnFloor(hitbox, levelData)) {
            isInAir = true;
        }
        firstUpdate = false;

    }

    protected void updateInAir(int[][] levelData) {

        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData))
        {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            isInAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
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
        this.state = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    public void receiveDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            changeToNewEnemyState(DEAD);
        } else {
            changeToNewEnemyState(WASHIT);
        }
    }

    protected void checkIfPlayerWasHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox)) {
            player.updateHealth(-getDamageInflictedByEnemy(enemyType));
        }
        checkedAttackAlready = true;
    }


    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(enemyType, state)) {
                animationIndex = 0;


                switch(state) {
                    // enemy only attacks once goes to idle and then loop (can see player etc.) starts all over
                    case ATTACK, WASHIT -> state = IDLE;
                    case DEAD -> isActive = false;
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

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        changeToNewEnemyState(IDLE);
        isActive = true;
        airSpeed = 0;
    }


    public boolean isAlive() {
        return isActive;
    }

}
