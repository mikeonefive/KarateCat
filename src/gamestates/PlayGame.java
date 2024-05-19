package gamestates;

import com.studiohartman.jamepad.ControllerState;
import entities.EnemyManager;
import entities.Player;
import inputs.GamepadInput;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverScreen;
import ui.LevelCompleteOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.BackgroundEnvironment.*;
import static utilz.Constants.PlayerConstants.*;

public class PlayGame extends State implements StateMethods {     // in here we have the playing scene that we're currently playing
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;

    private int xLevelOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH); // 20% of the game width = left border (if player is at 18% we need to move level to the left)
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
    private int maxLevelOffsetX;


    private BufferedImage backgroundImg, bigCloudImg, smallCloudImg;
    private int[] smallCloudPosition;
    private Random random = new Random();

    private GameOverScreen gameOverScreen;
    private boolean isGameOver;

    private PauseOverlay pauseOverlay;
    private boolean isGamePaused = false;

    private LevelCompleteOverlay levelCompleteOverlay;
    private boolean isLevelComplete = false;

    private boolean isPlayerDying;

    public GamepadInput gamepadInput;


    public PlayGame(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PLAYGAME_BACKGROUND);
        bigCloudImg = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloudImg = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPosition = new int[8];
        for (int i = 0; i < smallCloudPosition.length; i++) {
            smallCloudPosition[i] = (int)(70 * Game.SCALE) + random.nextInt((int)(150 * Game.SCALE));

        }
        calculateLevelOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawnPosition(levelManager.getCurrentLevel().getPlayerSpawnCoordinates());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calculateLevelOffset() {

        maxLevelOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLvlData());

        player.setSpawnPosition(levelManager.getCurrentLevel().getPlayerSpawnCoordinates());

        gameOverScreen = new GameOverScreen(this);

        pauseOverlay = new PauseOverlay(this);

        levelCompleteOverlay = new LevelCompleteOverlay(this);

        gamepadInput = new GamepadInput(game.getGamePanel());

    }

    @Override
    public void update() {

        if (isGamePaused) {
            pauseOverlay.update();

        } else if (isLevelComplete) {
            levelCompleteOverlay.update();

        } else if (isGameOver) {
            gameOverScreen.update();

        } else if (isPlayerDying) {
            player.update();

        } else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            checkCloseToBorder();
            handleGamepadInput();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int difference = playerX - xLevelOffset;

        if (difference > rightBorder) {
            xLevelOffset += difference - rightBorder;
        } else if (difference < leftBorder) {
            xLevelOffset += difference - leftBorder;
        }

        if (xLevelOffset > maxLevelOffsetX) {
            xLevelOffset = maxLevelOffsetX;
        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);

        levelManager.draw(g, xLevelOffset);

        player.render(g, xLevelOffset);

        enemyManager.draw(g, xLevelOffset);

        objectManager.draw(g, xLevelOffset);


        if (isGamePaused) {
            pauseOverlay.draw(g);
        }

        if (isGameOver) {
            gameOverScreen.draw(g);
        }

        if (isLevelComplete)
            levelCompleteOverlay.draw(g);

    }

    private void drawClouds(Graphics g) {

        for (int i = 0; i < 5; i++) {       // the xLevelOffset calculation here makes sure the clouds move at different speeds
            g.drawImage(bigCloudImg, i * BIG_CLOUD_WIDTH - (int)(xLevelOffset * 0.3) , (int) (219 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }

        for (int i = 0; i < smallCloudPosition.length; i++) {
            g.drawImage(smallCloudImg, SMALL_CLOUD_WIDTH * 4 * i - (int)(xLevelOffset * 0.7), smallCloudPosition[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }


    }

    public void resetAll() {

        isGameOver = false;
        isGamePaused = false;
        isLevelComplete = false;
        isPlayerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
        
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void checkIfObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkIfObjectWasHit(attackBox);
    }


    public void checkIfEnemyHitByPlayer(Rectangle2D.Float attackBoxPlayer) {
        enemyManager.checkIfEnemyWasHit(attackBoxPlayer);
    }

    public void checkIfPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkIfObjectTouched(hitbox);
    }

    public void checkIfSpikesTouched(Player player) {
        objectManager.checkIfSpikesTouched(player);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

//        if (!isGameOver) {
//            if (e.getButton() == MouseEvent.BUTTON1) {
//                player.setAttacking(true, SPINKICK);
//            }
//        }

    }

    public void mouseDragged(MouseEvent e) {
        if (!isGameOver) {
            if (isGamePaused)
                pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!isGameOver) {
            if (isGamePaused) {
                pauseOverlay.mousePressed(e);
            }
            if (isLevelComplete) {
                levelCompleteOverlay.mousePressed(e);
            }

        } else
            gameOverScreen.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isGameOver) {
            if (isGamePaused) {
                pauseOverlay.mouseReleased(e);
            }

            if (isLevelComplete) {
                levelCompleteOverlay.mouseReleased(e);
            }
        } else
            gameOverScreen.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isGameOver) {
            if (isGamePaused) {
                pauseOverlay.mouseMoved(e);
            }

            if (isLevelComplete) {
                levelCompleteOverlay.mouseMoved(e);
            }
        } else
            gameOverScreen.mouseMoved(e);

    }

    public void setLevelComplete(boolean isLevelComplete) {
        this.isLevelComplete = isLevelComplete;
    }

    public void setMaxLevelOffset(int levelOffset) {
        this.maxLevelOffsetX = levelOffset;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (isGameOver) {
            gameOverScreen.keyPressed(e);
        } else {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;

                case KeyEvent.VK_K:
                    player.setAttacking(true, PUNCH);
                    break;
                case KeyEvent.VK_L:
                    player.setAttacking(true, UPPERCUT);
                    break;
                case KeyEvent.VK_I:
                    player.setAttacking(true, ROUNDKICK);
                    break;
                case KeyEvent.VK_O:
                    player.setAttacking(true, SPINKICK);
                    break;


                case KeyEvent.VK_ENTER:
                    player.setJump(true);
                    break;

                case KeyEvent.VK_BACK_SPACE:
                    GameState.state = GameState.MENU;
                    break;

                case KeyEvent.VK_ESCAPE:
                    isGamePaused = !isGamePaused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (!isGameOver) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;


                case KeyEvent.VK_K:
                    // player.setAttacking(false);
                    break;

                case KeyEvent.VK_ENTER:
                    player.setJump(false);
                    break;
            }

        }
    }

    @Override
    public void handleGamepadInput() {

        ControllerState currState = gamepadInput.getControllers().getState(0);

        player.setJump(false);
        player.resetDirBooleans();

        // buttons
        if (currState.b)
            player.setAttacking(true, ROUNDKICK);
        else if (currState.y)
            player.setAttacking(true, SPINKICK);
        else if (currState.x)
            player.setAttacking(true, PUNCH);
        else if (currState.a)
            player.setAttacking(true, UPPERCUT);

        // jump
        else if (currState.rb)
            player.setJump(true);

        // directions
        if (currState.dpadRight || currState.leftStickX > 0.5)
            player.setRight(true);
        if (currState.dpadLeft || currState.leftStickX < -0.5)
            player.setLeft(true);

    }


    public void resumeGame() {
        isGamePaused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    // getter
    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }


    public void setPlayerDying(boolean isPlayerDying) {
        this.isPlayerDying = isPlayerDying;
    }
}
