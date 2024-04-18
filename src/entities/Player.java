package entities;

import gamestates.PlayGame;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;

    private int animTick, animIndex, animSpeed = 15;
    private int playerAction = IDLE;

    private boolean up, left, right, down, jump;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private int attackType = -1; // Default value indicating no attack

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

    // status bars UI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(84 * Game.SCALE);
    private int statusBarHeight = (int)(59 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);

    private int lifeBarWidth = (int)(62 * Game.SCALE);
    private int lifeBarHeight = (int)(10 * Game.SCALE);
    private int lifeBarXStart = (int)(16 * Game.SCALE);
    private int lifeBarYStart = (int)(15 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = lifeBarWidth;

    // attackBox, area that the player attacks and if there's an enemy there it gets a can of whoopass
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;

    private boolean checkedAttackAlready;

    private PlayGame playGame;

    public Player(float x, float y, int width, int height, PlayGame playGame) {
        super(x, y, width, height);        // we take in x and y and pass them over to the Entity class where they are stored
        this.playGame = playGame;

        loadAnimations();

        initHitbox(x, y, (int)(12 * Game.SCALE), (int)(28 * Game.SCALE));

        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update() {

        updateHealthBar();

        // check if player dead?
        if (currentHealth <= 0) {
           playGame.setGameOver(true);
           return;
        }

        updateAttackBox();

        updatePosition();
        if (isAttacking) {
            checkAttack();
        }

        updateAnimationTick();
        setAnimation();

    }

    private void checkAttack() {
        if (checkedAttackAlready || animIndex != 4) {
            return;
        }
        checkedAttackAlready = true;
        playGame.checkIfEnemyHitByPlayer(attackBox);
        
    }

    private void updateAttackBox() {

        if (right) {          // Game.SCALE * 5 is the offset we need
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 5);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 5);
        }
        attackBox.y = hitbox.y + Game.SCALE * 10;

    }

    private void updateHealthBar() {

        healthWidth = (int)((currentHealth / (float)maxHealth) * lifeBarWidth);

    }

    public void render(Graphics graphics, int levelOffset) {

        graphics.drawImage(animations[playerAction][animIndex],
                (int)(hitbox.x - xDrawOffset) - levelOffset + flipX,
                (int)(hitbox.y - yDrawOffset),
                width * flipW, height, null);   // flipW is -1 when we go to the left so we would flip the image in this case
        // drawHitbox(graphics, levelOffset);

        drawAttackBox(graphics, levelOffset);

        drawStatusBar(graphics);
    }

    private void drawAttackBox(Graphics graphics, int levelOffsetX) {
        graphics.setColor(Color.red);
        graphics.drawRect((int)attackBox.x - levelOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);

    }

    private void drawStatusBar(Graphics graphics) {

        graphics.setColor(Color.red);
        graphics.fillRect(lifeBarXStart + statusBarX, lifeBarYStart + statusBarY, healthWidth, lifeBarHeight);

        graphics.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

    }


    private void updateAnimationTick() {

        animTick ++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex ++;
            if (animIndex >= getSpriteAmount(playerAction)) {
                animIndex = 0;
                isAttacking = false;
                checkedAttackAlready = false;
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
            switch(attackType) {

                case PUNCH:
                    playerAction = PUNCH;
                    break;

                case ROUNDKICK:
                    playerAction = ROUNDKICK;
                    break;

                case UPPERCUT:
                    playerAction = UPPERCUT;
                    break;

                case SPINKICK:
                    playerAction = SPINKICK;
                    break;
            }

            // Check if the attack animation is new, not already inside the attack cycle
            // then we start with frame 3 -> faster attack (first frames are idle),
            // if we wanna change it for a certain attack type (like frame 4 or sth), we have to do so in the cases above
            if (startAnimation != playerAction) {
                animIndex = 3;
                animTick = 0;
                return;
            }

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
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
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

    public void updateHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0) {
            currentHealth = 0;

            // gameOver();

        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
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

        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
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

    public void setAttacking(boolean isAttacking, int attackType) {
        this.isAttacking = isAttacking;
        this.attackType = attackType;

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

    public void resetAll() {
        resetDirBooleans();
        isInAir = false;
        isAttacking = false;
        isMoving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        // player starting position at current level
        hitbox.x = x;
        hitbox.y = y;

        if (!isEntityOnFloor(hitbox, levelData)) {
            isInAir = true;
        }
    }
}
