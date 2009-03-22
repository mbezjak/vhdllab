package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CompileLastCommand extends ActionCommand {

    public static final String ID = "compileLastCommand";

    @Autowired
    private SimulationManager simulationManager;

    public CompileLastCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        simulationManager.compileLast();
    }

}
