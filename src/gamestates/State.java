package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {


    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isInsideButton(MouseEvent e, MenuButton menuButton) {
        return menuButton.getBounds().contains(e.getX(), e.getY());     // returns true if mouse inside of rectangle
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(GameState state) {

        switch(state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MAIN_MENU);
            case PLAYGAME -> game.getAudioPlayer().setSongForLevel(game.getPlayGame().getLevelManager().getLevelIndex());
        }

        GameState.state = state;

    }
}
