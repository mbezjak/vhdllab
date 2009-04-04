package hr.fer.zemris.vhdllab.platform.ui.wizard.simulator;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.List;

import org.springframework.richclient.core.Guarded;

public class SimulateLastCommandGuard extends CompileLastCommandGuard {

    public SimulateLastCommandGuard(SimulationManager manager, Guarded guarded) {
        super(manager, guarded);
    }

    @Override
    protected void updateEnabledState() {
        setEnabled(manager.getLastSimulatedFile() != null);
    }

    @Override
    public void compiled(List<CompilationMessage> messages) {
    }

    @Override
    public void simulated(Result result) {
        updateEnabledState();
    }

}
