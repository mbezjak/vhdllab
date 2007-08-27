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
public class SaveFileMethod extends AbstractIdParameterMethod<Void> {

	public SaveFileMethod(Long id, String content) {
		super("save.file", id);
		setParameter(PROP_FILE_CONTENT, content);
	}

}
