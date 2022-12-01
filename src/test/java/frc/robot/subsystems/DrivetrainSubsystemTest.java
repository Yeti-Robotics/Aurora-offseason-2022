package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants;
import frc.robot.utils.controllerUtils.ControllerContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DrivetrainSubsystemTest {
    DrivetrainSubsystem drivetrainSubsystem;

    WPI_TalonFX leftMotor1 = mock(WPI_TalonFX.class);
    WPI_TalonFX leftMotor2 = mock(WPI_TalonFX.class);
    WPI_TalonFX rightMotor1 = mock(WPI_TalonFX.class);
    WPI_TalonFX rightMotor2 = mock(WPI_TalonFX.class);

    MotorControllerGroup leftMotors;
    MotorControllerGroup rightMotors;
    DifferentialDrive differentialDrive;
    DifferentialDriveWheelSpeeds wheelSpeeds;
    DifferentialDriveOdometry odometer;

    AHRS gyro = mock(AHRS.class);
    ControllerContainer controller = mock(ControllerContainer.class);

    DoubleSolenoid shifter;

    @BeforeEach
    public void setup() {
        assert HAL.initialize(500, 0);
        leftMotors = new MotorControllerGroup(leftMotor1, leftMotor2);
        rightMotors = new MotorControllerGroup(rightMotor1, rightMotor2);

        differentialDrive = new DifferentialDrive(leftMotors, rightMotors);
        wheelSpeeds = new DifferentialDriveWheelSpeeds();
        when(gyro.getRotation2d()).thenReturn(new Rotation2d(0.0));
        odometer = new DifferentialDriveOdometry(gyro.getRotation2d());

        shifter =
            new DoubleSolenoid(
                Constants.PneumaticConstants.deviceType,
                Constants.DrivetrainConstants.SOLENOID_SHIFTER_PORTS[0],
                Constants.DrivetrainConstants.SOLENOID_SHIFTER_PORTS[1]
            );

        drivetrainSubsystem = new DrivetrainSubsystem(
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
            gyro,
            controller);
    }

    @AfterEach
    public void shutdown() {
        drivetrainSubsystem.close();
    }

    @Test
    public void testEncoderDistanceConversion() {
        drivetrainSubsystem.shiftUp();
        when(leftMotor1.getSelectedSensorPosition()).thenReturn(40550.0);
        when(rightMotor1.getSelectedSensorPosition()).thenReturn(-40550.0);
        assertEquals( 1, drivetrainSubsystem.getLeftEncoderDistance(), 0.01);
        assertEquals( 1, drivetrainSubsystem.getRightEncoderDistance(), 0.01);

        drivetrainSubsystem.shiftDown();
        assertNotEquals(1, drivetrainSubsystem.getLeftEncoderDistance());
        assertNotEquals(1, drivetrainSubsystem.getLeftEncoderDistance());
    }
}
