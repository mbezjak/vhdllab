package hr.fer.zemris.vhdllab.service.filetype.simulation;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractionException;

public class SimulationCircuitInterfaceExtractor implements
        CircuitInterfaceExtractor {

    @Override
    public CircuitInterface extract(FileInfo file)
            throws CircuitInterfaceExtractionException {
        throw new DependencyExtractionException(
                "Simulation file type doesn't support circuit interface extraction");
    }

}
