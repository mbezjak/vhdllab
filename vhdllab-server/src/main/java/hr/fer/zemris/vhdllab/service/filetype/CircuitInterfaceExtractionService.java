package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.FileInfo;

public class CircuitInterfaceExtractionService extends AbstractFileTypeFactory
        implements CircuitInterfaceExtractor {

    @Override
    public CircuitInterface extract(FileInfo file)
            throws CircuitInterfaceExtractionException {
        CircuitInterfaceExtractor extractor = getBean(file.getType(),
                CircuitInterfaceExtractor.class);
        return extractor.extract(file);
    }

}