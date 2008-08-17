package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsGlobalFileMethod extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsGlobalFileMethod(Long id, String userId) {
		super("exists.global.file", userId, id);
	}

}
