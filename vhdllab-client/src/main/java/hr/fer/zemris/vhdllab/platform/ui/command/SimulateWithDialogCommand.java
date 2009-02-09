package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class SimulateWithDialogCommand extends ActionCommand {

    public static final String ID = "simulateWithDialogCommand";

    @Autowired
    protected SimulationManager simulationManager;

    public SimulateWithDialogCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        simulationManager.simulateWithDialog();
    }

}
