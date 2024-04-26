package ui;

import gamestates.GameState;
import gamestates.PlayGame;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.RSMButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private PlayGame isPlaying;
    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;


    private SoundButton musicButton, sfxButton;
    private RsmButton menuButton, playAgainButton, resumeButton;
    private VolumeButton volumeButton;

    public PauseOverlay(PlayGame isPlaying) {

        this.isPlaying = isPlaying;
        
        loadBackground();
        createSoundButtons();
        createRsmButtons();
        createVolumeButton();

    }

    private void createVolumeButton() {
        int volumeX = (int)(363 * Game.SCALE);
        int volumeY = (int)(207 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
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

    private void createSoundButtons() {
        int soundX = (int)(420 * Game.SCALE);
        int musicY = (int)(124 * Game.SCALE);

        int sfxY = (int)(150 * Game.SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_WIDTH_DEFAULT, SOUND_HEIGHT_DEFAULT);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_WIDTH_DEFAULT, SOUND_HEIGHT_DEFAULT);
    }

    private void loadBackground() {

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = backgroundImg.getWidth();
        backgroundHeight = backgroundImg.getHeight();
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = 100;

    }

    public void update() {

        musicButton.update();
        sfxButton.update();

        menuButton.update();
        playAgainButton.update();
        resumeButton.update();
        volumeButton.update();

    }

    public void draw(Graphics g) {

        // Background
        g.drawImage(backgroundImg, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        // Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // resume, play again, menu Buttons
        menuButton.draw(g);
        playAgainButton.draw(g);
        resumeButton.draw(g);

        // volume slider
        volumeButton.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }


    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isIn(e, playAgainButton)) {
            playAgainButton.setMousePressed(true);
        } else if (isIn(e, resumeButton)) {
            resumeButton.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }


    public void mouseReleased(MouseEvent e) {

        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }

        } else if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
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
        }
        musicButton.resetMouseOverAndMousePressed();
        sfxButton.resetMouseOverAndMousePressed();

        menuButton.resetMouseOverAndMousePressed();
        playAgainButton.resetMouseOverAndMousePressed();
        resumeButton.resetMouseOverAndMousePressed();
        volumeButton.resetMouseOverAndMousePressed();

    }


    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        menuButton.setMouseOver(false);
        playAgainButton.setMouseOver(false);
        resumeButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);

        } else if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isIn(e, playAgainButton)) {
            playAgainButton.setMouseOver(true);
        } else if (isIn(e, resumeButton)) {
            resumeButton.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    // PauseButton is the super class of all the buttons here
    private boolean isIn(MouseEvent e, PauseButton button) {

        return button.getBoundaries().contains(e.getX(), e.getY());

    }

}
