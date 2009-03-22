package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.result.Result;

public interface MetadataExtractionService {

    CircuitInterface extractCircuitInterface(Integer fileId)
            throws CircuitInterfaceExtractionException;

    Result generateVhdl(Integer fileId) throws VhdlGenerationException;

}
