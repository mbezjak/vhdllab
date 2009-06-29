package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.List;

public abstract class SimulationAdapter implements SimulationListener {

    @Override
    public void compiled(File compiledFile, List<CompilationMessage> messages) {
    }

    @Override
    public void simulated(File simulatedfile, Result result) {
    }

}
