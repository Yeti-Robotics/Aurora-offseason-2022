package frc.robot.di;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import frc.robot.commands.rests.DrivetrainREST;
import frc.robot.commands.rests.DrivetrainTwoREST;
import frc.robot.commands.rests.restAnnotations.RobotEnabledSelfTest;
import frc.robot.commands.rests.restUtils.REST;
import frc.robot.commands.rests.restUtils.RESTHandler;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Set;

@Module
public class RESTModule {
    @Provides
    @Singleton
    public RESTHandler providesRESTHandler(@RobotEnabledSelfTest Set<Object> rests) {
        return new RESTHandler(Arrays.asList(rests.toArray()));
    }
    @Provides @IntoSet
    @RobotEnabledSelfTest
    public Object provideDrivetrainREST(DrivetrainSubsystem drivetrainSubsystem) {
        return new DrivetrainREST(drivetrainSubsystem);
    }

    @Provides @IntoSet
    @RobotEnabledSelfTest
    public Object provideDrivetrainTwoREST(DrivetrainSubsystem drivetrainSubsystem) {
        return new DrivetrainTwoREST(drivetrainSubsystem);
    }
}
