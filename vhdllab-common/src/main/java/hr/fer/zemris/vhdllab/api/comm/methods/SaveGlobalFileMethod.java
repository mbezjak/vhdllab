package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.comm.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public final class SaveGlobalFileMethod extends AbstractIdParameterMethod<Void> {

	private static final long serialVersionUID = 1L;

	public SaveGlobalFileMethod(Long id, String content, String userId) {
		super("save.global.file", userId, id);
		setParameter(PROP_FILE_CONTENT, content);
	}

}
