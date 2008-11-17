package hr.fer.zemris.vhdllab.service.filetype.testbench;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor;

import java.util.Collections;
import java.util.List;

public class TestbenchCircuitInterfaceExtractor implements
        CircuitInterfaceExtractor {

    @Override
    public CircuitInterface extract(FileInfo file)
            throws CircuitInterfaceExtractionException {
        List<Port> emptyPortList = Collections.emptyList();
        return new CircuitInterface(file.getName().toString(), emptyPortList);
    }

}
