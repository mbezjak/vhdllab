package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Set;

public interface MetadataExtractor {

    CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException;

    Set<String> extractDependencies(File file)
            throws DependencyExtractionException;

    Result generateVhdl(File file) throws VhdlGenerationException;

}
