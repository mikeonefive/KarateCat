package gamestates;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundBoard, backgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;


    public Menu(Game game) {
        super(game);
        loadBackground();
        loadButtons();

    }

    private void loadBackground() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);

        // CHANGE THAT TO NEW MENU BACKGROUND!
        backgroundBoard = LoadSave.getSpriteAtlas(LoadSave.MENU_BOARD);
        menuWidth = (int)(backgroundBoard.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundBoard.getHeight() * Game.SCALE);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton((int)(Game.GAME_WIDTH / 3.2), (int) (280 * Game.SCALE), 0, GameState.PLAYGAME);
        buttons[1] = new MenuButton((int)(Game.GAME_WIDTH / 3.2), (int) (320 * Game.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton((int)(Game.GAME_WIDTH / 3.2), (int) (360 * Game.SCALE), 2, GameState.QUIT);
    }

    @Override
    public void update() {

        for(MenuButton menuButton : buttons) {
            menuButton.update();
        }

    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        // CHANGE THAT TO NEW MENU BACKGROUND!
        // g.drawImage(backgroundBoard, menuX, menuY, menuWidth, menuHeight, null);

        for(MenuButton menuButton : buttons) {
            menuButton.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton menuButton : buttons) {
            if(isInsideButton(e, menuButton)) {
                menuButton.setMousePressed(true);
                break;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (isInsideButton(e, menuButton)) {
                if (menuButton.isMousePressed()) {
                    menuButton.applyGameState();
                    break;
                }
            }
        }

        resetButtons();

    }

    private void resetButtons() {
        for(MenuButton menuButton : buttons) {
            menuButton.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton menuButton : buttons) {
            menuButton.setMouseOver(false);
        }

        for(MenuButton menuButton : buttons) {
            if(isInsideButton(e, menuButton)) {
                menuButton.setMouseOver(true);
                break;
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYGAME;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
