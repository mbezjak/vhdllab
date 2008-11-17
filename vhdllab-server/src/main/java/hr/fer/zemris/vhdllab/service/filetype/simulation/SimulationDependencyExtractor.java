package hr.fer.zemris.vhdllab.service.filetype.simulation;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.util.Set;

public class SimulationDependencyExtractor implements DependencyExtractor {

    @Override
    public Set<Caseless> extract(FileInfo file)
            throws DependencyExtractionException {
        throw new DependencyExtractionException(
                "Simulation file type doesn't support dependency extraction");
    }

}
