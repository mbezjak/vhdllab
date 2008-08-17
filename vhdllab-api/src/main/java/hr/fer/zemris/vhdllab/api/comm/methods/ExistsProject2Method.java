package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsProject2Method extends AbstractMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsProject2Method(String projectName, String userId) {
		super("exists.project2", userId);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
