/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class CreateUserFileMethod extends AbstractMethod<Long> {

	public CreateUserFileMethod(String userId, String fileName, String fileType) {
		super("create.user.file");
		setParameter(PROP_USER_ID, userId);
		setParameter(PROP_FILE_NAME, fileName);
		setParameter(PROP_FILE_TYPE, fileType);
	}

}
