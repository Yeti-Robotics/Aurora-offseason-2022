package frc.robot.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

public class ButtonHelper {
    private GenericHID controller;

    HashMap<Integer, MultiButton> buttonMap = new HashMap<Integer, MultiButton>();
    private BooleanSupplier buttonLayerToggle;

    @Inject
    public ButtonHelper(GenericHID controller, BooleanSupplier isLayerOneActive) {
        this.controller = controller;
        this.buttonLayerToggle = isLayerOneActive;
    }

    public void createButton(
        int buttonNumber,
        Command[] commands,
        MultiButton.RunCondtion[] runCondtions) {
        MultiButton button = new MultiButton(
            new JoystickButton(controller, buttonNumber),
            commands,
            runCondtions);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                @Override
                public void run() {
                    button.updateButton();
                }
            }
        );
    }

    public void createAxisButton(
        int buttonNumber,
        Command[] commands,
        MultiButton.RunCondtion[] runCondtions,
        Double threshold) {
        AxisToButton axisButton;

        if (threshold != null && threshold <= 1.0) {
            axisButton = new AxisToButton(controller, buttonNumber, threshold);
        } else {
            axisButton = new AxisToButton(controller, buttonNumber);
        }
        MultiButton button = new MultiButton(
            axisButton,
            commands,
            runCondtions);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                @Override
                public void run() {
                    button.updateButton();
                }
            }
        );
    }

    public void createPOVButton(
        int buttonNumber,
        Command[] commands,
        MultiButton.RunCondtion[] runCondtions,
        POVToButton.Direction direction) {
        requireNonNullParam(direction, "direction", "createPOVButton");
        POVToButton povButton = new POVToButton(controller, direction);

        MultiButton button = new MultiButton(
            new JoystickButton(controller, buttonNumber),
            commands,
            runCondtions);

        CommandScheduler.getInstance().addButton(
            new Runnable() {
                @Override
                public void run() {
                    button.updateButton();
                }
            }
        );
    }
}
