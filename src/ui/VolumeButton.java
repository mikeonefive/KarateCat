package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {

    private BufferedImage[] images;
    private BufferedImage slider;
    private int index = 0;
    private boolean isMouseOver, isMousePressed;
    private int buttonX, minX, maxX;

    public VolumeButton(int x, int y, int width, int height) {
        // x + width / 2 because we want slider in the middle
        super(x + width / 2, y, VOLUME_DEFAULT_WIDTH, height);
        boundaries.x -= VOLUME_DEFAULT_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;

        minX = x + VOLUME_DEFAULT_WIDTH / 2;
        maxX = x + width - VOLUME_DEFAULT_WIDTH / 2;

        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        images = new BufferedImage[3];

        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;

        if (isMouseOver()) {
            index = 1;
        }
        if (isMousePressed())
            index = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(images[index], buttonX - VOLUME_DEFAULT_WIDTH / 2, y, VOLUME_DEFAULT_WIDTH, height, null);
    }

    public void changeX(int x) {
        if (x < minX) {
            buttonX = minX;
        } else if (x > maxX) {
            buttonX = maxX;
        } else buttonX = x;

        boundaries.x = buttonX - VOLUME_DEFAULT_WIDTH / 2;
    }

    public void resetMouseOverAndMousePressed() {
        isMouseOver = false;
        isMousePressed = false;
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
}



