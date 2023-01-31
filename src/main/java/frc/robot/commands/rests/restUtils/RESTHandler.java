package frc.robot.commands.rests.restUtils;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

public class RESTHandler implements Sendable, AutoCloseable {
    private final ArrayList<RESTContainer> restContainers;
    private final HashMap<Class<? extends RESTContainer>, ArrayList<String>> results;
    private DataLog log;
    private StringLogEntry resultLog;

    private ArrayList<RESTContainer> containerSchedule = new ArrayList<>();
    private ArrayList<RESTContainer.RobotEnableSelfTest> restSchedule = new ArrayList<>();
    private RESTContainer currentContainer;
    private RESTContainer.RobotEnableSelfTest currentREST;
    private Subsystem[] currentRequirements;
    private RESTCommand scheduledCommand;

    @Inject
    public RESTHandler(ArrayList<RESTContainer> rests) {
        this.restContainers = rests;
        results = new HashMap<>(rests.size());

        for (RESTContainer container : rests) {
            results.put(container.getClass(), new ArrayList<>());
        }
    }

    public void init() {
        DataLogManager.start();
        log = DataLogManager.getLog();

        for (RESTContainer c : restContainers) {
            c.getTests();
        }

        SendableRegistry.addLW(this, "RESTHandler");
    }

    public void fullTest() {
        scheduleRESTContainers(restContainers);
    }

    public void scheduleRESTContainers(ArrayList<RESTContainer> rests) {
        reset();
        containerSchedule = rests;
        restSchedule = new ArrayList<>();

        advanceSchedule();
    }

    private void runTest(RESTContainer.RobotEnableSelfTest test) {
        scheduledCommand = new RESTCommand(this, test, currentRequirements);
        scheduledCommand.schedule();
    }

    private void finishREST() {
        String result;
        try {
            currentREST.end();
            result = String.format("%s :: PASSED\n", currentREST.getName().toUpperCase());
            results.get(currentContainer.getClass()).add(result.stripTrailing());
            resultLog.append(result);
        } catch (Exception e) {
            result = String.format("%s :: FAILED\n", currentREST.getName());
            results.get(currentContainer.getClass()).add(result.stripTrailing());
            ;
            resultLog.append(result + "\t\t" + e.getLocalizedMessage());
        }

        advanceSchedule();
    }

    private void advanceSchedule() {
        if (!restSchedule.isEmpty()) {
            currentREST = restSchedule.remove(restSchedule.size() - 1);
            runTest(currentREST);
            return;
        }

        if (containerSchedule.isEmpty()) {
            return;
        }
        if (currentContainer != null) {
            currentContainer.after();
        }

        currentContainer = containerSchedule.remove(containerSchedule.size() - 1);
        currentRequirements = currentContainer.getRequirements();
        restSchedule = currentContainer.getTests();

        resultLog = new StringLogEntry(log, "RESTResult/" + currentContainer.getClass().getSimpleName());

        currentContainer.before();
        currentREST = restSchedule.remove(restSchedule.size() - 1);

        runTest(currentREST);
    }

    private void reset() {
        currentContainer = null;
        currentREST = null;
        if (scheduledCommand != null) {
            scheduledCommand.cancel();
            scheduledCommand = null;
        }
    }

    public ArrayList<String> getRESTResults(Class<? extends RESTContainer> containerClass) {
        return results.get(containerClass);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("RESTHandler");
        for (RESTContainer rest : restContainers) {
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
            handler.finishREST();
        }
    }
}

