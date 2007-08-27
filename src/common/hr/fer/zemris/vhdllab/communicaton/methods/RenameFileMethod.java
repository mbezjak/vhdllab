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
public class RenameFileMethod extends AbstractIdParameterMethod<Void> {

	public RenameFileMethod(Long id, String fileName) {
		super("rename.file", id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
