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
public final class ExtractHierarchyMethod extends AbstractIdParameterMethod<Hierarchy> {

	private static final long serialVersionUID = 1L;

	public ExtractHierarchyMethod(Long id) {
		super("extract.hierarchy", id);
	}

}
