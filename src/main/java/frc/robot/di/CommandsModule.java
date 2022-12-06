package frc.robot.di;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.DriveBackwardCommand;
import frc.robot.commands.DriveForwardCommand;
import frc.robot.subsystems.DrivetrainSubsystem;

@Module
public class CommandsModule {

    @Provides
    @IntoMap
    @ClassKey(DriveForwardCommand.class)
    static CommandBase provideDriveForwardCommand(DrivetrainSubsystem drivetrainSubsystem) {
        return new DriveForwardCommand(drivetrainSubsystem);
    }

    @Provides
    @IntoMap
    @ClassKey(DriveBackwardCommand.class)
    static CommandBase provideDriveBackwardCommand(DrivetrainSubsystem drivetrainSubsystem) {
        return new DriveBackwardCommand(drivetrainSubsystem);
    }
}
