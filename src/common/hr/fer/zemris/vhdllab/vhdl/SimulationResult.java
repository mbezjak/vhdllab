package hr.fer.zemris.vhdllab.vhdl;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.List;
import java.util.Properties;

public class SimulationResult extends Result {
	
	protected static final String SIMULATION_RESULT_SUPER = "simulation.result.super";
	protected static final String SIMULATION_RESULT_WAVEFORM = "simulation.result.waveform";

	private String waveform;

	public SimulationResult(Integer status, boolean isSuccessful, List<? extends SimulationMessage> messages, String waveform) {
		super(status, isSuccessful, messages);
		this.waveform = waveform;
	}

	public String getWaveform() {
		return waveform;
	}
	
	@Override
	public String serialize() {
		Properties prop = new Properties();
		String superSerialization = super.serialize();
		prop.setProperty(SIMULATION_RESULT_SUPER, superSerialization);
		if(waveform != null) {
			prop.setProperty(SIMULATION_RESULT_WAVEFORM, waveform);
		}
		return XMLUtil.serializeProperties(prop);
	}
	
	@SuppressWarnings("unchecked")
	public static SimulationResult deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties prop = XMLUtil.deserializeProperties(data);
		if(prop == null) throw new IllegalArgumentException("Unknown serialization format: data");
		String waveform = prop.getProperty(SIMULATION_RESULT_WAVEFORM);
		String superSerialization = prop.getProperty(SIMULATION_RESULT_SUPER);
		Result result = Result.deserialize(superSerialization);
		Integer status = result.getStatus();
		boolean isSuccessful = result.isSuccessful();
		List<SimulationMessage> messages = (List<SimulationMessage>) result.getMessages();
		return new SimulationResult(status, isSuccessful, messages, waveform);
	}
}
