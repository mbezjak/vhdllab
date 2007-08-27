/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

/**
 * @author Miro Bezjak
 *
 */
public class ExtractHierarchyMethod extends AbstractIdParameterMethod<Hierarchy> {

	public ExtractHierarchyMethod(Long id) {
		super("extract.hierarchy", id);
	}

}
