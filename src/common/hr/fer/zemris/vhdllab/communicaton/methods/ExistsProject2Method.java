/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsProject2Method extends AbstractMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsProject2Method(String projectName) {
		super("exists.project2");
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
