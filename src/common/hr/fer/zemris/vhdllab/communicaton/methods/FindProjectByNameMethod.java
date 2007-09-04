/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class FindProjectByNameMethod extends AbstractMethod<Long> {

	public FindProjectByNameMethod(String projectName) {
		super("find.project.by.name");
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
