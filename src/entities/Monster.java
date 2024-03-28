package entities;

import static utilz.Constants.EnemyConstants.*;

public class Monster extends Enemy {

    public Monster(float x, float y) {
        super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER);
    }

}
