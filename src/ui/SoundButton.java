package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImages;
    private boolean isMouseOver, isMousePressed;
    private boolean isMuted;
    private int rowIndex, colIndex;


    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImages();
    }

    private void loadSoundImages() {

        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImages = new BufferedImage[2][3];

        for (int y = 0; y < soundImages.length; y++) {
            for (int x = 0; x < soundImages[y].length; x++) {

                soundImages[y][x] = temp.getSubimage(x * SOUND_WIDTH_DEFAULT, y * SOUND_HEIGHT_DEFAULT, SOUND_WIDTH_DEFAULT, SOUND_HEIGHT_DEFAULT);

            }
        }
    }

    public void update() {

        if (isMuted) {
            rowIndex = 1;
        } else {
            rowIndex = 0;
        }

        colIndex = 0;
        if (isMouseOver) {
            colIndex = 1;
        }
        if (isMousePressed) {
            colIndex = 2;
        }

    }

    public void resetMouseOverAndMousePressed() {
        isMouseOver = false;
        isMousePressed = false;
    }

    public void draw(Graphics g) {
        g.drawImage(soundImages[rowIndex][colIndex], x, y, width, height, null);
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        isMousePressed = mousePressed;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}
