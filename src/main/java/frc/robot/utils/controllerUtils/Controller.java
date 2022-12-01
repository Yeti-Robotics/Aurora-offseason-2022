package frc.robot.utils.controllerUtils;

import edu.wpi.first.wpilibj.GenericHID;

public class Controller extends GenericHID {
    public Controller(int port) {
        super(port);
    }

    public double getLeftY() {
        return super.getRawAxis(0);
    }

    public double getLeftX() {
        return super.getRawAxis(1);
    }

    public double getRightY() {
        return super.getRawAxis(2);
    }

    public double getRightX() {
        return super.getRawAxis(3);
    }
}
