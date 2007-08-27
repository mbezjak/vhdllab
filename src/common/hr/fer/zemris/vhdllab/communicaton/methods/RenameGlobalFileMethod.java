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
public class RenameGlobalFileMethod extends AbstractIdParameterMethod<Void> {

	public RenameGlobalFileMethod(Long id, String fileName) {
		super("rename.global.file", id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
