package frc.robot.di.devices;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import dagger.Module;
import dagger.Provides;
import frc.robot.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class MotorModule {
    /****** Drivetrain motors ******/
    @Provides
    @Singleton
    @Named("left drive motor 1")
    public WPI_TalonFX provideLeftMotor1() {
        WPI_TalonFX motor = new WPI_TalonFX(Constants.DrivetrainConstants.LEFT_MOTOR_1);

        motor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configVoltageCompSaturation(Constants.DrivetrainConstants.VOLTAGE_COMP);
        motor.enableVoltageCompensation(true);
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        motor.setStatusFramePeriod(StatusFrame.Status_1_General, 250);
        motor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);

        return motor;
    }

    @Provides
    @Singleton
    @Named("left drive motor 2")
    public WPI_TalonFX provideLeftMotor2() {
        WPI_TalonFX motor = new WPI_TalonFX(Constants.DrivetrainConstants.LEFT_MOTOR_2);

        motor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configVoltageCompSaturation(Constants.DrivetrainConstants.VOLTAGE_COMP);
        motor.enableVoltageCompensation(true);
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        motor.setStatusFramePeriod(StatusFrame.Status_1_General, 250);
        motor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 250);

        return motor;
    }

    @Provides
    @Singleton
    @Named("right drive motor 1")
    public WPI_TalonFX provideRightMotor1() {
        WPI_TalonFX motor = new WPI_TalonFX(Constants.DrivetrainConstants.RIGHT_MOTOR_1);

        motor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configVoltageCompSaturation(Constants.DrivetrainConstants.VOLTAGE_COMP);
        motor.enableVoltageCompensation(true);
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        motor.setStatusFramePeriod(StatusFrame.Status_1_General, 250);
        motor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);

        return motor;
    }

    @Provides
    @Singleton
    @Named("right drive motor 2")
    public WPI_TalonFX provideRightMotor2() {
        WPI_TalonFX motor = new WPI_TalonFX(Constants.DrivetrainConstants.RIGHT_MOTOR_2);

        motor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 70, 80, 0.1));
        motor.configVoltageCompSaturation(Constants.DrivetrainConstants.VOLTAGE_COMP);
        motor.enableVoltageCompensation(true);
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        motor.setStatusFramePeriod(StatusFrame.Status_1_General, 250);
        motor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 250);

        return motor;
    }
}
