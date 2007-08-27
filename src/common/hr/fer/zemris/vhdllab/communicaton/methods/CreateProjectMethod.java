/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class CreateProjectMethod extends AbstractMethod<Long> {

	public CreateProjectMethod(String userId, String projectName) {
		super("create.project");
		setParameter(PROP_USER_ID, userId);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
