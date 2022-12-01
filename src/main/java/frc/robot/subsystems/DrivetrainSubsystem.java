package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.utils.controllerUtils.Controller;
import frc.robot.utils.controllerUtils.ControllerContainer;

import javax.inject.Inject;
import javax.inject.Named;

public class DrivetrainSubsystem extends SubsystemBase implements AutoCloseable {
    private final WPI_TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private final MotorControllerGroup leftMotors, rightMotors;

    private final DifferentialDrive differentialDrive;
    private final DifferentialDriveWheelSpeeds wheelSpeeds;
    private final DifferentialDriveOdometry odometer;

    private final DoubleSolenoid shifter;

    private final ControllerContainer controllerContainer;
    private final Controller controller;
    private DriveMode driveMode;

    @Inject
    public DrivetrainSubsystem(
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
        AHRS gyro,
        ControllerContainer controllerContainer
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
        this.shifter = shifter;
        this.gyro = gyro;
        this.controllerContainer = controllerContainer;

        controller = controllerContainer.get(0);

        setMotorsBrake();

        resetEncoders();
        resetGyro();
        resetOdometry(new Pose2d());

        shiftUp();
    }

    private NeutralMode neutralMode;

    private final AHRS gyro;

    public void setDriveMode(DriveMode mode) {
        driveMode = mode;
        switch (driveMode) {
            case TANK:
                this.setDefaultCommand(
                    new RunCommand(() -> tankDrive(controller.getLeftY(), controller.getRightY()), this));
                break;
            case CHEEZY:
                this.setDefaultCommand(
                    new RunCommand(() -> cheezyDrive(controller.getLeftY(), controller.getRightX()), this));
                break;
            case ARCADE:
                this.setDefaultCommand(
                    new RunCommand(() -> arcadeDrive(controller.getLeftY(), controller.getRightX()), this));
                break;
        }
    }

    @Override
    public void periodic() {
        odometer.update(gyro.getRotation2d(), getLeftEncoderDistance(), getRightEncoderDistance());
    }

    public void setMaxOutput(double maxOutput) {
        differentialDrive.setMaxOutput(maxOutput);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMotors.setVoltage(leftVolts);
        rightMotors.setVoltage(rightVolts);
        differentialDrive.feed();
    }

    public void tankDrive(double leftpower, double rightpower) {
        differentialDrive.tankDrive(leftpower, rightpower);
    }

    public void cheezyDrive(double straight, double turn) {
        differentialDrive.curvatureDrive(straight, -turn, true);
    }

    public void arcadeDrive(double straight, double turn) {
        differentialDrive.arcadeDrive(straight, -turn);
    }

    public void stopDrive() {
        leftMotors.set(0.0);
        rightMotors.set(0.0);
    }

    public void setMotorsBrake() {
        leftMotor1.setNeutralMode(NeutralMode.Brake);
        leftMotor2.setNeutralMode(NeutralMode.Brake);
        rightMotor1.setNeutralMode(NeutralMode.Brake);
        rightMotor2.setNeutralMode(NeutralMode.Brake);
        neutralMode = NeutralMode.Brake;
    }

    public void setMotorsCoast() {
        leftMotor1.setNeutralMode(NeutralMode.Coast);
        leftMotor2.setNeutralMode(NeutralMode.Coast);
        rightMotor1.setNeutralMode(NeutralMode.Coast);
        rightMotor2.setNeutralMode(NeutralMode.Coast);
        neutralMode = NeutralMode.Coast;
    }

    // Distance in meters
    public double getLeftEncoderDistance() {
        return leftMotor1.getSelectedSensorPosition()
            * (getShifterPosition() == DoubleSolenoid.Value.kForward
            ? DrivetrainConstants.DISTANCE_PER_PULSE_HIGH_GEAR
            : DrivetrainConstants.DISTANCE_PER_PULSE_LOW_GEAR);
    }

    public double getRightEncoderDistance() {
        return -rightMotor1.getSelectedSensorPosition()
            * (getShifterPosition() == DoubleSolenoid.Value.kForward
            ? DrivetrainConstants.DISTANCE_PER_PULSE_HIGH_GEAR
            : DrivetrainConstants.DISTANCE_PER_PULSE_LOW_GEAR);
    }

    public double getAverageEncoder() {
        return ((getLeftEncoderDistance() + getRightEncoderDistance()) / 2.0);
    }

    public void resetEncoders() {
        leftMotor1.setSelectedSensorPosition(0.0);
        rightMotor1.setSelectedSensorPosition(0.0);
    }

    // Velocity in meters/second
    public double getLeftEncoderVelocity() {
        return (leftMotor1.getSelectedSensorVelocity() * 10)
            * (getShifterPosition() == DoubleSolenoid.Value.kForward
            ? DrivetrainConstants.DISTANCE_PER_PULSE_HIGH_GEAR
            : DrivetrainConstants.DISTANCE_PER_PULSE_LOW_GEAR);
    }

    public double getRightEncoderVelocity() {
        return (-rightMotor1.getSelectedSensorVelocity() * 10)
            * (getShifterPosition() == DoubleSolenoid.Value.kForward
            ? DrivetrainConstants.DISTANCE_PER_PULSE_HIGH_GEAR
            : DrivetrainConstants.DISTANCE_PER_PULSE_LOW_GEAR);
    }

    public double getAverageVelocity() {
        return (getLeftEncoderVelocity() + getRightEncoderVelocity()) / 2.0;
    }

    public double getHeading() {
        return gyro.getRotation2d().getDegrees();
    }

    public void resetGyro() {
        gyro.reset();
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        wheelSpeeds.leftMetersPerSecond = getLeftEncoderVelocity();
        wheelSpeeds.rightMetersPerSecond = getRightEncoderVelocity();
        return wheelSpeeds;
    }

    public Pose2d getPose() {
        return odometer.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        odometer.resetPosition(pose, gyro.getRotation2d());
    }

    public void toggleShifter() {
        shifter.toggle();
    }

    public void shiftUp() {
        shifter.set(DoubleSolenoid.Value.kForward);
    }

    public void shiftDown() {
        shifter.set(DoubleSolenoid.Value.kReverse);
    }

    public DoubleSolenoid.Value getShifterPosition() {
        return shifter.get();
    }

    public enum DriveMode {
        TANK,
        CHEEZY,
        ARCADE
    }

    public DriveMode getDriveMode() {
        return driveMode;
    }

    public NeutralMode getNeutralMode() {
        return neutralMode;
    }

    public void close() {
        shifter.close();
    }
}

