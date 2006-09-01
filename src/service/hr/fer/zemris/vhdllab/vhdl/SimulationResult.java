package hr.fer.zemris.vhdllab.vhdl;

import java.util.List;

public class SimulationResult extends Result {

	String waveform;

	public SimulationResult(Integer status, boolean isSuccessful, List<SimulationMessage> messages, String waveform) {
		super(status, isSuccessful, messages);
		this.waveform = waveform;
	}

	public Object getWaveform() {
		return waveform;
	}
}
