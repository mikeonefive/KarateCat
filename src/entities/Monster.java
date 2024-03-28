package entities;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Monster extends Enemy {

    public Monster(float x, float y) {

        super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER);

        // (int)(16 * Game.SCALE), (int)(40 * Game.SCALE)
        initHitbox(x, y, 16, 40);

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
