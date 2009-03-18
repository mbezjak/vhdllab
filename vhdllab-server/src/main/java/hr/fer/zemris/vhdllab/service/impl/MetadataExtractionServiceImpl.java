package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MetadataExtractionServiceImpl implements MetadataExtractionService {

    @Autowired
    private ApplicationContext context;

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        Validate.notNull(file, "File can't be null");
        CircuitInterface ci = getBean(file).extractCircuitInterface(file);
        if (ci == null) {
            throw new IllegalStateException("Circuit interface is null");
        }
        return null;
    }

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        Validate.notNull(file, "File can't be null");
        Set<String> dependencies = getBean(file).extractDependencies(file);
        if (dependencies == null) {
            throw new IllegalStateException("Dependencies is null");
        }
        return dependencies;
    }

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        Validate.notNull(file, "File can't be null");
        Result result = getBean(file).generateVhdl(file);
        if (result == null) {
            throw new IllegalStateException("Result is null");
        }
        return result;
    }

    private MetadataExtractionService getBean(File file) {
        String type = file.getType().toString().toLowerCase(Locale.ENGLISH);
        String interfaceName = MetadataExtractionService.class.getSimpleName();
        String beanName = type + interfaceName;
        return (MetadataExtractionService) context.getBean(beanName,
                MetadataExtractionService.class);
    }

}
