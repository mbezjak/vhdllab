/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

/**
 * @author Miro Bezjak
 *
 */
public class ExtractCircuitInterfaceMethod extends AbstractIdParameterMethod<CircuitInterface> {

	public ExtractCircuitInterfaceMethod(Long id) {
		super("extract.circuit.interface", id);
	}

}
