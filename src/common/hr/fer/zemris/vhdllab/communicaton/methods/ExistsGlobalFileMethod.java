/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsGlobalFileMethod extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsGlobalFileMethod(Long id) {
		super("exists.global.file", id);
	}

}
