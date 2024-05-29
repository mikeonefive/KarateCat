package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;


public class Ghost extends Enemy {

    private int attackBoxOffsetX;

    public Ghost(float x, float y) {

        super(x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST);

        // the attack distance of this type of enemy is shorter than the standard
        this.attackDistance = Game.TILES_SIZE / 1.8f;

        initHitbox(32, 32);
        
        initAttackBox();

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 50, 40);
        attackBoxOffsetX = 12;
    }


    public void update(int[][] levelData, Player player) {
        updateBehavior(levelData, player);
        updateAnimationTick();
        updateAttackBox();

    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;

    }

    private void updateBehavior(int[][] levelData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(levelData);
        }

        if (isInAir) {
            updateInAir(levelData);
        } else {
            switch (state) {

                case IDLE:
                    changeToNewEnemyState(WALK);
                    break;

                case WALK:

                    if (canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player)) {
                            changeToNewEnemyState(ATTACK);
                        }
                    }

                    move(levelData);
                    break;

                case ATTACK:
                    if (animationIndex == 0) { // we have to reset this every time we start the attack animation
                        checkedAttackAlready = false;
                    }

                    if (animationIndex != 0 && !checkedAttackAlready) {
                        checkIfPlayerWasHit(attackBox, player);
                    }
                    break;

                case WASHIT:
                    break;
            }
        }

    }

    // ghost is able to move differently than other enemies (no floor check)
    @Override
    protected void move(int[][] levelData) {
        float xSpeed;

        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        // Ghost doesn't need the floor check so it can also move when there's no floor
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            // if(isFloor(hitbox, xSpeed, levelData)) {
                hitbox.x += xSpeed;
                return;
            // }
        }

        changeWalkingDir();
    }

    public int flipX() {

        if (walkDir == LEFT) {
            return width;
        }

        return 0;
    }

    public int flipWidth() {
        if (walkDir == LEFT) {
            return -1;
        }

        return 1;

    }

}
