// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.DriveForwardCommand;
import frc.robot.di.RobotComponent;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.utils.controllerUtils.ButtonHelper;
import frc.robot.utils.controllerUtils.ControllerContainer;
import frc.robot.utils.controllerUtils.MultiButton;

import javax.inject.Inject;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private RobotComponent robotComponent;

    public final ControllerContainer controllerContainer;
    private final ButtonHelper buttonHelper;
    public final DrivetrainSubsystem drivetrainSubsystem;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    @Inject
    public RobotContainer(
        ControllerContainer controllerContainer,
        DrivetrainSubsystem drivetrainSubsystem) {
        this.controllerContainer = controllerContainer;
        this.drivetrainSubsystem = drivetrainSubsystem;

        drivetrainSubsystem.setDriveMode(DrivetrainSubsystem.DriveMode.CHEEZY);

        buttonHelper = new ButtonHelper(controllerContainer.getControllers());
    }


    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    public void configureButtonBindings() {
        buttonHelper.createButton(1, 0, robotComponent.commands().get(DriveForwardCommand.class), MultiButton.RunCondition.WHEN_PRESSED);
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new InstantCommand();
    }

    public void setRobotComponent(RobotComponent robotComponent) {
        this.robotComponent = robotComponent;
    }

    public RobotComponent getRobotComponent() {
        return robotComponent;
    }
}
