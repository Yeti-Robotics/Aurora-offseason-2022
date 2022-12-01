package frc.robot.utils.controllerUtils;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import javax.inject.Inject;
import java.util.HashMap;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

public class ButtonHelper {
    private static final int maxAxis = 32;
    private static final int maxPOV = 16;
    private final Controller[] controllers;
    HashMap<Controller, HashMap<Byte, MultiButton>> buttonMap = new HashMap<>();
    private Controller controller;

    @Inject
    public ButtonHelper(Controller[] controllers) {
        this.controllers = controllers;
        for (Controller c : controllers) {
            buttonMap.put(c, new HashMap<>());
        }

        controller = controllers[0];
    }

    public static String buttonIDToString(byte buttonID) {
        String type;
        int port = (buttonID & 0xFF);
        byte binaryType = (byte) ((buttonID & 0xFF) >> 6);

        switch (binaryType) {
            case 0b00000000:
                type = "Button";
                port = buttonID;
                break;
            case 0b00000001:
                type = "Axis";
                port = port ^ 0b01000000;
                if (port >= maxAxis) {
                    port -= maxAxis;
                }
                break;
            case 0b00000010:
                type = "POV";
                port = port ^ 0b10000000;
                if (port >= maxPOV) {
                    port %= maxPOV;
                }
                break;
            default:
                type = "UNKNOWN";
                break;
        }

        return String.format("Button Type: %s || Port: %d\n", type, port);
    }

    private void createButton(
        Button button,
        byte buttonID,
        int layer,
        Command command,
        MultiButton.RunCondition runCondition) {

        if (buttonID == (byte) 0b11111111) {
            return;
        }

        if (buttonMap.get(controller).containsKey(buttonID)) {
            buttonMap.get(controller).get(buttonID).addLayer(layer, command, runCondition);
            return;
        }

        MultiButton multiButton = new MultiButton(
            button,
            buttonID,
            layer,
            command,
            runCondition);

        buttonMap.get(controller).put(buttonID, multiButton);

        CommandScheduler.getInstance().addButton(
            multiButton::updateButton
        );
    }

    public void createButton(
        int buttonNumber,
        int layer,
        Command command,
        MultiButton.RunCondition runCondition) {

        createButton(
            new JoystickButton(controller, buttonNumber),
            getButtonID(ButtonType.BUTTON, buttonNumber),
            layer,
            command,
            runCondition);
    }

    public void createAxisButton(
        int axisPort,
        int layer,
        Command command,
        MultiButton.RunCondition runCondition,
        double threshold,
        boolean isNegative) {

        AxisToButton axisButton = new AxisToButton(controller, axisPort, threshold, isNegative);

        createButton(
            axisButton,
            getButtonID(ButtonType.AXIS, isNegative ? maxAxis + axisPort : axisPort),
            layer,
            command,
            runCondition);
    }

    public void createAxisButton(
        int axisPort,
        int layer,
        Command command,
        MultiButton.RunCondition runCondition) {

        AxisToButton axisButton = new AxisToButton(controller, axisPort);

        createButton(
            axisButton,
            getButtonID(ButtonType.AXIS, axisPort),
            layer,
            command,
            runCondition);
    }

    public void createPOVButton(
        int povPort,
        POVToButton.Direction direction,
        int layer,
        Command command,
        MultiButton.RunCondition runCondition) {
        requireNonNullParam(direction, "direction", "createPOVButton");

        POVToButton povButton = new POVToButton(controller, povPort, direction);

        povPort = (Math.max((direction.value / 90 * maxPOV - 1), 0)) + povPort;

        createButton(
            povButton,
            getButtonID(ButtonType.POV, povPort),
            layer,
            command,
            runCondition);
    }

    public void setController(int controllerNumber) {
        controller = controllers[controllerNumber];
    }

    public void setAllLayers(int layer) {
        MultiButton.syncLayers(layer);
    }

    public void setButtonLayer(ButtonType type, int port, int layer) {
        buttonMap.get(controller).get(getButtonID(type, port)).setButtonLayer(layer);
    }

    public int getButtonLayer(ButtonType type, int port) {
        return buttonMap.get(controller).get(getButtonID(type, port)).getButtonLayer();
    }

    public HashMap<Byte, MultiButton> getButtonMap(int controllerNumber) {
        return buttonMap.get(controllers[controllerNumber]);
    }

    public byte getButtonID(ButtonType type, Integer port) {
        if (port > 63) {
            return (byte) 0b11111111;
        }
        switch (type) {
            case BUTTON:
                return port.byteValue();
            case AXIS:
                return (byte) (0b01000000 | port.byteValue());
            case POV:
                return (byte) (0b10000000 | port.byteValue());
        }

        return (byte) 0b11111111;
    }

    public enum ButtonType {
        BUTTON,
        AXIS,
        POV
    }
}
