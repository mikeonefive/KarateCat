package ui;

import com.studiohartman.jamepad.ControllerState;
import gamestates.GameState;
import gamestates.PlayGame;
import inputs.GamepadInput;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.RSMButtons.*;


public class PauseOverlay {

    private PlayGame playGame;
    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private AudioOptions audioOptions;
    private RsmButton menuButton, playAgainButton, resumeButton;
    private RsmButton[] buttons = new RsmButton[3];

    private final GamepadInput gamepadInput;
    private int currentButtonIndex = 0;  // Track the currently selected button

    // Cooldown for gamepad input to avoid choppy navigation
    private long lastInputTime;
    private final long inputCooldown = 200; // 200 milliseconds cooldown




    public PauseOverlay(PlayGame playGame, GamepadInput gamepadInput) {

        this.playGame = playGame;

        loadBackground();

        audioOptions = playGame.getGame().getAudioOptions();

        createRsmButtons();

        this.gamepadInput = gamepadInput;
        lastInputTime = System.currentTimeMillis(); // this is for the gamepad so it doesn't update the button state too fast

    }



    private void createRsmButtons() {

        int buttonX = (int)(397 * Game.SCALE);
        int menuY = (int)(235 * Game.SCALE);

        int playAgainY = (int)(250 * Game.SCALE);

        int resumeY = (int)(265 * Game.SCALE);


        resumeButton = new RsmButton(buttonX, resumeY, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, 0);
        playAgainButton = new RsmButton(buttonX, playAgainY, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, 1);
        menuButton = new RsmButton(buttonX, menuY, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, 2);
        menuButton.setMouseOver(true);

        // buttons array to make the handleGamepadInput method more dynamic
        buttons[0] = menuButton;
        buttons[1] = playAgainButton;
        buttons[2] = resumeButton;
    }


    private void loadBackground() {

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = backgroundImg.getWidth();
        backgroundHeight = backgroundImg.getHeight();
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = 100;

    }

    public void update() {

        handleGamepadInput();

        menuButton.update();
        playAgainButton.update();
        resumeButton.update();

        audioOptions.update();

    }

    private void handleGamepadInput() {

        ControllerState buttonPressed = gamepadInput.getButtonPressed();
        long currentTime = System.currentTimeMillis();


        if (currentTime - lastInputTime >= inputCooldown) {
            if (buttonPressed.dpadDown) {
                currentButtonIndex = (currentButtonIndex + 1) % buttons.length;
                updateButtonSelection();
                lastInputTime = currentTime;

            } else if (buttonPressed.dpadUp) {
                currentButtonIndex = (currentButtonIndex - 1 + buttons.length) % buttons.length;
                updateButtonSelection();
                lastInputTime = currentTime;

            }

            // menu button pressed
            if ((buttonPressed.b || buttonPressed.a) && menuButton.isMouseOver()) {
                    playGame.setGameState(GameState.MENU);
                    playGame.resumeGame();
                    lastInputTime = currentTime;

            }

            // start over button pressed
            if ((buttonPressed.a) && playAgainButton.isMouseOver()) {
                playGame.resetAll();
                playGame.resumeGame();
                lastInputTime = currentTime;

            }

            // resume button pressed
            if ((buttonPressed.a) && resumeButton.isMouseOver()) {
                playGame.resumeGame();
                lastInputTime = currentTime;
            }

        }

    }

    private void updateButtonSelection() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setMouseOver(i == currentButtonIndex);
        }
    }

    public void draw(Graphics g) {

        // Background
        g.drawImage(backgroundImg, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        // resume, play again, menu Buttons
        menuButton.draw(g);
        playAgainButton.draw(g);
        resumeButton.draw(g);

        audioOptions.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }


    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isIn(e, playAgainButton)) {
            playAgainButton.setMousePressed(true);
        } else if (isIn(e, resumeButton)) {
            resumeButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }


    public void mouseReleased(MouseEvent e) {

        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                playGame.setGameState(GameState.MENU);
                playGame.resumeGame();
            }
        } else if (isIn(e, playAgainButton)) {
            if (playAgainButton.isMousePressed()) {
                playGame.resetAll();
                playGame.resumeGame();
            }
        } else if (isIn(e, resumeButton)) {
            if (resumeButton.isMousePressed()) {
                playGame.resumeGame();
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuButton.resetMouseOverAndMousePressed();
        playAgainButton.resetMouseOverAndMousePressed();
        resumeButton.resetMouseOverAndMousePressed();

    }


    public void mouseMoved(MouseEvent e) {

        menuButton.setMouseOver(false);
        playAgainButton.setMouseOver(false);
        resumeButton.setMouseOver(false);

        if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isIn(e, playAgainButton)) {
            playAgainButton.setMouseOver(true);
        } else if (isIn(e, resumeButton)) {
            resumeButton.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }



    // PauseButton is the super class of all the buttons here
    private boolean isIn(MouseEvent e, PauseButton button) {

        return button.getBoundaries().contains(e.getX(), e.getY());

    }

}
