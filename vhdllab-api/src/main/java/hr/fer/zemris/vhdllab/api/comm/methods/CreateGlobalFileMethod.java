package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class CreateGlobalFileMethod extends AbstractMethod<Long> {

	private static final long serialVersionUID = 1L;

	public CreateGlobalFileMethod(String fileName, String fileType, String userId) {
		super("create.global.file", userId);
		setParameter(PROP_FILE_NAME, fileName);
		setParameter(PROP_FILE_TYPE, fileType);
	}

}
