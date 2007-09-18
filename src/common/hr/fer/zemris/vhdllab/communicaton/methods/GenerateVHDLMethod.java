/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;

/**
 * @author Miro Bezjak
 *
 */
public final class GenerateVHDLMethod extends AbstractIdParameterMethod<VHDLGenerationResult> {

	private static final long serialVersionUID = 1L;

	public GenerateVHDLMethod(Long id) {
		super("generate.vhdl", id);
	}

}
