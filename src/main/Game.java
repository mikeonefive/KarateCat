package main;


import gamestates.GameOptions;
import gamestates.GameState;
import gamestates.Menu;
import gamestates.PlayGame;
import inputs.GamepadInput;
import ui.AudioOptions;


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
    private final int UPS_SET = 150;        // updates per second

    private PlayGame playGame;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private GamepadInput gamepadInput;


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
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();

    }

    private void initClasses() {

        gamepadInput = new GamepadInput(gamePanel);  // Initialize once and share

        audioOptions = new AudioOptions();  // this is created here, so we can use it in pauseScreen and Options but we use same instance

        menu = new Menu(this, gamepadInput);
        playGame = new PlayGame(this, gamepadInput);
        gameOptions = new GameOptions(this, gamepadInput);


    }

    private void startGameLoop() {
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void update() {

        switch(GameState.state) {

            case PLAYGAME:
                playGame.update();
                break;

            case MENU:
                menu.update();
                break;

            case OPTIONS:
                gameOptions.update();
                break;

            case QUIT:
            default:
                cleanup();
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {

        switch(GameState.state) {

            case PLAYGAME:
                playGame.draw(g);
                break;

            case MENU:
                menu.draw(g);
                break;

            case OPTIONS:
                gameOptions.draw(g);
                break;

            default:
                break;
        }

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
                // System.out.printf("FPS: %s | UPS: %s\n", frames, updates);
                frames = 0;
                updates = 0;
            }
        }

    }
    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYGAME) {
            playGame.getPlayer().resetDirBooleans();
        }

    }

    public void cleanup() {
        if (this.getPlayGame() != null && this.getPlayGame().gamepadInput != null) {
            this.getPlayGame().gamepadInput.getControllers().quitSDLGamepad();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public PlayGame getPlayGame() {
        return playGame;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
