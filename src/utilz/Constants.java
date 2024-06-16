package utilz;

import main.Game;

public class Constants {

    public static final float GRAVITY = 0.05f * Game.SCALE;
    public static final int ANIMATION_SPEED = 15;

    public static class Projectiles {
        public static final int CANNON_ARROW_DEFAULT_WIDTH = 22;
        public static final int CANNON_ARROW_DEFAULT_HEIGHT = 6;

        public static final int CANNON_ARROW_WIDTH = (int)(Game.SCALE * CANNON_ARROW_DEFAULT_WIDTH);
        public static final int CANNON_ARROW_HEIGHT = (int)(Game.SCALE * CANNON_ARROW_DEFAULT_HEIGHT);
        public static final float SPEED = 0.8f * Game.SCALE;
    }

    public static class ObjectConstants {

        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;

        public static final int RED_POTION_VALUE = 15; // for restoring health
        public static final int BLUE_POTION_VALUE = 10; // for getting more power

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int)(Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int)(Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int)(Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int)(Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int CANNON_WIDTH_DEFAULT = 32;
        public static final int CANNON_HEIGHT_DEFAULT = 32;
        public static final int CANNON_WIDTH = (int)(Game.SCALE * CANNON_WIDTH_DEFAULT);
        public static final int CANNON_HEIGHT = (int)(Game.SCALE * CANNON_HEIGHT_DEFAULT);


        public static int getSpriteAmount(int objectType) {
            switch(objectType) {
                case RED_POTION, BLUE_POTION:
                    return 7;

                case BARREL, BOX:
                    return 8;

                case CANNON_LEFT, CANNON_RIGHT:
                    return 5;
            }
            return 1;
        }
    }

    public static class EnemyConstants {

        public static final int MONSTER = 0;
        public static final int MONSTER_DEFAULT_WIDTH = 64;
        public static final int MONSTER_DEFAULT_HEIGHT = 64;
        public static final int MONSTER_WIDTH = (int)(MONSTER_DEFAULT_WIDTH * Game.SCALE);
        public static final int MONSTER_HEIGHT = (int)(MONSTER_DEFAULT_HEIGHT * Game.SCALE);

        public static final int MONSTER_DRAWOFFSET_X = 22; // the offset here is the difference between the start of the sprite and the start of the hitbox
        public static final int MONSTER_DRAWOFFSET_Y = 20;

        public static final int IDLE = 0;
        public static final int WALK = 1;
        public static final int ATTACK = 2;
        public static final int DEAD = 3;
        public static final int WASHIT = 4;

        public static final int GHOST = 1;
        public static final int GHOST_DEFAULT_WIDTH = 32;
        public static final int GHOST_DEFAULT_HEIGHT = 32;
        public static final int GHOST_WIDTH = (int)(GHOST_DEFAULT_WIDTH * Game.SCALE);
        public static final int GHOST_HEIGHT = (int)(GHOST_DEFAULT_HEIGHT * Game.SCALE);
        public static final int GHOST_DRAWOFFSET_X = 10; // the offset here is the difference between the start of the sprite and the start of the hitbox
        public static final int GHOST_DRAWOFFSET_Y = 17;


        public static int getSpriteAmount(int enemyType, int enemyState) {

            switch(enemyType) {
                case MONSTER:
                    switch(enemyState) {
                        case IDLE:
                        case ATTACK:
                        case WALK:
                            return 4;
                        case DEAD:
                            return 7;
                        case WASHIT:
                            return 1;
                    }

                case GHOST:
                    switch(enemyState) {
                        case IDLE:
                        case ATTACK:
                        case WALK:
                            return 6;
                    }

            }
            return 0;

        }

        public static int getMaxHealth(int enemyType) {
            switch (enemyType) {
                case MONSTER:
                    return 20;

                case GHOST:
                    return 100;

                default:
                    return 1;
            }
        }

        public static int getDamageInflictedByEnemy(int enemyType) {
            switch (enemyType) {
                case MONSTER:
                    return 15;

                case GHOST:
                    return 25;

                default:
                    return 0;
            }
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

        public static class PauseButtons {
            public static final int SOUND_WIDTH_DEFAULT = 65;
            public static final int SOUND_HEIGHT_DEFAULT = 35;
//            public static final int SOUND_WIDTH = (int)(SOUND_WIDTH_DEFAULT * Game.SCALE);
//            public static final int SOUND_HEIGHT = (int)(SOUND_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class RSMButtons {
            public static final int RSM_DEFAULT_WIDTH = 135;
            public static final int RSM_DEFAULT_HEIGHT = 26;
            public static final int RSM_WIDTH = (int) (RSM_DEFAULT_WIDTH * Game.SCALE);
            public static final int RSM_HEIGHT = (int) (RSM_DEFAULT_HEIGHT * Game.SCALE);
        }

        public static class GameOverButtons {
            public static final int GAMEOVER_DEFAULT_WIDTH = 80;
            public static final int GAMEOVER_DEFAULT_HEIGHT = 54;
            public static final int GAMEOVER_WIDTH = (int) (GAMEOVER_DEFAULT_WIDTH * Game.SCALE);
            public static final int GAMEOVER_HEIGHT = (int) (GAMEOVER_DEFAULT_HEIGHT * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 19;
            public static final int VOLUME_DEFAULT_HEIGHT = 31;
            public static final int SLIDER_DEFAULT_WIDTH = 152;

            // public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
            // public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            // public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        // public static final int UP = 1;
        public static final int RIGHT = 2;
        // public static final int DOWN = 3;

    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int WALK = 1;
        public static final int JUMP = 2;
        public static final int DOWNKICK = 12;
        public static final int DEAD = 4;
        public static final int GETTINGHIT = 5;
        public static final int FALL = 6;
        public static final int PUNCH = 9;
        public static final int POWERATTACK = 3;
        public static final int ROUNDKICK = 14;
        public static final int UPPERCUT = 15;


        public static int getSpriteAmount(int playerAction) {
            switch (playerAction) {
                case DEAD:
                    return 7;

                case WALK:
                case JUMP:
                case ROUNDKICK:
                case DOWNKICK:
                case POWERATTACK:
                    return 8;

                case IDLE:
                    return 4;
                case GETTINGHIT:
                    return 3;

                case UPPERCUT:
                    return 6;

                case PUNCH:
                    return 10;

                case FALL:
                default:
                    return 1;
            }
        }

    }
}
