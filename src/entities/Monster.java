package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;


public class Monster extends Enemy {

    private int attackBoxOffsetX;
    private int hitBoxOffsetXLeft = 10;

    public Monster(float x, float y) {

        super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER);

        // the attack distance of this type of enemy is shorter than the standard
        this.attackDistance = Game.TILES_SIZE / 1.8f;

        // monster stays in its default size, otherwise it's too tall
        initHitbox(16, 40);
        
        initAttackBox();

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 35, 40);
        attackBoxOffsetX = 7; // this should be the correct offsetX (30 - 16 / 2)
    }


    public void update(int[][] levelData, Player player) {
        updateBehavior(levelData, player);
        updateAnimationTick();
        updateAttackBox();

    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x;

        // this is for this specific monster because it's in its default size
        if (walkDir == LEFT) {
            attackBox.x = hitbox.x - 24;
        }

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

                    if (animationIndex == 3 && !checkedAttackAlready) {
                        checkIfPlayerWasHit(attackBox, player);
                    }
                    break;
                case WASHIT:
                    break;
            }
        }

    }


    public int flipX() {

        if (walkDir == LEFT) {
            // - TILES_SIZE because monster was walking too far to the right (no solid tile)
            // + hitboxOffsetXLeft because the hitbox wasn't in the right place when it walked to the left
            return width - Game.TILES_SIZE + hitBoxOffsetXLeft;
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
