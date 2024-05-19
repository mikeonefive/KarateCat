package gamestates;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// interface = collection of methods that we want each class to create (if they implement this interface)
// if they implement it, they must have all these methods
public interface StateMethods {

    public void update();

    public void draw(Graphics g);

    public void mouseClicked(MouseEvent e);

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);

    public void mouseMoved(MouseEvent e);

    public void keyPressed(KeyEvent e);

    public void keyReleased(KeyEvent e);

    public void handleGamepadInput();

}
