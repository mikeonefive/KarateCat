package utilz;

import main.Game;

public class Constants {

    public static class EnemyConstants {
        public static final int MONSTER = 0;

        public static final int IDLE = 0;
        public static final int WALK = 1;
        public static final int ATTACK = 2;
        public static final int DIE = 3;

        public static final int MONSTER_DEFAULT_WIDTH = 64;
        public static final int MONSTER_DEFAULT_HEIGHT = 64;

        public static final int MONSTER_WIDTH = (int)(MONSTER_DEFAULT_WIDTH * Game.SCALE);
        public static final int MONSTER_HEIGHT = (int)(MONSTER_DEFAULT_HEIGHT * Game.SCALE);

        public static int getSpriteAmount(int enemyType, int enemyState) {

            switch(enemyType) {
                case MONSTER:
                    switch(enemyState) {
                        case IDLE:
                        case ATTACK:
                        case WALK:
                            return 4;
                        case DIE:
                            return 7;
                    }
            }
            return 0;

        }
    }

    public static class BackgroundEnvironment {
        public static final int BIG_CLOUD_DEFAULT_WIDTH = 448;
        public static final int BIG_CLOUD_DEFAULT_HEIGHT = 101;
        public static final int SMALL_CLOUD_DEFAULT_WIDTH = 74;
        public static final int SMALL_CLOUD_DEFAULT_HEIGHT = 24;


        public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_DEFAULT_WIDTH * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int)(BIG_CLOUD_DEFAULT_HEIGHT * Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_DEFAULT_WIDTH * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int)(SMALL_CLOUD_DEFAULT_HEIGHT * Game.SCALE);
    }

    public static class UI {

        public static class Buttons {
            public static final int BUTTON_WIDTH_DEFAULT = 140;
            public static final int BUTTON_HEIGHT_DEFAULT = 45;
            public static final int BUTTON_WIDTH = (int)(BUTTON_WIDTH_DEFAULT * Game.SCALE);
            public static final int BUTTON_HEIGHT = (int)(BUTTON_HEIGHT_DEFAULT * Game.SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int WALK = 1;
        public static final int JUMP = 2;
        public static final int FALL = 3;
        public static final int PUNCH = 6;
        public static final int HIGHKICK = 12;
        public static final int UPPERCUT = 16;
        public static final int SPINKICK = 4;


        public static int getSpriteAmount(int playerAction) {
            switch (playerAction) {
                case WALK:
                case JUMP:
                    return 8;
                case IDLE:
                    return 4;
                case PUNCH:
                case HIGHKICK:
                case UPPERCUT:
                    return 6;
                case SPINKICK:
                    return 10;
                case FALL:
                default:
                    return 1;
            }
        }

    }
}
