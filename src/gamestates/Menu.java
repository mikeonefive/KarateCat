package gamestates;

import com.studiohartman.jamepad.ControllerState;
import inputs.GamepadInput;
import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Menu extends State implements StateMethods {

    private final MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundBoard, backgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;
    private final GamepadInput gamepadInput;
    private int currentButtonIndex = 0;  // Track the currently selected button

    // Cooldown for gamepad input to avoid choppy navigation
    private long lastInputTime;
    private final long inputCooldown = 200; // 200 milliseconds cooldown


    public Menu(Game game, GamepadInput gamepadInput) {
        super(game);
        loadBackground();
        loadButtons();

        this.gamepadInput = gamepadInput;
        lastInputTime = System.currentTimeMillis(); // this is for the gamepad so it doesn't update the button state too fast

    }

    private void loadBackground() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);

        backgroundBoard = LoadSave.getSpriteAtlas(LoadSave.MENU_BOARD);
        menuWidth = (int)(backgroundBoard.getWidth() * 1.2);
        menuHeight = (int)(backgroundBoard.getHeight() * 1.2);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton((int)(Game.GAME_WIDTH / 2), (int) (205 * Game.SCALE), 0, GameState.PLAYGAME);
        buttons[0].setMouseOver(true); // this is for the gamepad input default selection at the start
        buttons[1] = new MenuButton((int)(Game.GAME_WIDTH / 2), (int) (230 * Game.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton((int)(Game.GAME_WIDTH / 2), (int) (255 * Game.SCALE), 2, GameState.QUIT);
    }

    @Override
    public void update() {

        handleGamepadInput();

        for (MenuButton menuButton : buttons) {
            menuButton.update();
        }

    }

    @Override
    public void handleGamepadInput() {

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

            if (buttonPressed.a) {
                // PROBLEM!!! with the A button is here because we change to the
                // play state right away (menu is only there for some millisecs)
                buttons[currentButtonIndex].applyGameState();
                lastInputTime = currentTime;

                // change song for the new level
                if (buttons[currentButtonIndex].getState() == GameState.PLAYGAME)
                    game.getAudioPlayer().setSongForLevel(game.getPlayGame().getLevelManager().getLevelIndex());

            }



        }




    }

    private void updateButtonSelection() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setMouseOver(i == currentButtonIndex);
        }
    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundBoard, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton menuButton : buttons) {
            menuButton.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (isInsideButton(e, menuButton)) {
                menuButton.setMousePressed(true);
                break;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (isInsideButton(e, menuButton)) {
                if (menuButton.isMousePressed())
                    menuButton.applyGameState();
                if (menuButton.getState() == GameState.PLAYGAME)
                    game.getAudioPlayer().setSongForLevel(game.getPlayGame().getLevelManager().getLevelIndex());
                break;

            }
        }

        resetButtons();

    }



    private void resetButtons() {
        for (MenuButton menuButton : buttons) {
            menuButton.resetBools();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            menuButton.setMouseOver(false);
        }

        for (MenuButton menuButton : buttons) {
            if (isInsideButton(e, menuButton)) {
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
