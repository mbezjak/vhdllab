/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class FindProjectByNameMethod extends AbstractMethod<Long> {

	private static final long serialVersionUID = 1L;

	public FindProjectByNameMethod(String projectName) {
		super("find.project.by.name");
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
