// this class is our game window

package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow
{
    private JFrame jframe; // jframe is used to get a window
    public GameWindow(GamePanel gamePanel) // constructor name and class name have to be the same
    {

        jframe = new JFrame(); // create new object of type JFrame

        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.add(gamePanel); // add gamePanel so we can draw sth and actually see it in the gameWindow
        // jframe.setLocationRelativeTo(null); // center the window
        jframe.setResizable(false);
        jframe.pack(); // fit size of window to preferred size of its components
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}
