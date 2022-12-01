package frc.robot.utils.controllerUtils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

public class AxisToButton extends Button {
    private final GenericHID controller;

    private final int port;
    private double threshold = 0.25;
    private boolean isNegative = false;

    public AxisToButton(GenericHID controller, int port, double threshold, boolean isNegative) {
        this.controller = controller;
        this.port = port;
        this.threshold = Math.abs(threshold) > 1.0 ? this.threshold : Math.abs(threshold);
        this.isNegative = isNegative;
    }

    public AxisToButton(GenericHID controller, int port) {
        this.controller = controller;
        this.port = port;
    }

    @Override
    public boolean get() {
        return isNegative ? controller.getRawAxis(port) <= -threshold : controller.getRawAxis(port) >= threshold;
    }
}
