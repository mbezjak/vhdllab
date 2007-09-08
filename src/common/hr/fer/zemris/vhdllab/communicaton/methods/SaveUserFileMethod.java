/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.communicaton.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public final class SaveUserFileMethod extends AbstractIdParameterMethod<Void> {

	private static final long serialVersionUID = 1L;

	public SaveUserFileMethod(Long id, String content) {
		super("save.user.file", id);
		setParameter(PROP_FILE_CONTENT, content);
	}

}
