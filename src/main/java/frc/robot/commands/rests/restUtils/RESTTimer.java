package frc.robot.commands.rests.restUtils;

import edu.wpi.first.wpilibj.Timer;

public final class RESTTimer {
    private static final Timer timer = new Timer();

    static {
        timer.start();
    }

    public static boolean hasElapsed(double seconds) {
        return RESTTimer.timer.hasElapsed(seconds);
    }

    /**
     * Resets timer to 0, ONLY USE IN REST INTERNALS
     */
    public static void reset() {
        RESTTimer.timer.reset();
    }
}
