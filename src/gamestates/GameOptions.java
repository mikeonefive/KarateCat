package gamestates;

import com.studiohartman.jamepad.ControllerState;
import inputs.GamepadInput;
import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.RsmButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.RSMButtons.*;

public class GameOptions extends State implements StateMethods {

    // gamepad & cooldown for gamepad
    private GamepadInput gamepadInput;
    private long lastInputTime;
    private final long inputCooldown = 200; // 200 milliseconds cooldown


    private AudioOptions audioOptions;
    private BufferedImage backgroundImage, optionsBackgroundImage;
    private int bgX, bgY, bgWidth, bgHeight;
    private RsmButton menuButton;

    public GameOptions(Game game, GamepadInput gamepadInput) {
        super(game);
        this.gamepadInput = gamepadInput;
        loadImages();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int)(330 * Game.SCALE);
        int menuY = (int)(235 * Game.SCALE);
        menuButton = new RsmButton(menuX, menuY, RSM_WIDTH, RSM_HEIGHT, 2);
        menuButton.setName("menu");
        menuButton.setMouseOver(true);
    }

    private void loadImages() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
        optionsBackgroundImage = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = optionsBackgroundImage.getWidth();
        bgHeight = optionsBackgroundImage.getHeight();
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = 100;
    }

    @Override
    public void update() {

        menuButton.update();
        audioOptions.update();

        handleGamepadInput();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImage, bgX, bgY, bgWidth, bgHeight, null);

        menuButton.draw(g);
        audioOptions.draw(g);
    }


    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuButton.resetMouseOverAndMousePressed();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        menuButton.setMouseOver(false);

        if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            GameState.state = GameState.MENU;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void handleGamepadInput() {

        ControllerState buttonPressed = gamepadInput.getButtonPressed();
        long currentTime = System.currentTimeMillis();


        if (currentTime - lastInputTime >= inputCooldown) {

            if (buttonPressed.back) {
                GameState.state = GameState.MENU;
                lastInputTime = currentTime;
            }

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private boolean isIn(MouseEvent e, PauseButton button) {

        return button.getBoundaries().contains(e.getX(), e.getY());

    }
}
