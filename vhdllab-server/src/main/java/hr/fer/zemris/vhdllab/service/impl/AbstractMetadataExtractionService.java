package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Set;

public abstract class AbstractMetadataExtractionService extends ServiceSupport
        implements MetadataExtractionService {

    @Override
    public CircuitInterface extractCircuitInterface(Integer fileId)
            throws CircuitInterfaceExtractionException {
        return extractCircuitInterface(loadFile(fileId));
    }

    public abstract CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException;

    @Override
    public Set<String> extractDependencies(Integer fileId)
            throws DependencyExtractionException {
        return extractDependencies(loadFile(fileId));
    }

    public abstract Set<String> extractDependencies(File file)
            throws DependencyExtractionException;

    @Override
    public Result generateVhdl(Integer fileId) throws VhdlGenerationException {
        return generateVhdl(loadFile(fileId));
    }

    public abstract Result generateVhdl(File file)
            throws VhdlGenerationException;

}
