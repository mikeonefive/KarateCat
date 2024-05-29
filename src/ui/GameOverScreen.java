package ui;

import com.studiohartman.jamepad.ControllerState;
import gamestates.GameState;
import gamestates.PlayGame;
import inputs.GamepadInput;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.GameOverButtons.*;

public class GameOverScreen {

    private PlayGame playingGame;
    private BufferedImage image;
    private int imgX, imgY, imgWidth, imgHeight;
    private GameOverButton backToMenu, tryAgain;

    // Gamepad input
    private final GamepadInput gamepadInput;
    // Cooldown for gamepad input to avoid choppy navigation
    private long lastInputTime;
    private final long inputCooldown = 200;


    public GameOverScreen(PlayGame playingGame, GamepadInput gamepadInput) {

        this.playingGame = playingGame;
        this.gamepadInput = gamepadInput;

        createImage();
        createButtons();

        lastInputTime = System.currentTimeMillis();

    }

    private void createButtons() {
        int menuX = (int) (322 * Game.SCALE);
        int tryAgainX = (int) (427 * Game.SCALE);
        int y = (int) (235 * Game.SCALE);
        tryAgain = new GameOverButton(tryAgainX, y, GAMEOVER_WIDTH, GAMEOVER_HEIGHT, 0);
        backToMenu = new GameOverButton(menuX, y, GAMEOVER_WIDTH, GAMEOVER_HEIGHT, 1);

        tryAgain.setMouseOver(true); // default highlighted button


    }

    private void createImage() {
        image = LoadSave.getSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgWidth = (int)(image.getWidth() * Game.SCALE);
        imgHeight = (int)(image.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgWidth / 2; // makes sure image is in the middle
        imgY = (int)(100 * Game.SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));    // 200 is the transparent value
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(image, imgX, imgY, imgWidth, imgHeight, null);

        backToMenu.draw(g);
        tryAgain.draw(g);

//        g.setColor(Color.white);
//        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
//        g.drawString("Press ESC to go to the Main Menu!", Game.GAME_WIDTH / 2, 300);
    }

    public void keyPressed(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//            playingGame.resetAll();
//            GameState.state = GameState.MENU;
//        }
    }

    public void update() {
        backToMenu.update();
        tryAgain.update();
        handleGamepadInput();
    }

    private boolean isIn(GameOverButton button, MouseEvent event) {
        return button.getBoundaries().contains(event.getX(), event.getY());
    }


    public void mouseMoved(MouseEvent e) {

        tryAgain.setMouseOver(false);
        backToMenu.setMouseOver(false);


        if (isIn(backToMenu, e)) {
            backToMenu.setMouseOver(true);
        } else if (isIn(tryAgain, e)) {
            tryAgain.setMouseOver(true);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(backToMenu, e)) {

            if (backToMenu.isMousePressed()) {
                playingGame.resetAll();
                playingGame.setGameState(GameState.MENU);
            }
        } else if (isIn(tryAgain, e)) {
            if (tryAgain.isMousePressed()) {
                playingGame.resetAll();
                playingGame.getGame().getAudioPlayer().setSongForLevel(playingGame.getLevelManager().getLevelIndex());
            }
        }

        backToMenu.resetMouseOverAndMousePressed();
        tryAgain.resetMouseOverAndMousePressed();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(backToMenu, e)) {
            backToMenu.setMousePressed(true);
        } else if (isIn(tryAgain, e)) {
            tryAgain.setMousePressed(true);
        }
    }

    public void handleGamepadInput() {

        ControllerState buttonPressed = gamepadInput.getButtonPressed();
        long currentTime = System.currentTimeMillis();


        if (currentTime - lastInputTime >= inputCooldown) {
            if (buttonPressed.dpadLeft && backToMenu.isMouseOver() || buttonPressed.dpadRight && backToMenu.isMouseOver()) {
                tryAgain.setMouseOver(true);
                backToMenu.setMouseOver(false);
                lastInputTime = currentTime;

            } else if (buttonPressed.dpadLeft && tryAgain.isMouseOver() || buttonPressed.dpadRight && tryAgain.isMouseOver()) {
                backToMenu.setMouseOver(true);
                tryAgain.setMouseOver(false);
                lastInputTime = currentTime;
            }


            if ((buttonPressed.a || buttonPressed.b) && tryAgain.isMouseOver()) {
                playingGame.resetAll();
                playingGame.getGame().getAudioPlayer().setSongForLevel(playingGame.getLevelManager().getLevelIndex());
            }

            else if ((buttonPressed.a || buttonPressed.b) && backToMenu.isMouseOver()) {
                playingGame.resetAll();
                playingGame.setGameState(GameState.MENU);

            }
        }

    }

}
