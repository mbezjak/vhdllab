package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;

/**
 * @author Miro Bezjak
 *
 */
public final class CompileFileMethod extends AbstractIdParameterMethod<CompilationResult> {

	private static final long serialVersionUID = 1L;

	public CompileFileMethod(Long id, String userId) {
		super("compile.file", userId, id);
	}

}
