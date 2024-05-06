package entities;

import main.Game;
import static utilz.Constants.EnemyConstants.*;


public class Crabby extends Enemy {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox((int) (22 * Game.SCALE), (int) (19 * Game.SCALE));

    }

    public void update(int[][] levelData, Player player) {
        updateMoving(levelData, player);
        updateAnimationTick();

    }


    private void updateMoving(int[][] levelData, Player player) {
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
                    }
                    if (isPlayerCloseForAttack(player)) {
                        changeToNewEnemyState(ATTACK);
                    }

                    move(levelData);
                    break;
            }
        }

    }

}