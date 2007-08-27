/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsProject2Method extends AbstractMethod<Boolean> {

	public ExistsProject2Method(String userId, String projectName) {
		super("exists.project2");
		setParameter(PROP_USER_ID, userId);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
