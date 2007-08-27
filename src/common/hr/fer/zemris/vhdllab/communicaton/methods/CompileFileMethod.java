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
public class CompileFileMethod extends AbstractIdParameterMethod<CompilationResult> {

	public CompileFileMethod(Long id) {
		super("compile.file", id);
	}

}
