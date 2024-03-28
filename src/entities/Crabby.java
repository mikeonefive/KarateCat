package entities;

import main.Game;
import static utilz.Constants.EnemyConstants.*;


public class Crabby extends Enemy {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));

    }

    public void update(int[][] levelData) {
        updateMoving(levelData);
        updateAnimationTick();

    }


    private void updateMoving(int[][] levelData) {
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
                    move(levelData);
                    break;
            }
        }

    }

}