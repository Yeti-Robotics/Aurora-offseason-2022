package frc.robot.di.devices;

import com.kauailabs.navx.frc.AHRS;
import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.SerialPort;

import javax.inject.Singleton;

@Module(includes = {MotorModule.class, SolenoidModule.class})
public class DeviceModule {
    @Provides
    @Singleton
    public AHRS providesGyro() {
        return new AHRS(SerialPort.Port.kUSB);
    }
}

