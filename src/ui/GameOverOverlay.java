package ui;

import gamestates.GameState;
import gamestates.PlayGame;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {

    private PlayGame playingGame;

    public GameOverOverlay(PlayGame playingGame) {
        this.playingGame = playingGame;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));    // 200 is the transparent value
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press ESC to go to the Main Menu!", Game.GAME_WIDTH / 2, 300);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playingGame.resetAll();
            GameState state = GameState.MENU;
        }
    }

}
