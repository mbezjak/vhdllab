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

	public ExistsProject2Method(String projectName) {
		super("exists.project2");
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
