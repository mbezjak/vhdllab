package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class FindProjectByNameMethod extends AbstractMethod<Long> {

	private static final long serialVersionUID = 1L;

	public FindProjectByNameMethod(String projectName, String userId) {
		super("find.project.by.name", userId);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
