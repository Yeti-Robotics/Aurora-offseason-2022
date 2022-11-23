package frc.robot.di;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import frc.robot.commands.tests.RESTCommand;
import frc.robot.commands.tests.RESTHandler;
import frc.robot.commands.tests.DrivetrainREST;
import frc.robot.commands.tests.testAnnotations.RobotEnabledSelfTest;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Set;

@Module
public class RESTModule {
    @Provides
    @Singleton
    public RESTCommand providesRESTCommand(RESTHandler restHandler) {
        return new RESTCommand(restHandler);
    }

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
}
