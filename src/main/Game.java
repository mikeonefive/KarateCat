package main;

import entities.Player;
import levels.LevelManager;

import java.awt.*;

// runnable is a method we pass into the thread, so thread knows what to do and runs that
// we pass that code into the thread with runnable
public class Game implements Runnable
{
    // game window object
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoop;
    private final int FPS_SET = 120;
    private final int UPS_SET = 130;        // updates per second

    private Player player;
    private LevelManager levelManager;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


    // constructor = head method of the class, whenever we create
    // object of this Game class we execute code that is in here
    public Game()
    {

        initClasses();      // initialize player, enemies etc.

        // create gameWindow object inside of Game
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();

    }

    private void initClasses() {
        levelManager = new LevelManager(this);
        player = new Player(200, 200, (int) (64 * SCALE), (int) (64 * SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());

    }

    private void startGameLoop() {
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void update() {
        player.update();
        levelManager.update();
    }

    public void render(Graphics g) {

        levelManager.draw(g);
        player.render(g);

    }

    @Override
    public void run() {

        double timePerFrame = 1_000_000_000 / FPS_SET; // how long each frame lasts
        double timePerUpdate = 1_000_000_000 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;

        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;      // this is used so we don't lose any time like 1 ms or so
        double deltaF = 0;      // F = frame

        while (true) {
            long currentTime = System.nanoTime();

            // update the game if deltaU is more or equal to 1
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates ++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // if now minus last time we had a frame is equal or more than 1 sec
//            if (now - lastFrame >= timePerFrame) {
//                gamePanel.repaint();
//                lastFrame = now;
//                frames++;
//            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.printf("FPS: %s | UPS: %s\n", frames, updates);
                frames = 0;
                updates = 0;
            }

        }
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    // getter
    public Player getPlayer() {
        return player;
    }


}
