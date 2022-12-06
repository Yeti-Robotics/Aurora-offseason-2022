package frc.robot.commands.rests.restAnnotations;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface RobotEnabledSelfTest {
}