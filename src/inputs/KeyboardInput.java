package inputs;

import gamestates.GameState;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInput(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch(GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;

            case PLAYGAME:
                gamePanel.getGame().getPlayGame().keyPressed(e);
                break;

            case OPTIONS:
                gamePanel.getGame().getGameOptions().keyPressed(e);
                break;

            default:
                break;
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;

            case PLAYGAME:
                gamePanel.getGame().getPlayGame().keyReleased(e);
                break;

            default:
                break;
        }

    }
}
