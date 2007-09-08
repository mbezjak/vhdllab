/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class GenerateVHDLMethod extends AbstractIdParameterMethod<String> {

	private static final long serialVersionUID = 1L;

	public GenerateVHDLMethod(Long id) {
		super("generate.vhdl", id);
	}

}
