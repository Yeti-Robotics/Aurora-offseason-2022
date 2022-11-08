package frc.robot.di;

import dagger.Component;
import frc.robot.Robot;
import frc.robot.RobotContainer;

import javax.inject.Singleton;

@Singleton
@Component(modules = {RobotModule.class, SubsystemsModule.class, CommandsModule.class})
public interface RobotComponent {
    @Component.Builder
    interface Builder {
        public RobotComponent build();
    }

    void inject(Robot robot);

    RobotContainer container();
}
