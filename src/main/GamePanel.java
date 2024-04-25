// this class is for drawing all the objects

package main;

import inputs.KeyboardInput;
import inputs.MouseInput;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;


public class GamePanel extends JPanel
{
    private Game game;

    private MouseInput mouseInput;

    public GamePanel(Game game)
    {
        mouseInput = new MouseInput(this);
        this.game = game;

        setPanelSize();
        
        addKeyListener(new KeyboardInput(this));
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
    }



    private void setPanelSize() {

        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        // System.out.printf("%s x %s\n", GAME_WIDTH, GAME_HEIGHT);
    }


    public void updateGame() {

    }

    // we can only draw sth if we create a method called paintComponent, this is like the brush to draw sth on the panel
    public void paintComponent(Graphics graphics)
    {
        // calls the super class (parent class), it's like cleaning the surface and then we can paint sth
        // without residue from previous frame
        super.paintComponent(graphics);

        game.render(graphics);

    }

    public Game getGame() {
        return game;
    }
}
