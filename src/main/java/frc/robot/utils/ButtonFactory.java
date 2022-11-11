package frc.robot.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

public class ButtonFactory {
    private final GenericHID controller;

    HashMap<Integer, Command[]> buttonMap = new HashMap<Integer, Command[]>();
    private BooleanSupplier buttonLayerToggle;

    public enum RunCondtion {
        WHEN_PRESSED,
        WHEN_RELEASED,
        WHILE_HELD,
        TOGGLE_WHEN_PRESSED,
        CANCEL_WHEN_PRESSED
    }

    @Inject
    public ButtonFactory(GenericHID controller, BooleanSupplier isLayerOneActive) {
        this.controller = controller;
        this.buttonLayerToggle = isLayerOneActive;
    }

    public void createButton(
        int buttonNumber,
        Command layerOneCommand,
        RunCondtion layerOneRunCondition,
        Command layTwoCommand,
        RunCondtion layerTwoRunCondition) {
        requireNonNullParam(layerOneCommand, "layerOneCommand", "createButton");
        requireNonNullParam(layerOneRunCondition, "layerOneRunCondition", "createButton");

        JoystickButton button = new JoystickButton(controller, buttonNumber);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                boolean pressed;
                boolean pressedLast;

                @Override
                public void run() {
                    pressed = button.get();

                    if (buttonLayerToggle.getAsBoolean()) {
                        buttonHandler(layerOneCommand, layerOneRunCondition, pressed, pressedLast);
                    } else {
                        buttonHandler(layTwoCommand, layerTwoRunCondition, pressed, pressedLast);
                    }

                    pressedLast = pressed;
                }
            }
        );
    }

    public void createButton(
        int buttonNumber,
        Command layerOneCommand,
        RunCondtion layerOneRunCondition) {
        requireNonNullParam(layerOneCommand, "layerOneCommand", "createTwoLayButton");
        requireNonNullParam(layerOneRunCondition, "layerOneRunCondition", "createButton");

        JoystickButton button = new JoystickButton(controller, buttonNumber);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                boolean pressed;
                boolean pressedLast;

                @Override
                public void run() {
                    pressed = button.get();
                    buttonHandler(layerOneCommand, layerOneRunCondition, pressed, pressedLast);
                    pressedLast = pressed;
                }
            }
        );
    }

    public void createButton(
        int buttonNumber,
        Command layerOneCommand,
        RunCondtion layerOneRunCondition,
        Command layTwoCommand,
        RunCondtion layerTwoRunCondition,
        Double threshold) {
        requireNonNullParam(layerOneCommand, "layerOneCommand", "createButton");
        requireNonNullParam(layerOneRunCondition, "layerOneRunCondition", "createButton");
        AxisToButton button;

        if (threshold != null && threshold <= 1.0) {
            button = new AxisToButton(controller, buttonNumber, threshold);
        } else {
            button = new AxisToButton(controller, buttonNumber);
        }

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                boolean pressed;
                boolean pressedLast;

                @Override
                public void run() {
                    pressed = button.get();

                    if (buttonLayerToggle.getAsBoolean()) {
                        buttonHandler(layerOneCommand, layerOneRunCondition, pressed, pressedLast);
                    } else {
                        buttonHandler(layTwoCommand, layerTwoRunCondition, pressed, pressedLast);
                    }

                    pressedLast = pressed;
                }
            }
        );
    }

    public void createAxisButton(
        int buttonNumber,
        Command layerOneCommand,
        RunCondtion layerOneRunCondition,
        Double threshold) {
        requireNonNullParam(layerOneCommand, "layerOneCommand", "createTwoLayButton");
        requireNonNullParam(layerOneRunCondition, "layerOneRunCondition", "createButton");
        AxisToButton button;

        if (threshold != null && threshold <= 1.0) {
            button = new AxisToButton(controller, buttonNumber, threshold);
        } else {
            button = new AxisToButton(controller, buttonNumber);
        }

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                boolean pressed;
                boolean pressedLast;

                @Override
                public void run() {
                    pressed = button.get();
                    buttonHandler(layerOneCommand, layerOneRunCondition, pressed, pressedLast);
                    pressedLast = pressed;
                }
            }
        );
    }

    public void createButton(
        int buttonNumber,
        Command layerOneCommand,
        RunCondtion layerOneRunCondition,
        Command layTwoCommand,
        RunCondtion layerTwoRunCondition,
        POVToButton.Direction direction) {
        requireNonNullParam(layerOneCommand, "layerOneCommand", "createButton");
        requireNonNullParam(layerOneRunCondition, "layerOneRunCondition", "createButton");
        POVToButton button = new POVToButton(controller, direction);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                boolean pressed;
                boolean pressedLast;

                @Override
                public void run() {
                    pressed = button.get();

                    if (buttonLayerToggle.getAsBoolean()) {
                        buttonHandler(layerOneCommand, layerOneRunCondition, pressed, pressedLast);
                    } else {
                        buttonHandler(layTwoCommand, layerTwoRunCondition, pressed, pressedLast);
                    }

                    pressedLast = pressed;
                }
            }
        );
    }

    public void createPOVButton(
        int buttonNumber,
        Command layerOneCommand,
        RunCondtion layerOneRunCondition,
        POVToButton.Direction direction) {
        requireNonNullParam(layerOneCommand, "layerOneCommand", "createTwoLayButton");
        requireNonNullParam(layerOneRunCondition, "layerOneRunCondition", "createButton");
        POVToButton button = new POVToButton(controller, direction);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                boolean pressed;
                boolean pressedLast;

                @Override
                public void run() {
                    pressed = button.get();
                    buttonHandler(layerOneCommand, layerOneRunCondition, pressed, pressedLast);
                    pressedLast = pressed;
                }
            }
        );
    }

    public void buttonHandler(Command command, RunCondtion runCondtion, boolean pressed, boolean pressedLast) {
        switch (runCondtion) {
            case WHEN_PRESSED:
                if (!pressedLast && pressed) {
                    command.schedule();
                }
                break;
            case WHEN_RELEASED:
                if (pressedLast && !pressed) {
                    command.schedule();
                }
                break;
            case WHILE_HELD:
                if (pressed) {
                    command.schedule();
                } else if (!pressedLast) {
                    command.cancel();
                }
                break;
            case TOGGLE_WHEN_PRESSED:
                if (!pressedLast && pressed) {
                    if (command.isScheduled()) {
                        command.cancel();
                    } else {
                        command.schedule();
                    }
                }
                break;
            case CANCEL_WHEN_PRESSED:
                if (!pressedLast && pressed) {
                    command.cancel();
                }
                break;
        }
    }
}
