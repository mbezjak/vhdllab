package hr.fer.zemris.vhdllab.simulators;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import java.util.List;

public interface ISimulator {
	SimulationResult simulate(List<File> dbFiles, List<File> otherFiles, File simFile, VHDLLabManager vhdlman);
}
