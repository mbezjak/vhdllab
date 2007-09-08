/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

/**
 * @author Miro Bezjak
 *
 */
public final class SimulateFileMethod extends AbstractIdParameterMethod<SimulationResult> {

	private static final long serialVersionUID = 1L;

	public SimulateFileMethod(Long id) {
		super("simulate.file", id);
	}

}
