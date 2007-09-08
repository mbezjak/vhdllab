/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsProjectMethod extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsProjectMethod(Long id) {
		super("exists.project", id);
	}

}
