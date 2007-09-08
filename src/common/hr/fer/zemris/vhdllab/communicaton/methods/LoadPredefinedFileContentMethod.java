/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadPredefinedFileContentMethod extends AbstractMethod<String> {

	private static final long serialVersionUID = 1L;

	public LoadPredefinedFileContentMethod(String fileName) {
		super("load.predefined.file.content");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
