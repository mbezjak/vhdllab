package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class SimulateLastCommand extends ActionCommand {

    public static final String ID = "simulateLastCommand";

    @Autowired
    protected SimulationManager simulationManager;

    public SimulateLastCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        simulationManager.simulateLast();
    }

}
