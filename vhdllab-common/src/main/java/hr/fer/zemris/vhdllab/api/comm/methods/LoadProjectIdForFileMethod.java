package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadProjectIdForFileMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public LoadProjectIdForFileMethod(Long id, String userId) {
		super("load.project.id.for.file", userId, id);
	}

}
