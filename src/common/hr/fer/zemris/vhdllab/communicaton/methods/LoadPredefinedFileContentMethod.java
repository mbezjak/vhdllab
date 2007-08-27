/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadPredefinedFileContentMethod extends AbstractMethod<String> {

	public LoadPredefinedFileContentMethod(String fileName) {
		super("load.predefined.file.content");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
