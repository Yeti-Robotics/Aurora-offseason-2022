package frc.robot.commands.rests;

import frc.robot.commands.rests.restAnnotations.Test;
import frc.robot.commands.rests.restUtils.RESTContainer;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Inject;

public class NewDrivetrainREST extends RESTContainer {
    private final DrivetrainSubsystem drivetrainSubsystem;

    @Inject
    public NewDrivetrainREST(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    public void before() {}

    @Test
    void driveForwards() {
        init(() -> {});

        execute(() -> {});

        isFinished(() -> hasElapsed(5.0));

        end(() -> {});
    }
}
