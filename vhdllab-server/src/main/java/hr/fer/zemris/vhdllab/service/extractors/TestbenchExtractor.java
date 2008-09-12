package hr.fer.zemris.vhdllab.service.extractors;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.ArrayList;
import java.util.Properties;

public class TestbenchExtractor implements CircuitInterfaceExtractor {

    @Override
    public CircuitInterface execute(File file) throws ServiceException {
        return new CircuitInterface(file.getName(), new ArrayList<Port>(0));
    }

    @Override
    public void configure(Properties properties) {
    }

}
