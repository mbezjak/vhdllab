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
public class SaveGlobalFileMethod extends AbstractIdParameterMethod<Void> {

	public SaveGlobalFileMethod(Long id, String content) {
		super("save.global.file", id);
		setParameter(PROP_FILE_CONTENT, content);
	}

}
