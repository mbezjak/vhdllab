/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class CreateProjectMethod extends AbstractMethod<Long> {

	private static final long serialVersionUID = 1L;

	public CreateProjectMethod(String projectName) {
		super("create.project");
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
