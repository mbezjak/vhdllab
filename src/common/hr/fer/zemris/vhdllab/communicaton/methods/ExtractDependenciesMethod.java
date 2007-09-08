/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 *
 */
public final class ExtractDependenciesMethod extends AbstractIdParameterMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public ExtractDependenciesMethod(Long id) {
		super("extract.dependencies", id);
	}

}
