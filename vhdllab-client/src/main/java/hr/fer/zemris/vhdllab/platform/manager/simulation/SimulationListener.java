package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.EventListener;

public interface SimulationListener extends EventListener {

    void simulated(Result result);

}
