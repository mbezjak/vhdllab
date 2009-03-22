package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CompileWithDialogCommand extends ActionCommand {

    public static final String ID = "compileWithDialogCommand";

    @Autowired
    private SimulationManager simulationManager;

    public CompileWithDialogCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        simulationManager.compileWithDialog();
    }

}
