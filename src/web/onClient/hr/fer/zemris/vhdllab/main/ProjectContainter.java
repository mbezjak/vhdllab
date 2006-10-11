package hr.fer.zemris.vhdllab.main;

import java.util.List;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

public interface ProjectContainter {
	List<String> getAllCircuits();
	CircuitInterface getCircuitInterfaceFor(String name);
}
