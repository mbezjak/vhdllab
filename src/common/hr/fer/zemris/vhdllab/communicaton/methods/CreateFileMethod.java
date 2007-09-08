/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class CreateFileMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public CreateFileMethod(Long id, String fileName, String fileType) {
		super("create.file", id);
		setParameter(PROP_FILE_NAME, fileName);
		setParameter(PROP_FILE_TYPE, fileType);
	}

}
