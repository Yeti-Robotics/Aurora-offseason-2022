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

/**
 * Handles the scheduling of RESTContainers and results from RobotEnabledSelfTests
 * Creates a wpilog and puts the results on the NetworkTables
 */
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

    /**
     * Creates handler with a default list of RESTcontainers.
     * These will be containers used during a full test.
     *
     * @param restContainers default list of RESTContainers
     */
    @Inject
    public RESTHandler(ArrayList<RESTContainer> restContainers) {
        this.restContainers = restContainers;
        results = new HashMap<>(restContainers.size());

        for (RESTContainer container : restContainers) {
            results.put(container.getClass(), new ArrayList<>());
        }
    }

    /**
     * Initializes the DataLog and SendableRegistry.
     * Must be called once before running RESTs or after calling close().
     */
    public void init() {
        DataLogManager.start();
        log = DataLogManager.getLog();

        for (RESTContainer c : restContainers) {
            c.getTests();
        }

        SendableRegistry.add(this, "RESTHandler");
    }

    /**
     * Runs all RESTContainers given during the construction of the handler
     */
    public void fullTest() {
        scheduleRESTContainers(restContainers);
    }

    /**
     * Schedules RESTContainers to run.
     * Any REST currently running will be canceled.
     *
     * @param restContainers RESTContainers to schedule
     */
    public void scheduleRESTContainers(ArrayList<RESTContainer> restContainers) {
        reset();
        containerSchedule = new ArrayList<>(restContainers);
        restSchedule = new ArrayList<>();

        advanceSchedule();
    }

    /**
     * Creates a RESTCommand from the provided REST and schedules it.
     *
     * @param test the REST to run
     */
    private void runTest(RESTContainer.RobotEnableSelfTest test) {
        scheduledCommand = new RESTCommand(this, test, currentRequirements);
        scheduledCommand.schedule();
    }

    /**
     * Handles to results from the REST and puts the results in the DataLog and NetworkTables.
     * Advances the RESTHandler's schedule at the after the ending the REST.
     */
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
            resultLog.append(result + "\t\t" + e.getLocalizedMessage());
        }

        advanceSchedule();
    }

    /**
     * Advances the schedule. Should be called at the end of RESTs or when a new schedule is added.
     * If there are no more RESTs or RESTContainers, it will return.
     */
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
        restSchedule = new ArrayList<>(currentContainer.getTests());

        resultLog = new StringLogEntry(log, "RESTResult/" + currentContainer.getClass().getSimpleName());

        currentContainer.before();
        currentREST = restSchedule.remove(restSchedule.size() - 1);

        runTest(currentREST);
    }

    /**
     * Cancels any running RESTContainer or REST
     */
    private void reset() {
        currentContainer = null;
        currentREST = null;
        if (scheduledCommand != null) {
            scheduledCommand.cancel();
            scheduledCommand = null;
        }
    }

    /**
     * Gets the results from a RESTContainer. If the RESTContainer doesn't exist,
     * a ArrayList with a error string will be returned.
     *
     * @param containerClass class of the desired RESTContainer
     * @return all results from the container
     */
    public ArrayList<String> getRESTResults(Class<? extends RESTContainer> containerClass) {
        if (!results.containsKey(containerClass)) {
            ArrayList<String> noContainer = new ArrayList<>(1);
            noContainer.add("REST CONTAINER DOES NOT EXIST");
            return noContainer;
        }

        return results.get(containerClass);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("RESTHandler");
        for (RESTContainer container : restContainers) {
            builder.addStringArrayProperty(container.getClass().getSimpleName(), () -> getRESTResults(container.getClass()).toArray(new String[container.getTests().size()]), null);
        }
    }

    /**
     * Closes DataLogs and removes self from the SendableRegistry
     */
    @Override
    public void close() {
        SendableRegistry.remove(this);
        log.close();
    }

    /**
     * Command used to run RESTs
     */
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
            RESTTimer.reset();
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

