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
    private final List<RESTContainer> rests;
    private final HashMap<Class<? extends RESTContainer>, ArrayList<String>> results;
    private DataLog log;
    private StringLogEntry resultLog;

    private List<RESTContainer> containerSchedule;
    private RESTContainer currentRESTContainer;
    private RESTContainer.RobotEnableSelfTest currentREST;
    @Inject
    public RESTHandler(List<RESTContainer> rests) {
        this.rests = rests;
        results = new HashMap<>(rests.size());
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
        scheduleRESTContainers(rests);
    }

    public void scheduleRESTContainers(List<RESTContainer> rests) {
        containerSchedule = rests;
    }

    public void runTest(RESTContainer.RobotEnableSelfTest test) {
        new RESTCommand(this, test).schedule();
    }

    private void results() {
        String result;
        try {
            currentREST.end();
            result = String.format("%s :: PASSED\n", currentREST.getName().toUpperCase());
            results.get(currentRESTContainer.getClass()).add(result.stripTrailing());
            resultLog.append(result);
        } catch (Exception e) {
            result = String.format("%s :: FAILED\n", currentREST.getName());
            results.get(currentRESTContainer.getClass()).add(result.stripTrailing());
            resultLog.append(result + "\t\t" + e.getLocalizedMessage());
        }
    }

    public ArrayList<String> getRESTResults(Class<? extends RESTContainer> containerClass) {
        return results.get(containerClass);
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
        SendableRegistry.remove(this);
        log.close();
    }

    private static class RESTCommand extends CommandBase {
        private final RESTHandler handler;
        private final RESTContainer.RobotEnableSelfTest test;
        public RESTCommand(RESTHandler handler, RESTContainer.RobotEnableSelfTest test, Subsystem... requirements) {
            this.handler = handler;
            this.test = test;

            addRequirements(requirements);
        }

        @Override
        public void initialize() {
            test.init();
        }

        @Override
        public void execute() {
            test.execute();
        }

        @Override
        public boolean isFinished() {
            return test.isFinished();
        }

        @Override
        public void end(boolean interrupted) {
            handler.results();
        }
    }
}

