package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;


public class Monster extends Enemy {

    //attackBox
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Monster(float x, float y) {

        super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER);

        // the attack distance of this type of enemy is shorter than the standard
        this.attackDistance = Game.TILES_SIZE / 3f;

        // monster stays in its default size, otherwise it's too tall
        initHitbox(x, y, 16, 40);
        
        initAttackBox();

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 30, 40);
        attackBoxOffsetX = 7; // this should be the correct offsetX (30 - 16 / 2)
    }


    public void update(int[][] levelData, Player player) {
        updateMoving(levelData, player);
        updateAnimationTick();
        updateAttackBox();

    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;

        if (walkDir == LEFT) {
            attackBox.x = hitbox.x - 24;
        }

        attackBox.y = hitbox.y;

    }


    private void updateMoving(int[][] levelData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(levelData);
        }

        if (inAir) {
            updateInAir(levelData);
        } else {
            switch (enemyState) {

                case IDLE:
                    changeToNewEnemyState(WALK);
                    break;

                case WALK:

                    if (canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if (isPlayerCloseForAttack(player)) {
                        changeToNewEnemyState(ATTACK);
                    }

                    move(levelData);
                    break;
            }
        }

    }

    public void drawAttackBox(Graphics g, int levelOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - levelOffsetX), (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    public int flipX() {

        if (walkDir == LEFT) {
            // - TILES_SIZE because monster was walking too far to the right (no solid tile)
            return width - Game.TILES_SIZE;
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
