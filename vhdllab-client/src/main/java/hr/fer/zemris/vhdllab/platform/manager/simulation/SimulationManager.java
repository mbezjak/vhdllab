package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface SimulationManager extends EventPublisher<SimulationListener> {

    void simulateWithDialog();

    void simulateLast();

    void simulate(FileInfo file);

}
