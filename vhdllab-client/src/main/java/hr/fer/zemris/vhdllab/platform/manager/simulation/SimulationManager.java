package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface SimulationManager extends EventPublisher<SimulationListener> {

    void compile(File file);

    File getLastCompiledFile();

    void compileLast();

    void simulate(File file);

    File getLastSimulatedFile();

    void simulateLast();

    void compileWithDialog();

    void simulateWithDialog();
}
