package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsUserFileMethod extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsUserFileMethod(Long id, String userId) {
		super("exists.user.file", userId, id);
	}

}
