package hr.fer.zemris.vhdllab.service.ci;

import hr.fer.zemris.vhdllab.entity.File;

public interface CircuitInterfaceExtractor {

    CircuitInterface extract(File file)
            throws CircuitInterfaceExtractionException;

}
