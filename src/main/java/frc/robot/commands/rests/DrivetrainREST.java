package frc.robot.commands.rests;

import static frc.robot.commands.rests.restUtils.RESTAssertions.*;

import frc.robot.commands.rests.restAnnotations.*;
import frc.robot.commands.rests.restUtils.RESTHandler;
import frc.robot.subsystems.DrivetrainSubsystem;

@RobotEnabledSelfTest
public class DrivetrainREST {
    @Requirement
    private final DrivetrainSubsystem drivetrainSubsystem;

    public DrivetrainREST(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    @Before
    public void setup() {
    }

    @After
    public void shutdown() {
    }

    @Test
    public void driveForward() {
        RESTHandler.setInit(() -> {
            drivetrainSubsystem.resetEncoders();
        });

        RESTHandler.setExecute(() -> {
            drivetrainSubsystem.tankDrive(0.5, 0.5);
            RESTHandler.setFinished(RESTHandler.hasElapsed(5.0));
        });

        RESTHandler.setEnd(() -> {
            drivetrainSubsystem.stopDrive();
            assertTrue(drivetrainSubsystem.getLeftEncoderDistance() > 0);
        });
    }
}
