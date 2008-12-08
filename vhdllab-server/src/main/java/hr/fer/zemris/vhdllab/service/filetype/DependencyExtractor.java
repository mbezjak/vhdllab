package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;

import java.util.Set;

public interface DependencyExtractor {

    Set<Caseless> extract(FileInfo file) throws DependencyExtractionException;

}
