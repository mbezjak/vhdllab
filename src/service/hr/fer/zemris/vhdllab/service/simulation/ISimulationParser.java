package hr.fer.zemris.vhdllab.service.simulation;

import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

public interface ISimulationParser {

	SimulationResult parseResult(String result);
	
}
