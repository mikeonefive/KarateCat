package inputs;

import com.studiohartman.jamepad.ControllerManager;
import main.GamePanel;


public class GamepadInput {
    private ControllerManager controllers;
    private GamePanel gamePanel;


    public GamepadInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
    }

    public ControllerManager getControllers() {
        return controllers;
    }


}