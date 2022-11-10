// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DrivetrainConstants {
        public static final double DEADBAND = 0.0525;

        public static final int LEFT_MOTOR_1 = 1;
        public static final int LEFT_MOTOR_2 = 2;
        public static final int RIGHT_MOTOR_1 = 3;
        public static final int RIGHT_MOTOR_2 = 4;

        public static final double HIGH_GEAR_RATIO = 6.32; // jvn
        public static final double LOW_GEAR_RATIO = 11.90;

        public static final double WHEEL_DIAMETER_IN = 4.0;
        public static final double WHEEL_DIAMETER_M = Units.inchesToMeters(WHEEL_DIAMETER_IN);  // 0.1016
        public static final double WHEEL_CIRCUMFERENCE_M = WHEEL_DIAMETER_M * Math.PI; // 0.3192

        public static final double DISTANCE_PER_PULSE_HIGH_GEAR =
            WHEEL_CIRCUMFERENCE_M / (TalonFXConstants.ENCODER_RESOLUTION * HIGH_GEAR_RATIO);
        public static final double DISTANCE_PER_PULSE_LOW_GEAR =
            WHEEL_CIRCUMFERENCE_M / (TalonFXConstants.ENCODER_RESOLUTION * HIGH_GEAR_RATIO);

        public static final int[] SOLENOID_SHIFTER_PORTS = {0, 1};

        public static final double VOLTAGE_COMP = 12.0;
    }

    public static final class TalonFXConstants {
        public static final int ENCODER_RESOLUTION = 2048;

        public static final double VOLTAGE_COMP = 11.0;
    }

    public static final class PneumaticConstants {
        public static final PneumaticsModuleType deviceType = PneumaticsModuleType.CTREPCM;
    }
}
