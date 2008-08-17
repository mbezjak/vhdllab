package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;



/**
 * @author Miro Bezjak
 *
 */
public final class IsSuperuserMethod extends AbstractMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public IsSuperuserMethod(String userId) {
		super("is.superuser", userId);
	}

}
