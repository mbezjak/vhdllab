package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.FileInfo;

public interface CircuitInterfaceExtractor {

    CircuitInterface extract(FileInfo file)
            throws CircuitInterfaceExtractionException;

}
