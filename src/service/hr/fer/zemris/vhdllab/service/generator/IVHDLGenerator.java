package hr.fer.zemris.vhdllab.service.generator;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;

public interface IVHDLGenerator {

	VHDLGenerationResult generateVHDL(File f, VHDLLabManager labman) throws ServiceException;
}
