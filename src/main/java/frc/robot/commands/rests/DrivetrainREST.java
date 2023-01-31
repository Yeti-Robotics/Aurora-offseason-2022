package frc.robot.commands.rests;

import frc.robot.commands.rests.restAnnotations.Requirement;
import frc.robot.commands.rests.restAnnotations.Test;
import frc.robot.commands.rests.restUtils.RESTContainer;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Inject;

import static frc.robot.commands.rests.restUtils.RESTAssertions.assertEquals;

public class DrivetrainREST extends RESTContainer {
    @Requirement
    private final DrivetrainSubsystem drivetrainSubsystem;

    @Inject
    public DrivetrainREST(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    public void before() {
    }

    @Override
    protected void after() {
    }

    @Test
    void driveForwards() {
        init(() -> {
            drivetrainSubsystem.resetEncoders();
        });

        execute(() -> {
            drivetrainSubsystem.tankDrive(0.5, 0.5);
        });

        isFinished(() -> hasElapsed(5.0));

        end(() -> {
            assertEquals(5000, drivetrainSubsystem.getLeftEncoderDistance(), 50);
        });
    }
}
