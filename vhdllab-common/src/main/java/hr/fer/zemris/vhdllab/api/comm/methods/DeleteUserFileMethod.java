package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.comm.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public final class DeleteUserFileMethod extends AbstractIdParameterMethod<Void> {

	private static final long serialVersionUID = 1L;

	public DeleteUserFileMethod(Long id, String userId) {
		super("delete.user.file", userId, id);
	}

}
