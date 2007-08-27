/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsPredefinedFileMethod extends AbstractMethod<String> {

	public ExistsPredefinedFileMethod(String fileName) {
		super("exists.predefined.file");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
