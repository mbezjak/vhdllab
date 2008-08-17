package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;

/**
 * @author Miro Bezjak
 *
 */
public final class SimulateFileMethod extends AbstractIdParameterMethod<SimulationResult> {

	private static final long serialVersionUID = 1L;

	public SimulateFileMethod(Long id, String userId) {
		super("simulate.file", userId, id);
	}

}
