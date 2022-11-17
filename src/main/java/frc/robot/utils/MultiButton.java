package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;


public class MultiButton {
    private final Button button;
    private final Command[] commands;

    public enum RunCondtion {
        WHEN_PRESSED,
        WHEN_RELEASED,
        WHILE_HELD,
        TOGGLE_WHEN_PRESSED,
        CANCEL_WHEN_PRESSED
    }

    private final RunCondtion[] runCondtions;

    private final Runnable[] buttonActions;

    private boolean pressed;
    private boolean pressedLast;

    private int buttonLayer = 0;

    public MultiButton(Button button, Command[] commands, RunCondtion[] runCondtions) {
        this.button = button;
        this.commands = commands;
        this.runCondtions = runCondtions;
        this.buttonActions = new Runnable[commands.length];

        for (int i = 0; i < commands.length; i++) {
            buttonActions[i] = defineButton(commands[i], runCondtions[i]);
        }
    }

    public void updateButton() {
        pressed = button.get();
        buttonActions[buttonLayer].run();
        pressedLast = pressed;
    }

    public void setButtonLayer(int layer) {
        buttonLayer = (layer - 1) % buttonActions.length;
    }

    public int getButtonLayer() {
        return buttonLayer;
    }

    private Runnable defineButton(Command command, RunCondtion runCondtion) {
        Runnable runnable;
        switch (runCondtion) {
            case WHEN_PRESSED:
                runnable = () -> {
                    if (!pressedLast && pressed) {
                        command.schedule();
                    }
                };
                break;
            case WHEN_RELEASED:
                runnable = () -> {
                    if (pressedLast && !pressed) {
                        command.schedule();
                    }
                };
                break;
            case WHILE_HELD:
                runnable = () -> {
                    if (pressed) {
                        command.schedule();
                    } else if (!pressedLast) {
                        command.cancel();
                    }
                };
                break;
            case TOGGLE_WHEN_PRESSED:
                runnable = () -> {
                    if (!pressedLast && pressed) {
                        if (command.isScheduled()) {
                            command.cancel();
                        } else {
                            command.schedule();
                        }
                    }
                };
                break;
            case CANCEL_WHEN_PRESSED:
                runnable = () -> {
                    if (!pressedLast && pressed) {
                        command.cancel();
                    }
                };
                break;
            default:
                runnable = null;
        }
        return runnable;
    }
}
