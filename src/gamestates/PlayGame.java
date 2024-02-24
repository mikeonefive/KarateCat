package gamestates;

import entities.Player;
import levels.LevelManager;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayGame extends State implements StateMethods {     // in here we have the playing scene that we're currently playing
    private Player player;
    private LevelManager levelManager;

    public PlayGame(Game game) {
        super(game);
        initClasses();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());

    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        player.render(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseRemoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;

            case KeyEvent.VK_K:
                player.setAttacking(true);
                break;

            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;

            case KeyEvent.VK_BACK_SPACE:
                GameState.state = GameState.MENU;
                break;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;


            case KeyEvent.VK_K:
                // player.setAttacking(false);
                break;

            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    // getter
    public Player getPlayer() {
        return player;
    }
}
