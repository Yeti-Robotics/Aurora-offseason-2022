package frc.robot.di;

import dagger.Component;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.di.devices.DeviceModule;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
@Component(modules = {RobotModule.class, SubsystemsModule.class, CommandsModule.class, DeviceModule.class, RESTModule.class})
public interface RobotComponent {
    Map<Class<?>, CommandBase> commands();
    @Component.Builder
    interface Builder {
        RobotComponent build();
    }

    void inject(Robot robot);

    RobotContainer container();
}
