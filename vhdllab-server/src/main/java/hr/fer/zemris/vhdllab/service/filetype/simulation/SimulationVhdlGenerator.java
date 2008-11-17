package hr.fer.zemris.vhdllab.service.filetype.simulation;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

public class SimulationVhdlGenerator implements VhdlGenerator {

    @Override
    public VHDLGenerationResult generate(FileInfo file)
            throws VhdlGenerationException {
        throw new DependencyExtractionException(
                "Simulation file type doesn't support vhdl generation");
    }

}
