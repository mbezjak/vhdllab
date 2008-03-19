package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * VHDL generators generate VHDL code out of specified file. All VHDL generators
 * must have an empty public default constructor for proper initialization.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface VHDLGenerator {

	/**
	 * Returns a VHDL generation result for specified file. Return value can
	 * never be <code>null</code>.
	 * <p>
	 * Implementor can expect <code>file</code> parameter to always be not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param file
	 *            a file for which VHDL must be generated
	 * @return a VHDL generation result
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	VHDLGenerationResult generateVHDL(File file) throws ServiceException;
}
