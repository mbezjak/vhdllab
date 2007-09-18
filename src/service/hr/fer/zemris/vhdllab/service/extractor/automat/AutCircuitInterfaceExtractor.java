package hr.fer.zemris.vhdllab.service.extractor.automat;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.ICircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.generator.automat.VHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;

public class AutCircuitInterfaceExtractor implements ICircuitInterfaceExtractor {

	public CircuitInterface extractCircuitInterface(File f) throws ServiceException {
		String VHDL=new VHDLGenerator().generateVHDL(f,null).getVhdl();
		CircuitInterface circuit=Extractor.extractCircuitInterface(VHDL);
		return circuit;
	}

}
