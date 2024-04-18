package ui;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class PauseOverlay {

    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;


    private SoundButton musicButton, sfxButton;

    public PauseOverlay() {

        loadBackground();
        createSoundButtons();

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

    }

    public void draw(Graphics g) {

        // Background
        g.drawImage(backgroundImg, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        // Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

    }

    public void mouseDragged(MouseEvent e) {

    }


    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
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
        }

        musicButton.resetMouseOverAndMousePressed();
        sfxButton.resetMouseOverAndMousePressed();

    }


    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
    }

    // PauseButton is the super class of all the buttons here
    private boolean isIn(MouseEvent e, PauseButton button) {

        return button.getBoundaries().contains(e.getX(), e.getY());

    }

}
