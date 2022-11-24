package frc.robot.commands.rests.restUtils;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.commands.rests.restAnnotations.*;

import javax.inject.Inject;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RESTHandler implements Sendable {
    private final List<Object> rests;
    private final HashMap<Object, List<Method>> restTests;

    private Object currentREST;
    private List<Method> currentTestList;
    private Method currentTest;
    private List<Subsystem> currentRequirements;
    private static Runnable init = () -> {
    };
    private static Runnable execute = () -> {
    };
    private static Runnable end = () -> {
    };

    private final String[][] results;

    private DataLog log;
    private StringLogEntry resultLog;
    private static Timer timer;

    private boolean newREST = true;
    private static boolean testFinished = true;
    private boolean newTest = true;
    private int restIndex = 0;
    private int testIndex = 0;

    @Inject
    public RESTHandler(List<Object> rests) {
        this.rests = rests;
        restTests = new HashMap<>();
        results = new String[rests.size()][];

        int i = 0;
        for (Object rest : rests) {
            if (Objects.isNull(rest)) {
                throw new NullPointerException("SubsystemTest for SubsystemTestHandler is null");
            }

            Class<?> restClass = rest.getClass();
            if (!restClass.isAnnotationPresent(RobotEnabledSelfTest.class)) {
                throw new AnnotationTypeMismatchException(null, "This class is not annotated as a SubsystemTest");
            }

            restTests.put(rest, new ArrayList<>());

            for (Method method : restClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    method.setAccessible(true);
                    restTests.get(rest).add(method);
                }
            }
            results[i] = new String[restTests.get(rest).size()];
            i++;
        }

        currentREST = rests.get(restIndex);
        currentTestList = restTests.get(currentREST);
        currentTest = currentTestList.get(testIndex);
        currentRequirements = new ArrayList<>();
        timer = new Timer();
    }

    public void initialize() {
        DataLogManager.start();
        log = DataLogManager.getLog();

        setupREST();

        SendableRegistry.addLW(this, "RESTHandler");
        timer.start();
    }

    public void initTest() {
        init.run();
    }

    public void runTest() {
        execute.run();
    }

    public void handleResults() {
        String result;
        try {
            end.run();
            result = String.format("%s :: PASSED\n", currentTest.getName().toUpperCase());
            results[restIndex][testIndex] = result.stripTrailing();
            resultLog.append(result);
        } catch (Exception e) {
            result = String.format("%s :: FAILED\n", currentTest.getName().toUpperCase());
            results[restIndex][testIndex] = result.stripTrailing();
            resultLog.append(result + "\t\t" + e.getLocalizedMessage());
        }
    }

    public String[] getRESTResults(int restIndex) {
        return results[restIndex];
    }

    public void reset() {
        init = () -> {
        };
        execute = () -> {
        };
        end = () -> {
        };

        testFinished = true;
        newTest = true;
        timer.reset();
    }

    public void updateRESTProgress() {
        if (testIndex < currentTestList.size() - 1) {
            testIndex++;
            currentTest = currentTestList.get(testIndex);
            newREST = false;
            return;
        }

        if (restIndex < rests.size() - 1) {
            shutdownREST();
            restIndex++;
            testIndex = 0;
            currentREST = rests.get(restIndex);
            currentTestList = restTests.get(currentREST);
            currentTest = currentTestList.get(testIndex);
            setupREST();
            newREST = true;
        }
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

    public boolean isNewREST() {
        return newREST;
    }

    public void updateRequirements() {
        currentRequirements = new ArrayList<>();

        Subsystem subsystem;
        for (Field field : currentREST.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Requirement.class)) {
                field.setAccessible(true);
                try {
                    subsystem = (Subsystem) field.get(currentREST);
                    currentRequirements.add(subsystem);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        newREST = false;
    }

    public List<Subsystem> getCurrentRequirements() {
        return currentRequirements;
    }


    public void loadTest() {
        invokeMethod(currentTest, currentREST);
        newTest = false;
    }


    public boolean isNewTest() {
        return newTest;
    }

    public boolean isTestFinished() {
        return testFinished;
    }

    public boolean isRESTFinished() {
        if (restIndex == rests.size() - 1 && testFinished && testIndex == currentTestList.size() - 1) {
            shutdownREST();
            return true;
        }
        return false;
    }


    private void invokeMethod(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasElapsed(double seconds) {
        return timer.hasElapsed(seconds);
    }

    public static void setInit(Runnable runnable) {
        init = runnable;
    }

    public static void setExecute(Runnable runnable) {
        execute = runnable;
    }

    public static void setEnd(Runnable runnable) {
        end = runnable;
    }

    public static void setFinished(boolean finish) {
        testFinished = finish;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("RESTHandler");
        for (Object rest : rests) {
            builder.addStringArrayProperty(rest.getClass().getSimpleName(), () -> getRESTResults(restIndex), null);
        }
    }
}

