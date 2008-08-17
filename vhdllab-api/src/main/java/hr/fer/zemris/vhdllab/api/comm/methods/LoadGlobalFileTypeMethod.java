package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadGlobalFileTypeMethod extends AbstractIdParameterMethod<String> {

	private static final long serialVersionUID = 1L;

	public LoadGlobalFileTypeMethod(Long id, String userId) {
		super("load.global.file.type", userId, id);
	}

}
