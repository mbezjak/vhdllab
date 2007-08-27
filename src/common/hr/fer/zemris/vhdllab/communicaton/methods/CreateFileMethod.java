/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class CreateFileMethod extends AbstractIdParameterMethod<Long> {

	public CreateFileMethod(Long id, String fileName, String fileType) {
		super("create.file", id);
		setParameter(PROP_FILE_NAME, fileName);
		setParameter(PROP_FILE_TYPE, fileType);
	}

}
