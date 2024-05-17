package ui;

import gamestates.GameState;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utilz.Constants.UI.PauseButtons.SOUND_HEIGHT_DEFAULT;
import static utilz.Constants.UI.PauseButtons.SOUND_WIDTH_DEFAULT;
import static utilz.Constants.UI.VolumeButtons.SLIDER_DEFAULT_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_DEFAULT_HEIGHT;

public class AudioOptions {

    private SoundButton musicButton, sfxButton;
    private VolumeButton volumeButton;

    public AudioOptions() {

        createSoundButtons();
        createVolumeButton();

    }

    private void createVolumeButton() {
        int volumeX = (int)(363 * Game.SCALE);
        int volumeY = (int)(210 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int)(420 * Game.SCALE);
        int musicY = (int)(124 * Game.SCALE);

        int sfxY = (int)(150 * Game.SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_WIDTH_DEFAULT, SOUND_HEIGHT_DEFAULT);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_WIDTH_DEFAULT, SOUND_HEIGHT_DEFAULT);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {

        // Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

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
        }

        musicButton.resetMouseOverAndMousePressed();
        sfxButton.resetMouseOverAndMousePressed();
        volumeButton.resetMouseOverAndMousePressed();

    }


    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    // PauseButton is the super class of all the buttons here
    private boolean isIn(MouseEvent e, PauseButton button) {

        return button.getBoundaries().contains(e.getX(), e.getY());

    }

}
