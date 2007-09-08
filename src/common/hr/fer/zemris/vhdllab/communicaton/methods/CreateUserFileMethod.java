/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class CreateUserFileMethod extends AbstractMethod<Long> {

	private static final long serialVersionUID = 1L;

	public CreateUserFileMethod(String fileName, String fileType) {
		super("create.user.file");
		setParameter(PROP_FILE_NAME, fileName);
		setParameter(PROP_FILE_TYPE, fileType);
	}

}
