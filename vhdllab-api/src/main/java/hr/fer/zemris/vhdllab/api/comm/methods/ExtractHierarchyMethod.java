package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;

/**
 * @author Miro Bezjak
 *
 */
public final class ExtractHierarchyMethod extends AbstractIdParameterMethod<Hierarchy> {

	private static final long serialVersionUID = 1L;

	public ExtractHierarchyMethod(Long id, String userId) {
		super("extract.hierarchy", userId, id);
	}

}
