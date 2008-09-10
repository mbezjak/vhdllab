package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class CreateProjectMethod extends AbstractMethod<Long> {

	private static final long serialVersionUID = 1L;

	public CreateProjectMethod(String projectName, String userId) {
		super("create.project", userId);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
