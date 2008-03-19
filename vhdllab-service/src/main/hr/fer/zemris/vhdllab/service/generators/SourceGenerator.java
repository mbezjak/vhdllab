package hr.fer.zemris.vhdllab.service.generators;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import java.util.Collections;
import java.util.List;

/**
 * A generator for a {@link FileTypes#VHDL_SOURCE} file type.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class SourceGenerator implements VHDLGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.VHDLGenerator#generateVHDL(hr.fer.zemris.vhdllab.entities.File)
	 */
	@Override
	public VHDLGenerationResult generateVHDL(File file) throws ServiceException {
		List<VHDLGenerationMessage> messages = Collections.emptyList();
		String vhdl = file.getContent();
		VHDLGenerationResult result = new VHDLGenerationResult(true, messages,
				vhdl);
		return result;
	}

}
