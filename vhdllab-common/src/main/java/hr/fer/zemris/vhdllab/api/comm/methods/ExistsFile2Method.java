package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsFile2Method extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsFile2Method(Long id, String fileName, String userId) {
		super("exists.file2", userId, id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
