package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Set;

public interface MetadataExtractionService {

    CircuitInterface extractCircuitInterface(Integer fileId)
            throws CircuitInterfaceExtractionException;

    Set<String> extractDependencies(Integer fileId)
            throws DependencyExtractionException;

    Result generateVhdl(Integer fileId) throws VhdlGenerationException;

}
