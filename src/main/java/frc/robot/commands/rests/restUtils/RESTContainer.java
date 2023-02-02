package frc.robot.commands.rests.restUtils;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.commands.rests.restAnnotations.REST;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;

/**
 * Contains REST tests (methods annotated with @REST)
 */
public abstract class RESTContainer {
    /**
     * Retrieves all subsystems in the RESTContainer
     * annotated with @Requirement.
     * If the subsystems have already been retrieved,
     * requirements will be returned without reflecting again.
     *
     * @return required subsystems
     */
    public final Subsystem[] getRequirements() {
        if (requirements != null) {
            return requirements;
        }

        ArrayList<Subsystem> tempRequirements = new ArrayList<>();

        Class<?> extendingClass = this.getClass();

        for (Field field : extendingClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(frc.robot.commands.rests.restAnnotations.Requirement.class)) {
                field.setAccessible(true);

                try {
                    tempRequirements.add((Subsystem) field.get(this));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        requirements = tempRequirements.toArray(new Subsystem[0]);
        return requirements;
    }

    private Subsystem[] requirements;
    private ArrayList<RobotEnableSelfTest> tests;
    // Vars for currently being processed (in getTests function) test's resources
    private Runnable currentInit;
    private Runnable currentExecute;
    private BooleanSupplier currentIsFinished;
    private Runnable currentEnd;

    // made to be overridden
    protected void before() {
    }

    protected void after() {
    }

    /**
     * Set init function for this test
     *
     * @param initFn init function
     */
    protected final void init(Runnable initFn) {
        this.currentInit = initFn;
    }

    /**
     * Set execute function for this test
     *
     * @param executeFn execute function
     */
    protected final void execute(Runnable executeFn) {
        this.currentExecute = executeFn;
    }

    /**
     * Set isFinished function for this test
     *
     * @param isFinishedFn isFinished function
     */
    protected final void isFinished(BooleanSupplier isFinishedFn) {
        this.currentIsFinished = isFinishedFn;
    }

    /**
     * Set end function for this test
     *
     * @param endFn end function
     */
    protected final void end(Runnable endFn) {
        this.currentEnd = endFn;
    }

    /**
     * Timer is reset at the start of each test
     * Convenience wrapper around RESTTimer.hasElapsed
     *
     * @param seconds Number of seconds to check since test started
     * @return if <code>seconds</code> has elapsed since the start of the test
     */
    protected final boolean hasElapsed(double seconds) {
        return RESTTimer.hasElapsed(seconds);
    }

    /**
     * Retrieves all RobotEnabledSelfTests in the RESTContainer
     * annotated with @REST.
     * If the RESTs have already been composed,
     * RESTs will be returned without reflecting again.
     *
     * @return all RESTs in the RESTContainer
     */
    public final ArrayList<RobotEnableSelfTest> getTests() {
        if (tests != null) {
            return tests;
        }

        tests = new ArrayList<>();

        // ref to the class extending RESTContainer
        Class<?> extendingClass = this.getClass();

        for (Method method : extendingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(REST.class)) {
                if (method.getParameterCount() > 0) {
                    throw new RuntimeException("Methods annotated with @Test must not have parameters.");
                }

                method.setAccessible(true);
                try {
                    // this should set the init, execute, isFinished, finish functions
                    method.invoke(this);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    // impossible exception
                    throw new RuntimeException(e);
                }

                // add test obj to array and reset the current... variables
                tests.add(new RobotEnableSelfTest(method.getName().toUpperCase(), this.currentInit, this.currentExecute, this.currentIsFinished, this.currentEnd));
                this.currentInit = null;
                this.currentExecute = null;
                this.currentIsFinished = null;
                this.currentEnd = null;
            }
        }

        return tests;
    }

    /**
     * Runs a set of actions on the robot, then retrieves
     * data from the robot to check for functionality.
     * <p>
     * DO NOT RUN THE TEST WITHOUT A CONTAINER.
     * Tests must be part of a RESTContainer that is scheduled
     * with the RESTHandler
     */
    public static class RobotEnableSelfTest {
        /**
         * name should be the name of the method annotated with @REST
         */
        private final String name;

        private final Runnable initFn;
        private final Runnable executeFn;
        private final BooleanSupplier isFinishedFn;
        private final Runnable endFn;

        /**
         * @param name       name of the REST
         * @param init       ran once at the beginning of the test
         * @param execute    runs every iteration of the test
         * @param isFinished runs every iteration to determine when to end the test
         * @param end        ran at the end of the test. checks if the test has passed.
         *                   throws a RESTAssertionException if failed.
         */
        public RobotEnableSelfTest(String name, Runnable init, Runnable execute, BooleanSupplier isFinished, Runnable end) {
            this.name = name;
            this.initFn = init;
            this.executeFn = execute;
            this.isFinishedFn = isFinished;
            this.endFn = end;
        }

        public String getName() {
            return name;
        }

        public void init() {
            if (initFn != null) initFn.run();
        }

        public void execute() {
            if (executeFn != null) executeFn.run();
        }

        public boolean isFinished() {
            if (isFinishedFn != null) return isFinishedFn.getAsBoolean();
            return true;
        }

        public void end() throws RESTAssertionException {
            if (endFn != null) endFn.run();
        }
    }

}
