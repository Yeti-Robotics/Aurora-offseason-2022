package frc.robot.commands.rests.restUtils;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.commands.rests.restAnnotations.*;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BooleanSupplier;

public class RESTHandler implements Sendable, AutoCloseable {
    private RESTCommand restCommand;
    private static boolean testFinished = true;
    private final List<RESTContainer> rests;
    private final HashMap<Class<? extends RESTContainer>, ArrayList<String>> results;
    private Object currentREST;
    private List<Method> currentTestList;
    private Method currentTest;
    private List<Subsystem> currentRequirements;
    private DataLog log;
    private StringLogEntry resultLog;

    private RESTContainer scheduledREST;
    private RESTContainer.RobotEnableSelfTest scheduledTest;
    @Inject
    public RESTHandler(List<RESTContainer> rests) {
        this.rests = rests;
        results = new String[rests.size()][];

        currentRequirements = new ArrayList<>();
    }

    public void init() {
        DataLogManager.start();
        log = DataLogManager.getLog();

        for (RESTContainer c : rests) {
            c.getTests();
        }

        SendableRegistry.addLW(this, "RESTHandler");
    }

    public void fullTest() {
    }

    public void runTest(RESTContainer.RobotEnableSelfTest test) {
        new RESTCommand(test.initFn, test.executeFn, test.isFinishedFn, test.endFn).schedule();
    }

    public void results() {
        String result;
        try {
            result = String.format("%s :: PASSED\n", currentTest.getName().toUpperCase());
            results.get(scheduledREST.getClass()).add(result.stripTrailing());
            resultLog.append(result);
        } catch (Exception e) {
            result = String.format("%s :: FAILED\n", currentTest.getName().toUpperCase());
            results.get(scheduledREST.getClass()).add(result.stripTrailing());
            resultLog.append(result + "\t\t" + e.getLocalizedMessage());
        }
    }

    public ArrayList<String> getRESTResults(Class<? extends RESTContainer> containerClass) {
        return results.get(containerClass);
    }

    private void setupREST() {
        for (Method method : currentREST.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                method.setAccessible(true);
                invokeMethod(method, currentREST);
                break;
            }
        }
        resultLog = new StringLogEntry(log, "RESTResult/" + currentREST.getClass().getSimpleName());
    }

    private void shutdownREST() {
        for (Method method : currentREST.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(After.class)) {
                method.setAccessible(true);
                invokeMethod(method, currentREST);
                return;
            }
        }
    }

    public void updateRequirements() {
        currentRequirements = new ArrayList<>();

        Subsystem subsystem;
        for (Field field : currentREST.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Requirement.class)) {
                continue;
            }

            field.setAccessible(true);
            try {
                subsystem = (Subsystem) field.get(currentREST);
                currentRequirements.add(subsystem);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        newREST = false;
    }

    public List<Subsystem> getCurrentRequirements() {
        return currentRequirements;
    }

    private void invokeMethod(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("RESTHandler");
        for (RESTContainer rest : rests) {
            builder.addStringArrayProperty(rest.getClass().getSimpleName(), () -> getRESTResults(rest.getClass()).toArray(new String[5]), null);
        }
    }

    @Override
    public void close() {
        if (restCommand.isScheduled()) {
            restCommand.cancel();
        }

        restCommand = null;
        SendableRegistry.remove(this);
        log.close();
    }

    private static class RESTCommand extends CommandBase {
        private Runnable initFn;
        private Runnable executeFn;
        private BooleanSupplier isFinishedFn;
        private Runnable endFn;

        public RESTCommand(Runnable initFn, Runnable executeFn, BooleanSupplier isFinishedFn, Runnable endFn) {
            this.initFn = initFn;
            this.executeFn = executeFn;
            this.isFinishedFn = isFinishedFn;
            this.endFn = endFn;
        }

        @Override
        public void initialize() {
            if (initFn != null) initFn.run();
        }

        @Override
        public void execute() {
            if (executeFn != null) executeFn.run();
        }

        @Override
        public boolean isFinished() {
            if (isFinishedFn != null) return isFinishedFn.getAsBoolean();
            return true;
        }

        @Override
        public void end(boolean interrupted) {
        }
    }
}

