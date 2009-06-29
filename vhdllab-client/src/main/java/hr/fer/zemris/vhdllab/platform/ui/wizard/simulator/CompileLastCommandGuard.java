package hr.fer.zemris.vhdllab.platform.ui.wizard.simulator;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationListener;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.richclient.core.Guarded;

public class CompileLastCommandGuard implements SimulationListener {

    protected final SimulationManager manager;
    private final Guarded guarded;

    public CompileLastCommandGuard(SimulationManager manager, Guarded guarded) {
        Validate.notNull(manager);
        Validate.notNull(guarded);
        this.manager = manager;
        this.guarded = guarded;
        this.manager.addListener(this);
    }

    protected void updateEnabledState() {
        setEnabled(manager.getLastCompiledFile() != null);
    }

    protected void setEnabled(boolean enabled) {
        guarded.setEnabled(enabled);
    }

    @Override
    public void compiled(File compiledFile, List<CompilationMessage> messages) {
        updateEnabledState();
    }

    @Override
    public void simulated(File simulatedfile, Result result) {
    }

}
