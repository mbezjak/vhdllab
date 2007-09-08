/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsFileMethod extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsFileMethod(Long id) {
		super("exists.file", id);
	}

}
