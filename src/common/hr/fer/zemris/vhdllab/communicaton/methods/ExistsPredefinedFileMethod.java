/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsPredefinedFileMethod extends AbstractMethod<String> {

	private static final long serialVersionUID = 1L;

	public ExistsPredefinedFileMethod(String fileName) {
		super("exists.predefined.file");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
