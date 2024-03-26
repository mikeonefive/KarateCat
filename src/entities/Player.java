package entities;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;

    private int animTick, animIndex, animSpeed = 15;
    private int playerAction = IDLE;

    private boolean up, left, right, down, jump;
    private boolean isMoving = false, isAttacking = false;
    private float playerSpeed = 1.0f * Game.SCALE;

    private int[][] levelData;

    // for the hitbox
    private float xDrawOffset = 23 * Game.SCALE;
    private float yDrawOffset = 25 * Game.SCALE;

    // jumping/gravity
    private float airSpeed = 0f;
    private float gravity = 0.05f * Game.SCALE;
    private float jumpSpeed = -2.50f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean isInAir = false;


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);        // we take in x and y and pass them over to the Entity class where they are stored
        loadAnimations();
        initHitbox(x, y, (int)(12 * Game.SCALE), (int)(28 * Game.SCALE));
    }

    public void update() {

        updatePosition();
        updateAnimationTick();
        setAnimation();

    }

    public void render(Graphics graphics, int levelOffset) {

        graphics.drawImage(animations[playerAction][animIndex], (int)(hitbox.x - xDrawOffset) - levelOffset, (int)(hitbox.y - yDrawOffset), width, height, null);
        // drawHitbox(graphics);
    }


    private void updateAnimationTick() {

        animTick ++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex ++;
            if (animIndex >= getSpriteAmount(playerAction)) {
                animIndex = 0;
                isAttacking = false;
            }
        }
    }

    public void setAnimation() {

        int startAnimation = playerAction;

        if (isMoving) {
            playerAction = WALK;
        } else {
            playerAction = IDLE;
        }

        if (isInAir) {
            if(airSpeed < 0) {   // are we going up?
                playerAction = JUMP;
            } else
                playerAction = FALL;

        }

        if (isAttacking) {
            playerAction = PUNCH;
        }
        // if we changed the animation -> new animation so we reset animationTick so we start again
        if (startAnimation != playerAction) {
            resetAnimTick();
        }
    }

    private void resetAnimTick() {
        animTick = 0;
        animIndex = 0;
    }

    private void updatePosition() {

        isMoving = false;
        if(jump) {
            jump();
        }

        // if nothing is pressed, then we return, no business of being here
//        if (!left && !right && !isInAir) {
//            return;
//        }
        if (!isInAir) {
            if ((!left && !right) || (left && right))
            {
                return;
            }
        }

        float xSpeed = 0;   // temp var to pass into canMoveHere method (the next position we wanna move to)

        if (left) {
            xSpeed -= playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }

        if(!isInAir) {  // we're just going left or right, not in air
            if(!isEntityOnFloor(hitbox, levelData))
                isInAir = true;

        }

        if (isInAir) {      // in air we have to check for x and y direction collisions

            if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {        // if we can't move up or down, meaning we hit the roof or floor
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) { // if going down & hit floor
                    resetInAir();
                } else {    // if we hit the roof
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }

        } else { // on the ground only x direction collisions
            updateXPos(xSpeed);
        }
        isMoving = true;


        // this checks if we can move to the next position (current + speed)
//        if (canMoveHere(hitbox.x+xSpeed, hitbox.y+ySpeed, hitbox.width, hitbox.height, levelData)){
//            hitbox.x += xSpeed;
//            hitbox.y += ySpeed;
//            isMoving = true;
//        }
    }

    private void jump() {
        if(isInAir) { // if already in air, don't jump again, return
            return;
        }
        isInAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        isInAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {

        if (canMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)){
            hitbox.x += xSpeed;
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }

    }

    private void loadAnimations() {     // image input is handled with utilz LoadSafe class

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[16][13];       // [y][x]

        for (int y = 0; y < animations.length; y++) {
            for (int x = 0; x < animations[y].length; x++) {
                animations[y][x] = img.getSubimage(x * 64, y * 64, 64, 64);
            }
        }
    }

    public void loadLevelData(int [][] levelData) {
        this.levelData = levelData;
        if(!isEntityOnFloor(hitbox, levelData))
            isInAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setJump(boolean jump) {
        this.jump = jump;

    }

}
