/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.communicaton.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public final class RenameProjectMethod extends AbstractIdParameterMethod<Void> {

	private static final long serialVersionUID = 1L;

	public RenameProjectMethod(Long id, String projectName) {
		super("rename.project", id);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
