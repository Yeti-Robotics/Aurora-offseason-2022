package frc.robot.commands.rests;

import frc.robot.commands.rests.restAnnotations.*;
import frc.robot.commands.rests.restUtils.REST;
import frc.robot.commands.rests.restUtils.RESTHandler;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Inject;

import static frc.robot.commands.rests.restUtils.RESTAssertions.assertTrue;

@RobotEnabledSelfTest
public class DrivetrainTwoREST {
    @Requirement
    private final DrivetrainSubsystem drivetrainSubsystem;

    @Inject
    public DrivetrainTwoREST(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    @Before
    public void setup() {
    }

    @After
    public void shutdown() {
    }

    @Test
    public void driveForwardTwo() {
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
