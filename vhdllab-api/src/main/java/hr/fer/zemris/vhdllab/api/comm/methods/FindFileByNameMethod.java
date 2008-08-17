package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class FindFileByNameMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public FindFileByNameMethod(Long id, String fileName, String userId) {
		super("find.file.by.name", userId, id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
