package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class FindUserFileByNameMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public FindUserFileByNameMethod(Long id, String fileName, String userId) {
		super("find.user.file.by.name", userId, id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
