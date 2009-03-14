package hr.fer.zemris.vhdllab.service.filetype.automaton;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.util.Collections;
import java.util.Set;

public class AutomatonDependencyExtractor implements DependencyExtractor {

    @Override
    public Set<Caseless> extract(File file)
            throws DependencyExtractionException {
        return Collections.emptySet();
    }

}
