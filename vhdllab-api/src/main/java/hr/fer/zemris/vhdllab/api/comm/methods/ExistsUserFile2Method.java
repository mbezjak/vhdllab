package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsUserFile2Method extends AbstractMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsUserFile2Method(String fileName, String userId) {
		super("exists.user.file2", userId);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
