package hr.fer.zemris.vhdllab.service.filetype.automaton;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.util.Collections;
import java.util.Set;

public class AutomatonDependencyExtractor implements DependencyExtractor {

    @Override
    public Set<Caseless> extract(FileInfo file)
            throws DependencyExtractionException {
        return Collections.emptySet();
    }

}
