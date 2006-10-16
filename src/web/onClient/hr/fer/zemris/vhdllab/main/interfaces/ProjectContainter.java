package hr.fer.zemris.vhdllab.main.interfaces;

import java.util.List;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

public interface ProjectContainter {
	List<String> getAllCircuits();
	CircuitInterface getCircuitInterfaceFor(String name);
}
