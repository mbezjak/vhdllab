package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class CreateFileMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public CreateFileMethod(Long id, String fileName, String fileType, String userId) {
		super("create.file", userId, id);
		setParameter(PROP_FILE_NAME, fileName);
		setParameter(PROP_FILE_TYPE, fileType);
	}

}
