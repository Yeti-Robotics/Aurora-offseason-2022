package frc.robot.utils.controllerUtils;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

public class POVToButton extends Button {
    private final GenericHID controller;

    @Override
    public boolean get() {
        return controller.getPOV(port) == direction.value;
    }

    private final Direction direction;
    private final int port;

    public POVToButton(GenericHID controller, int port, Direction direction) {
        this.controller = controller;
        this.port = port;
        this.direction = direction;
    }

    public POVToButton(GenericHID controller, Direction direction) {
        this.controller = controller;
        this.port = 0;
        this.direction = direction;
    }

    public enum Direction {
        UP(0),
        DOWN(90),
        LEFT(180),
        RIGHT(270);

        public final int value;

        Direction(int value) {
            this.value = value;
        }
    }
}
