package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.EventListener;
import java.util.List;

public interface SimulationListener extends EventListener {

    void compiled(File compiledFile, List<CompilationMessage> messages);

    void simulated(File simulatedfile, Result result);

}
