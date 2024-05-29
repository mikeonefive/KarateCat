package ui;

import com.studiohartman.jamepad.ControllerState;
import gamestates.GameState;
import gamestates.PlayGame;
import inputs.GamepadInput;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.RSMButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelCompleteOverlay {

    private PlayGame playGame;
    private RsmButton menu, next;
    private BufferedImage image;
    private int bgX, bgY, bgWidth, bgHeight;

    private GamepadInput gamepadInput;
    // Cooldown for gamepad input to avoid choppy navigation
    private long lastInputTime;
    private final long inputCooldown = 200; // 200 milliseconds cooldown


    public LevelCompleteOverlay(PlayGame playGame, GamepadInput gamepadInput) {
        this.playGame = playGame;
        this.gamepadInput = gamepadInput;
        initImage();
        initButtons();
        lastInputTime = System.currentTimeMillis(); // this is for the gamepad so it doesn't update the button state too fast
    }

    private void initButtons() {
        int nextX = (int)(305* Game.SCALE);
        int nextY = (int)(195 * Game.SCALE);

        int menuX = (int)(330 * Game.SCALE);
        int menuY = (int)(222 * Game.SCALE);

        next = new RsmButton(nextX, nextY, RSM_WIDTH, RSM_HEIGHT, 3);
        next.setName("Level complete Button");  // this is to scale them because there are 2 draw commands (1 for default, 1 for scaled)
        next.setMouseOver(true); // default button highlighted

        menu = new RsmButton(menuX, menuY, RSM_WIDTH, RSM_HEIGHT, 2);
        menu.setName("Level complete Button");
    }

    private void initImage() {
        image = LoadSave.getSpriteAtlas(LoadSave.LEVEL_COMPLETE_IMG);
        bgWidth = (int)(image.getWidth() * Game.SCALE);
        bgHeight = (int)(image.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int)(75 * Game.SCALE);
    }

    public void draw(Graphics g) {

        g.drawImage(image, bgX, bgY, bgWidth, bgHeight, null);

        next.draw(g);
        menu.draw(g);
    }

    public void update() {
        handleGamepadInput();

        next.update();
        menu.update();
    }

    private boolean isIn(RsmButton button, MouseEvent event) {
        return button.getBoundaries().contains(event.getX(), event.getY());
    }

    public void mouseMoved(MouseEvent e) {

        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e)) {
            menu.setMouseOver(true);
        } else if (isIn(next, e)) {
            next.setMouseOver(true);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {

            if (menu.isMousePressed()) {
                playGame.resetAll();
                playGame.setGameState(GameState.MENU);
            }
        } else if (isIn(next, e)) {
            if (next.isMousePressed()) {
                playGame.loadNextLevel();
                playGame.getGame().getAudioPlayer().setSongForLevel(playGame.getLevelManager().getLevelIndex());
            }
        }

        menu.resetMouseOverAndMousePressed();
        next.resetMouseOverAndMousePressed();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        } else if (isIn(next, e)) {
            next.setMousePressed(true);
        }
    }

    public void handleGamepadInput() {

        ControllerState buttonPressed = gamepadInput.getButtonPressed();
        long currentTime = System.currentTimeMillis();


        if (currentTime - lastInputTime >= inputCooldown) {
            if (buttonPressed.dpadDown && next.isMouseOver() || buttonPressed.dpadUp && next.isMouseOver()) {
                menu.setMouseOver(true);
                next.setMouseOver(false);
                lastInputTime = currentTime;

            } else if (buttonPressed.dpadDown && menu.isMouseOver() || buttonPressed.dpadUp && menu.isMouseOver()) {
                next.setMouseOver(true);
                menu.setMouseOver(false);
                lastInputTime = currentTime;
            }


            if ((buttonPressed.a || buttonPressed.b) && next.isMouseOver()) {
                playGame.loadNextLevel();
                playGame.getGame().getAudioPlayer().setSongForLevel(playGame.getLevelManager().getLevelIndex());
            }

            else if ((buttonPressed.a || buttonPressed.b) && menu.isMouseOver()) {
                playGame.resetAll();
                playGame.setGameState(GameState.MENU);

            }
        }

    }

}
