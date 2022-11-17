package frc.robot.di;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import dagger.Module;
import dagger.Provides;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class SubsystemsModule {
    @Provides
    @Singleton
    @Named("drivetrain")
    public DrivetrainSubsystem provideDrivetrainSubsystem(
        @Named("left drive motor 1") WPI_TalonFX leftMotor1,
        @Named("left drive motor 2") WPI_TalonFX leftMotor2,
        @Named("right drive motor 1") WPI_TalonFX rightMotor1,
        @Named("right drive motor 2") WPI_TalonFX rightMotor2,
        @Named("left drive motors") MotorControllerGroup leftMotors,
        @Named("right drive motors") MotorControllerGroup rightMotors,
        DifferentialDrive differentialDrive,
        DifferentialDriveWheelSpeeds wheelSpeeds,
        DifferentialDriveOdometry odometer,
        @Named("shifter") DoubleSolenoid shifter,
        AHRS gyro) {
        return new DrivetrainSubsystem(
            leftMotor1,
            leftMotor2,
            rightMotor1,
            rightMotor2,
            leftMotors,
            rightMotors,
            differentialDrive,
            wheelSpeeds,
            odometer,
            shifter,
            gyro
        );
    }
}
