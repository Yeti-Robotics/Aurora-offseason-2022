package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import javax.inject.Inject;
import javax.inject.Named;

public class DrivetrainSubsystem extends SubsystemBase {
    private final WPI_TalonFX leftMotor1,leftMotor2, rightMotor1, rightMotor2;
    private final MotorControllerGroup leftMotors, rightMotors;

    private final DifferentialDrive differentialDrive;
    private final DifferentialDriveWheelSpeeds wheelSpeeds;
    private final DifferentialDriveOdometry odometer;
    public enum DriveMode {
        TANK,
        CHEEZY,
        ARCADE
    }
    private DriveMode driveMode;

    private final AHRS gyro;

    @Inject
    public DrivetrainSubsystem(
        @Named("left drive motor 1") WPI_TalonFX leftMotor1,
        @Named("left drive motor 1") WPI_TalonFX leftMotor2,
        @Named("left drive motor 1") WPI_TalonFX rightMotor1,
        @Named("left drive motor 1") WPI_TalonFX rightMotor2,
        @Named("left drive motors") MotorControllerGroup leftMotors,
        @Named("right drive motors") MotorControllerGroup rightMotors,
        DifferentialDrive differentialDrive,
        DifferentialDriveWheelSpeeds wheelSpeeds,
        DifferentialDriveOdometry odometer,
        AHRS gyro
        ) {
        this.leftMotor1 = leftMotor1;
        this.leftMotor2 = leftMotor2;
        this.rightMotor1 = rightMotor1;
        this.rightMotor2 = rightMotor2;

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;
        this.differentialDrive = differentialDrive;
        this.wheelSpeeds = wheelSpeeds;
        this.odometer = odometer;
        this.driveMode = driveMode;
        this.gyro = gyro;
    }
}

