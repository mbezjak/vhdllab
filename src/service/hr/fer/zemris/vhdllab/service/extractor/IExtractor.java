package hr.fer.zemris.vhdllab.service.extractor;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

public interface IExtractor {

	CircuitInterface extractCircuitInterface(File f);
}
