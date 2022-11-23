package frc.robot.commands.tests.testAnnotations;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface RobotEnabledSelfTest {
}