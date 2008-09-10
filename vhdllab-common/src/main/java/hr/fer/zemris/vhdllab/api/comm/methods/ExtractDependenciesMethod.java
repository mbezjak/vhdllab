package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 *
 */
public final class ExtractDependenciesMethod extends AbstractIdParameterMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public ExtractDependenciesMethod(Long id, String userId) {
		super("extract.dependencies", userId, id);
	}

}
