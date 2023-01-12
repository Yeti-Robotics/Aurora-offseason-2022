package frc.robot.commands.rests.restUtils;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.rests.restAnnotations.RobotEnabledSelfTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;

/**
 * Contains REST tests (methods annotated with @Test)
 */
@RobotEnabledSelfTest
public abstract class RESTContainer {
    private static class Test {
        private final Runnable initFn;
        private final Runnable executeFn;
        private final BooleanSupplier isFinishedFn;
        private final Runnable endFn;

        public Test(Runnable init, Runnable execute, BooleanSupplier isFinished, Runnable end) {
            this.initFn = init;
            this.executeFn = execute;
            this.isFinishedFn = isFinished;
            this.endFn = end;
        }

        private void init() {
            if(this.initFn != null) this.initFn.run();
        }

        private void execute() {
            if(this.executeFn != null) this.executeFn.run();
        }

        private boolean isFinished() {
            if (this.isFinishedFn != null) return this.isFinishedFn.getAsBoolean();
            return true;
        }

        private void end() {
            if(this.endFn != null) this.endFn.run();
        }

        public void run() {
            // this should be the only usage of this method anywhere!!!!
            RESTTimer.reset();
            init();

            do {
                execute();
            } while (!isFinished());

            end();
        }
    }
    private ArrayList<Test> tests;

    // Vars for currently being processed (in getTests function) test's resources
    private Runnable currentInit;
    private Runnable currentExecute;
    private BooleanSupplier currentIsFinished;
    private Runnable currentEnd;

    // made to be overridden
    protected void before() {}
    protected void after() {}

    /**
     * Set init function for this test
     * @param initFn init function
     */
    protected final void init(Runnable initFn) {
        this.currentInit = initFn;
    }

    /**
     * Set execute function for this test
     * @param executeFn execute function
     */
    protected final void execute(Runnable executeFn) {
        this.currentExecute = executeFn;
    }

    /**
     * Set isFinished function for this test
     * @param isFinishedFn isFinished function
     */
    protected final void isFinished(BooleanSupplier isFinishedFn) {
        this.currentIsFinished = isFinishedFn;
    }

    /**
     * Set end function for this test
     * @param endFn end function
     */
    protected final void end(Runnable endFn) {
        this.currentEnd = endFn;
    }

    /**
     * Timer is reset at the start of each test
     * Convenience wrapper around RESTTimer.hasElapsed
     * @param seconds Number of seconds to check since test started
     * @return if <code>seconds</code> has elapsed since the start of the test
     */
    protected final boolean hasElapsed(double seconds) {
        return RESTTimer.hasElapsed(seconds);
    }

    // final means cannot be overridden here
    public final ArrayList<Test> getTests() {
        if (this.tests != null) {
            return this.tests;
        }

        ArrayList<Test> tests = new ArrayList<>();

        // ref to the class extending RESTContainer
        Class<?> extendingClass = this.getClass();

        for (Method method : extendingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(frc.robot.commands.rests.restAnnotations.Test.class)) {
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
                tests.add(new Test(this.currentInit, this.currentExecute, this.currentIsFinished, this.currentEnd));
                this.currentInit = null;
                this.currentExecute = null;
                this.currentIsFinished = null;
                this.currentEnd = null;
            }
        }

        return tests;
    }
}
