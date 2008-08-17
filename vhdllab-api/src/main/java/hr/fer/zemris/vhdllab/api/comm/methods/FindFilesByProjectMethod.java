package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 *
 */
public final class FindFilesByProjectMethod extends AbstractIdParameterMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public FindFilesByProjectMethod(Long id, String userId) {
		super("find.files.by.project", userId, id);
	}

}
