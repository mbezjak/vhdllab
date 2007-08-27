/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsUserFile2Method extends AbstractMethod<Boolean> {

	public ExistsUserFile2Method(String userId, String fileName) {
		super("exists.user.file2");
		setParameter(PROP_USER_ID, userId);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
