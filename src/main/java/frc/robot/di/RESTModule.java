package frc.robot.di;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import frc.robot.commands.rests.DrivetrainREST;
import frc.robot.commands.rests.restUtils.RESTContainer;
import frc.robot.commands.rests.restUtils.RESTHandler;
import frc.robot.subsystems.DrivetrainSubsystem;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Set;

@Module
public class RESTModule {
    @Provides
    @Singleton
    public RESTHandler providesRESTHandler(Set<RESTContainer> rests) {
        ArrayList<RESTContainer> restList = new ArrayList<>(rests);
        return new RESTHandler(restList);
    }

    @Provides
    @IntoSet
    public RESTContainer provideDrivetrainREST(DrivetrainSubsystem drivetrainSubsystem) {
        return new DrivetrainREST(drivetrainSubsystem);
    }

//    @Provides @IntoSet
//    public RESTContainer provideDrivetrainTwoREST(DrivetrainSubsystem drivetrainSubsystem) {
//        return new DrivetrainTwoREST(drivetrainSubsystem);
//    }
}
