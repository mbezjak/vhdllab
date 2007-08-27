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
public class ExtractDependenciesMethod extends AbstractIdParameterMethod<ArrayList<Long>> {

	public ExtractDependenciesMethod(Long id) {
		super("extract.dependencies", id);
	}

}
