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
public class RenameProjectMethod extends AbstractIdParameterMethod<Void> {

	public RenameProjectMethod(Long id, String projectName) {
		super("rename.project", id);
		setParameter(PROP_PROJECT_NAME, projectName);
	}

}
