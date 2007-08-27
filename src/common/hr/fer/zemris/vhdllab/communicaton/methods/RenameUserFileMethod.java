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
public class RenameUserFileMethod extends AbstractIdParameterMethod<Void> {

	public RenameUserFileMethod(Long id, String fileName) {
		super("rename.user.file", id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
