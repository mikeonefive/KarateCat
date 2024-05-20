package inputs;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import main.GamePanel;


public class GamepadInput {
    private ControllerManager controllers;
    private GamePanel gamePanel;


    public GamepadInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
    }

    public ControllerState getButtonPressed() {
        ControllerState currState = this.getControllers().getState(0);
        return currState;
    }

    public ControllerManager getControllers() {
        return controllers;
    }


}