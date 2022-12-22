package frc.robot.utils.controllerUtils;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.Arrays;
import java.util.function.BiConsumer;


public class MultiButton {
    private static boolean isLayersSynced;
    private static int syncLayer;
    private final Button button;
    private final byte buttonID;
    private BiConsumer<Boolean, Boolean>[] buttonActions;
    private boolean pressed;
    private boolean pressedLast;
    private int layerCount;
    private int buttonLayer = 0;

    @SuppressWarnings("unchecked")
    public MultiButton(Button button, byte buttonID, int layer, Command command, RunCondition runCondition) {
        this.button = button;
        this.buttonID = buttonID;
        this.layerCount = layer + 1;
        buttonActions = new BiConsumer[layerCount];

        addLayer(layer, command, runCondition);
    }

    public static void syncLayers(int layer) {
        isLayersSynced = true;
        syncLayer = layer;
    }


    public void addLayer(int layer, Command command, RunCondition runCondition) {
        if (layerCount <= layer) {
            layerCount = layer + 1;
            buttonActions = Arrays.copyOf(buttonActions, layerCount);
        }

        buttonActions[layer] = defineButton(command, runCondition);

        int i = 0;
        for (Object o : buttonActions) {
            if (o == null) {
                buttonActions[i] = (aBoolean, aBoolean2) -> {
                };
            }
            i++;
        }
    }

    public void updateButton() {
        if (isLayersSynced) {
            setAllLayers();
        }
        pressed = button.get();
        buttonActions[buttonLayer].accept(pressed, pressedLast);
        pressedLast = pressed;
    }

    public int getButtonLayer() {
        return buttonLayer;
    }

    public void setButtonLayer(int layer) {
        isLayersSynced = false;
        buttonLayer = layer % layerCount;
    }

    public void setAllLayers() {
        buttonLayer = syncLayer % layerCount;
    }

    public byte getButtonID() {
        return buttonID;
    }

    @Override
    public String toString() {
        return ButtonHelper.buttonIDToString(buttonID);
    }

    public enum RunCondition {
        WHEN_PRESSED,
        WHEN_RELEASED,
        WHILE_HELD,
        TOGGLE_WHEN_PRESSED,
        CANCEL_WHEN_PRESSED
    }

    private static BiConsumer<Boolean, Boolean> defineButton(Command command, RunCondition runCondition) {
        BiConsumer<Boolean, Boolean> biConsumer;

        if (command == null || runCondition == null) {
            biConsumer = (aBoolean, bBoolean) -> {
            };
            return biConsumer;
        }

        switch (runCondition) {
            case WHEN_PRESSED:
                biConsumer = (pressed, pressedLast) -> {
                    if (!pressedLast && pressed) {
                        command.schedule();
                    }
                };
                break;
            case WHEN_RELEASED:
                biConsumer = (pressed, pressedLast) -> {
                    if (pressedLast && !pressed) {
                        command.schedule();
                    }
                };
                break;
            case WHILE_HELD:
                biConsumer = (pressed, pressedLast) -> {
                    if (pressed) {
                        command.schedule();
                    } else if (!pressedLast) {
                        command.cancel();
                    }
                };
                break;
            case TOGGLE_WHEN_PRESSED:
                biConsumer = (pressed, pressedLast) -> {
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
                biConsumer = (pressed, pressedLast) -> {
                    if (!pressedLast && pressed) {
                        command.cancel();
                    }
                };
                break;
            default:
                biConsumer = null;
                break;
        }
        return biConsumer;
    }
}
