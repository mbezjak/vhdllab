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
public final class ExtractCircuitInterfaceMethod extends AbstractIdParameterMethod<CircuitInterface> {

	private static final long serialVersionUID = 1L;

	public ExtractCircuitInterfaceMethod(Long id) {
		super("extract.circuit.interface", id);
	}

}
