package frc.robot.commands.rests;

import frc.robot.commands.rests.restAnnotations.REST;
import frc.robot.commands.rests.restAnnotations.Requirement;
import frc.robot.commands.rests.restUtils.RESTContainer;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Inject;

import static frc.robot.commands.rests.restUtils.RESTAssertions.assertEquals;

public class DrivetrainTwoREST extends RESTContainer {
    @Requirement
    private final DrivetrainSubsystem drivetrainSubsystem;

    @Inject
    public DrivetrainTwoREST(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    public void before() {
    }

    @Override
    protected void after() {
    }

    @REST
    void driveForwards() {
        init(() -> {
            drivetrainSubsystem.resetEncoders();
        });

        execute(() -> {
            drivetrainSubsystem.tankDrive(1.0, 1.0);
        });

        isFinished(() -> hasElapsed(5.0));

        end(() -> {
            assertEquals(5000, drivetrainSubsystem.getLeftEncoderDistance(), 50);
        });
    }
}
