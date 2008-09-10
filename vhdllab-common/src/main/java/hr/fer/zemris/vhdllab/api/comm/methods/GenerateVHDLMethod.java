package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;

/**
 * @author Miro Bezjak
 *
 */
public final class GenerateVHDLMethod extends AbstractIdParameterMethod<VHDLGenerationResult> {

	private static final long serialVersionUID = 1L;

	public GenerateVHDLMethod(Long id, String userId) {
		super("generate.vhdl", userId, id);
	}

}
