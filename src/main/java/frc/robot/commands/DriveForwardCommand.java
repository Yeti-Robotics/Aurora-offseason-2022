package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Inject;


public class DriveForwardCommand extends CommandBase {
    private final DrivetrainSubsystem drivetrainSubsystem;
    private final Timer timer;

    @Inject
    public DriveForwardCommand(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        timer = new Timer();

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        drivetrainSubsystem.cheezyDrive(0.5, 0.0);
    }

    @Override
    public void execute() {
        drivetrainSubsystem.cheezyDrive(0.5, 0.0);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3.0);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.stopDrive();
    }
}
