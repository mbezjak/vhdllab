package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;

import java.util.Set;

public class DependencyExtractionService extends AbstractFileTypeFactory
        implements DependencyExtractor {

    @Override
    public Set<Caseless> extract(FileInfo file)
            throws DependencyExtractionException {
        DependencyExtractor extractor = getBean(file.getType(),
                DependencyExtractor.class);
        return extractor.extract(file);
    }

}
