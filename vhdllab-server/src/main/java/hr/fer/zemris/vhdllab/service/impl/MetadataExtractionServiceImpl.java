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

import org.hibernate.validator.ClassValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MetadataExtractionServiceImpl extends
        AbstractMetadataExtractionService implements MetadataExtractionService {

    private static final ClassValidator<CircuitInterface> CIRCUIT_INTERFACE_VALIDATOR =
                            new ClassValidator<CircuitInterface>(CircuitInterface.class);

    @Autowired
    private ApplicationContext context;

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        CircuitInterface ci = getBean(file).extractCircuitInterface(
                file.getId());
        if (ci == null) {
            throw new IllegalStateException("Circuit interface is null");
        }
        CIRCUIT_INTERFACE_VALIDATOR.assertValid(ci);
        return null;
    }

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        Set<String> dependencies = getBean(file).extractDependencies(
                file.getId());
        if (dependencies == null) {
            throw new IllegalStateException("Dependencies is null");
        }
        return dependencies;
    }

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        Result result = getBean(file).generateVhdl(file.getId());
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
