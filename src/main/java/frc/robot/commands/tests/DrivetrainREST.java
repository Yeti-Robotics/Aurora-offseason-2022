package frc.robot.commands.tests;

import static frc.robot.commands.tests.RESTassertions.*;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.tests.testAnnotations.*;
import frc.robot.subsystems.DrivetrainSubsystem;

@RobotEnabledSelfTest
public class DrivetrainREST {
    @Requirement
    private DrivetrainSubsystem drivetrainSubsystem;
    private DrivetrainSubsystem.DriveMode beforeDriveMode;

    public DrivetrainREST(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    @Before
    public void setup() {
        beforeDriveMode = drivetrainSubsystem.getDriveMode();
        drivetrainSubsystem.setDriveMode(DrivetrainSubsystem.DriveMode.TEST);
    }

    @After
    public void shutdown() {
//        drivetrainSubsystem.setRobotContainer(robotContainer);
        drivetrainSubsystem.setDriveMode(beforeDriveMode);
    }

    @Test
    public void driveForward() {
        RESTHandler.setInit(() -> {
            drivetrainSubsystem.resetEncoders();
            System.out.println("INIT TEST!!!");
        });

        RESTHandler.setExecute(() -> {
            drivetrainSubsystem.tankDrive(0.5, 0.5);
            RESTHandler.setFinished(RESTHandler.hasElapsed(5.0));
            System.out.println("WAITING!!");
        });

        RESTHandler.setResults(() -> {
            drivetrainSubsystem.stopDrive();
            assertEquals(drivetrainSubsystem.getLeftEncoderDistance() > 0);
            System.out.println("GETTING RESULTS!!!");
        });
    }
}
