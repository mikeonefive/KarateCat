package entities;

import gamestates.PlayGame;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import static utilz.Constants.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;

    private boolean left, right, jump;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private int attackType = -1; // Default value indicating no attack

    private int[][] levelData;

    // for the hitbox
    private float xDrawOffset = 23 * Game.SCALE;
    private float yDrawOffset = 25 * Game.SCALE;

    // jumping/gravity
    private float jumpSpeed = -2.50f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;


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

    private int healthWidth = lifeBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    private boolean checkedAttackAlready;

    private PlayGame playGame;

    private int tileY = 0;

    public Player(float x, float y, int width, int height, PlayGame playGame) {
        super(x, y, width, height);        // we take in x and y and pass them over to the Entity class where they are stored
        this.playGame = playGame;
        this.state = IDLE;

        this.walkSpeed = 1.0f * Game.SCALE;

        this.maxHealth = 100;
        this.currentHealth = maxHealth;

        loadAnimations();

        initHitbox((int)(12 * Game.SCALE), (int)(28 * Game.SCALE));

        initAttackBox();
    }

    public void setSpawnPosition(Point position) {
        this.x = position.x;
        this.y = position.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update() {

        updateHealthBar();

        // check if player dead?
        if (currentHealth <= 0) {
            if (state != DEAD) {
                state = DEAD;
                animationTick = 0;
                animationIndex = 0;
                playGame.setPlayerDying(true);
            } else if (animationIndex == getSpriteAmount(DEAD) - 1 && animationTick >= ANIMATION_SPEED - 1) {
                playGame.setGameOver(true);
            } else
                updateAnimationTick();

           return;
        }

        updateAttackBox();

        updatePosition();

        if (isMoving) {
            checkIfPotionTouched();
            checkIfSpikesTouched();
            checkIfFallenOff();
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }

        if (isAttacking) {
            checkAttack();
        }

        updateAnimationTick();
        setAnimation();

    }

    private void checkIfFallenOff() {
        if (hitbox.y >= 620) {
            kill();
        }
    }

    private void checkIfSpikesTouched() {
        playGame.checkIfSpikesTouched(this);

    }

    private void checkIfPotionTouched() {
        playGame.checkIfPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (checkedAttackAlready || animationIndex != 4) {
            return;
        }
        checkedAttackAlready = true;
        playGame.checkIfEnemyHitByPlayer(attackBox);
        playGame.checkIfObjectHit(attackBox);
        
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

        graphics.drawImage(animations[state][animationIndex],
                (int)(hitbox.x - xDrawOffset) - levelOffset + flipX,
                (int)(hitbox.y - yDrawOffset),
                width * flipW, height, null);   // flipW is -1 when we go to the left so we would flip the image in this case
        // drawHitbox(graphics, levelOffset);

        // drawAttackBox(graphics, levelOffset);

        drawStatusBar(graphics);
    }


    private void drawStatusBar(Graphics graphics) {

        graphics.setColor(Color.red);
        graphics.fillRect(lifeBarXStart + statusBarX, lifeBarYStart + statusBarY, healthWidth, lifeBarHeight);

        graphics.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

    }


    private void updateAnimationTick() {

        animationTick ++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex ++;
            if (animationIndex >= getSpriteAmount(state)) {
                animationIndex = 0;
                isAttacking = false;
                checkedAttackAlready = false;
            }
        }
    }

    public void setAnimation() {

        int startAnimation = state;

        if (isMoving) {
            state = WALK;
        } else {
            state = IDLE;
        }

        if (isInAir) {
            if(airSpeed < 0) {   // are we going up?
                state = JUMP;
            } else
                state = FALL;

        }

        if (isAttacking) {
            switch(attackType) {

                case PUNCH:
                    state = PUNCH;
                    break;

                case ROUNDKICK:
                    state = ROUNDKICK;
                    break;

                case UPPERCUT:
                    state = UPPERCUT;
                    break;

                case SPINKICK:
                    state = SPINKICK;
                    break;
            }

            // Check if the attack animation is new, not already inside the attack cycle
            // then we start with frame 3 -> faster attack (first frames are idle),
            // if we wanna change it for a certain attack type (like frame 4 or sth), we have to do so in the cases above
            if (startAnimation != state) {
                animationIndex = 3;
                animationTick = 0;
                return;
            }

        }

        // if we changed the animation -> new animation so we reset animationTick so we start again
        if (startAnimation != state) {
            resetAnimTick();
        }
    }

    private void resetAnimTick() {
        animationTick = 0;
        animationIndex = 0;
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
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += walkSpeed;
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
                airSpeed += GRAVITY;
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

    public void kill() {
        currentHealth = 0;
    }

    public void updatePower(int value) {
        System.out.println("I feel much stronger now!");
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


    public void setJump(boolean jump) {
        this.jump = jump;

    }

    public void resetAll() {
        resetDirBooleans();
        isInAir = false;
        isAttacking = false;
        isMoving = false;
        state = IDLE;
        currentHealth = maxHealth;

        // player starting position at current level
        hitbox.x = x;
        hitbox.y = y;

        if (!isEntityOnFloor(hitbox, levelData)) {
            isInAir = true;
        }
    }

    public int getTileY() {
        return tileY;
    }


}
