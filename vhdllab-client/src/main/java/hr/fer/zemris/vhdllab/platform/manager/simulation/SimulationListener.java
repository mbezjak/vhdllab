package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.api.results.SimulationResult;

import java.util.EventListener;

public interface SimulationListener extends EventListener {

    void simulated(SimulationResult result);

}
