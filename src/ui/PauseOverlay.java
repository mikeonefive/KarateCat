package ui;

import gamestates.GameState;
import gamestates.PlayGame;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.RSMButtons.*;


public class PauseOverlay {

    private PlayGame isPlaying;
    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private AudioOptions audioOptions;
    private RsmButton menuButton, playAgainButton, resumeButton;


    public PauseOverlay(PlayGame isPlaying) {

        this.isPlaying = isPlaying;
        
        loadBackground();

        audioOptions = isPlaying.getGame().getAudioOptions();

        createRsmButtons();


    }



    private void createRsmButtons() {

        int buttonX = (int)(397 * Game.SCALE);
        int menuY = (int)(235 * Game.SCALE);

        int playAgainY = (int)(250 * Game.SCALE);

        int resumeY = (int)(265 * Game.SCALE);

        menuButton = new RsmButton(buttonX, menuY, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, 2);
        playAgainButton = new RsmButton(buttonX, playAgainY, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, 1);
        resumeButton = new RsmButton(buttonX, resumeY, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, 0);
    }


    private void loadBackground() {

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = backgroundImg.getWidth();
        backgroundHeight = backgroundImg.getHeight();
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = 100;

    }

    public void update() {

        menuButton.update();
        playAgainButton.update();
        resumeButton.update();

        audioOptions.update();

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
                isPlaying.setGameState(GameState.MENU);
                isPlaying.resumeGame();
            }
        } else if (isIn(e, playAgainButton)) {
            if (playAgainButton.isMousePressed()) {
                isPlaying.resetAll();
                isPlaying.resumeGame();
            }
        } else if (isIn(e, resumeButton)) {
            if (resumeButton.isMousePressed()) {
                isPlaying.resumeGame();
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
