package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LogoutMethod extends AbstractMethod<Integer> {

	private static final long serialVersionUID = 1L;

	public LogoutMethod(String userId) {
		super("logout", userId);
	}

}
