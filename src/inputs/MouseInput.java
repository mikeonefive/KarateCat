package inputs;

import gamestates.GameState;
import main.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;
    public MouseInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(GameState.state) {

            case PLAYGAME:
                gamePanel.getGame().getPlayGame().mouseClicked(e);
                break;

            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;

            case PLAYGAME:
                gamePanel.getGame().getPlayGame().mousePressed(e);
                break;

            default:
                break;
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {

        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;

            case PLAYGAME:
                gamePanel.getGame().getPlayGame().mouseReleased(e);
                break;

            default:
                break;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;

            case PLAYGAME:
                gamePanel.getGame().getPlayGame().mouseMoved(e);
                break;

            default:
                break;
        }
    }

    }

