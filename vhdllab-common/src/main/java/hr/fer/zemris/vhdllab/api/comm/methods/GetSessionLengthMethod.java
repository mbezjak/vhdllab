package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class GetSessionLengthMethod extends AbstractMethod<Integer> {

	private static final long serialVersionUID = 1L;

	public GetSessionLengthMethod(String userId) {
		super("get.session.length", userId);
	}

}
