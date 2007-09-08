/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

/**
 * @author Miro Bezjak
 *
 */
public final class CompileFileMethod extends AbstractIdParameterMethod<CompilationResult> {

	private static final long serialVersionUID = 1L;

	public CompileFileMethod(Long id) {
		super("compile.file", id);
	}

}
