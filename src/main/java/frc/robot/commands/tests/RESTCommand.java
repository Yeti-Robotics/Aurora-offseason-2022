package frc.robot.commands.tests;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

import javax.inject.Inject;


public class RESTCommand extends CommandBase {
    private RESTHandler handler;

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
            m_requirements.removeAll(handler.getCurrentRequirements());
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
