package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverScreen;
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

    private int xLevelOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH); // 20% of the game width = left border (if player is at 18% we need to move level to the left)
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
    private int levelTilesWide = LoadSave.getLevelData()[0].length;
    private int maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH; // TILES_IN_WIDTH are the tiles we can see on screen
    private int maxLevelOffset = maxTilesOffset * Game.TILES_SIZE;


    private BufferedImage backgroundImg, bigCloudImg, smallCloudImg;
    private int[] smallCloudPosition;
    private Random random = new Random();

    private GameOverScreen gameOverScreen;
    private boolean isGameOver;

    private PauseOverlay pauseOverlay;
    private boolean isGamePaused = false;

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
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);

        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());

        gameOverScreen = new GameOverScreen(this);

        pauseOverlay = new PauseOverlay(this);
    }

    @Override
    public void update() {

        if (isGamePaused) {
            pauseOverlay.update();
        } else if (!isGameOver) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
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

        if (xLevelOffset > maxLevelOffset) {
            xLevelOffset = maxLevelOffset;
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


        if (isGamePaused) {
            pauseOverlay.draw(g);
        }

        if (isGameOver) {
            gameOverScreen.draw(g);
        }

    }

    private void drawClouds(Graphics g) {

        for (int i = 0; i < 3; i++) {       // the xLevelOffset calculation here makes sure the clouds move at different speeds
            g.drawImage(bigCloudImg, i * BIG_CLOUD_WIDTH - (int)(xLevelOffset * 0.3) , (int) (219 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }

        for (int i = 0; i < smallCloudPosition.length; i++) {
            g.drawImage(smallCloudImg, SMALL_CLOUD_WIDTH * 4 * i - (int)(xLevelOffset * 0.7), smallCloudPosition[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }


    }

    public void resetAll() {

        isGameOver = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void checkIfEnemyHitByPlayer(Rectangle2D.Float attackBoxPlayer) {
        enemyManager.checkIfEnemyWasHit(attackBoxPlayer);
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
        if (isGamePaused)
            pauseOverlay.mouseDragged(e);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isGamePaused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isGamePaused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isGamePaused) {
            pauseOverlay.mouseMoved(e);
        }

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
}
