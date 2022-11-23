package frc.robot.commands.rests;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.rests.restUtils.RESTHandler;

import javax.inject.Inject;


public class RESTCommand extends CommandBase {
    private final RESTHandler handler;

    @Inject
    public RESTCommand(RESTHandler handler) {
        this.handler = handler;

        updateRequirements();
    }

    @Override
    public void initialize() {
        handler.initialize();
        handler.reset();
    }

    @Override
    public void execute() {
        if (handler.isNewTest()) {
            updateRequirements();
            handler.loadTest();
            handler.initTest();
        }

        handler.runTest();

        if (handler.isTestFinished()) {
            handler.handleResults();
            handler.updateRESTProgress();
            handler.reset();
        }
    }

    private void updateRequirements() {
        if (handler.isNewREST()) {
            handler.getCurrentRequirements().forEach(m_requirements::remove);
            handler.updateRequirements();
            m_requirements.addAll(handler.getCurrentRequirements());
        }
    }

    @Override
    public boolean isFinished() {
        return handler.isRESTFinished();
    }

    @Override
    public void end(boolean interrupted) {
        // handler.shutdownREST();
    }
}
