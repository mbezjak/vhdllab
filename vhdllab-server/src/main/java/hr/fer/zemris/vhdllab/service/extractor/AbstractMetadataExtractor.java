package hr.fer.zemris.vhdllab.service.extractor;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.impl.ServiceSupport;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Set;

public abstract class AbstractMetadataExtractor extends ServiceSupport
        implements MetadataExtractor {

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        return doExtractCircuitInterface(file.getData());
    }

    protected abstract CircuitInterface doExtractCircuitInterface(String data)
            throws CircuitInterfaceExtractionException;

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        return doExtractDependencies(file.getData());
    }

    protected abstract Set<String> doExtractDependencies(String data)
            throws DependencyExtractionException;

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        return doGenerateVhdl(file.getData());
    }

    protected abstract Result doGenerateVhdl(String data)
            throws VhdlGenerationException;

}
