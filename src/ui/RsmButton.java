package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.RSMButtons.*;

public class RsmButton extends PauseButton {

    private BufferedImage[] images;
    private int rowIndex, index;
    private boolean isMouseOver, isMousePressed;
    private String name;


    public RsmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.RSM_BUTTONS);

        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * RSM_DEFAULT_WIDTH, rowIndex * RSM_DEFAULT_HEIGHT, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT);

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
        // 2 different sizes for the buttons because some of 'em have to be scaled
        if (this.getName() != null) {
            g.drawImage(images[index], x, y, RSM_WIDTH, RSM_HEIGHT, null);
        } else
            g.drawImage(images[index], x, y, RSM_DEFAULT_WIDTH, RSM_DEFAULT_HEIGHT, null);
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


    // this is for the levelCompleteButtons because they should be scaled
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
