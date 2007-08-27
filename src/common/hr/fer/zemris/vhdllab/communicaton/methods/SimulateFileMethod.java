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
public class SimulateFileMethod extends AbstractIdParameterMethod<SimulationResult> {

	public SimulateFileMethod(Long id) {
		super("simulate.file", id);
	}

}
