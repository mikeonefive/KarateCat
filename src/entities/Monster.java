package entities;

import main.Game;

import static utilz.Constants.EnemyConstants.*;


public class Monster extends Enemy {

    public Monster(float x, float y) {

        super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER);

        // the attack distance of this type of enemy is shorter than the standard
        this.attackDistance = Game.TILES_SIZE / 3f;

        // monster stays in its default size, otherwise it's too tall
        initHitbox(x, y, 16, 40);

    }


    public void update(int[][] levelData, Player player) {
        updateMoving(levelData, player);
        updateAnimationTick();

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

}
