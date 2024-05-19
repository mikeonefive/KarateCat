package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.GameOverButtons.*;

public class GameOverButton extends PauseButton {

    private BufferedImage[] images;
    private int rowIndex, index;
    private boolean isMouseOver, isMousePressed;


    public GameOverButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.GAMEOVER_BUTTON_ATLAS);

        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * GAMEOVER_DEFAULT_WIDTH,
                    rowIndex * GAMEOVER_DEFAULT_HEIGHT,
                    GAMEOVER_DEFAULT_WIDTH,
                    GAMEOVER_DEFAULT_HEIGHT);

        }
    }

    public void update() {
        index = 0;

        if (isMouseOver) {
            index = 1;
        }
        if (isMousePressed) {
            index = 0;
        }
    }

    public void draw(Graphics g) {

        g.drawImage(images[index], x, y, GAMEOVER_WIDTH, GAMEOVER_HEIGHT, null);

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
