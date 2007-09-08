/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsUserFileMethod extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsUserFileMethod(Long id) {
		super("exists.user.file", id);
	}

}
