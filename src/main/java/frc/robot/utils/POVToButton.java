package frc.robot.utils;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.HashMap;
import java.util.Map;

public class POVToButton extends Button {
    private final GenericHID controller;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UP_LEFT,
        UP_RIGHT,
        DOWN_LEFT,
        DOWN_RIGHT
    }

    private final Map<Direction, Integer> POVmap = Map.of(
        Direction.UP, 0,
        Direction.UP_RIGHT, 45,
        Direction.RIGHT, 90,
        Direction.DOWN_RIGHT, 135,
        Direction.DOWN, 180,
        Direction.DOWN_LEFT, 225,
        Direction.LEFT, 270,
        Direction.UP_LEFT, 315
    );
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

    @Override
    public boolean get() {
        return controller.getPOV(port) == POVmap.get(direction);
    }
}
