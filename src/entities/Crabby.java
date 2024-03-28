package entities;

import main.Game;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

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
            if (!isEntityOnFloor(hitbox, levelData)) {
                inAir = true;
            }
            firstUpdate = false;
        }

        if (inAir) {
            if (canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData))
            {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            }
            else {
                inAir = false;
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE:
                    enemyState = WALK;
                    break;
                case WALK:
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

                    break;
            }
        }

    }

}