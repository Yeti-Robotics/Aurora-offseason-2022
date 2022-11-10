package frc.robot.di.devices;

import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.PneumaticConstants;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class SolenoidModule {
    @Provides
    @Singleton
    @Named("shifter")
    public DoubleSolenoid providesShifter() {
        return new DoubleSolenoid(
            PneumaticConstants.deviceType,
            DrivetrainConstants.SOLENOID_SHIFTER_PORTS[0],
            DrivetrainConstants.SOLENOID_SHIFTER_PORTS[1]);
    }
}
