package hr.fer.zemris.vhdllab.vhdl;

import java.io.ObjectStreamException;
import java.util.List;

public class SimulationResult extends Result {

	private static final long serialVersionUID = 1L;
	private String waveform;

	public SimulationResult(Integer status, boolean successful,
			List<? extends SimulationMessage> messages, String waveform) {
		super(status, successful, messages);
		this.waveform = waveform;
	}

	protected SimulationResult(Result result, String waveform) {
		super(result);
		this.waveform = waveform;
	}

	public String getWaveform() {
		return waveform;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends SimulationMessage> getMessages() {
		return (List<? extends SimulationMessage>) super.getMessages();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((waveform == null) ? 0 : waveform.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof SimulationResult))
			return false;
		final SimulationResult other = (SimulationResult) obj;
		if (waveform == null) {
			if (other.waveform != null)
				return false;
		} else if (!waveform.equals(other.waveform))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.Result#toString()
	 */
	@Override
	public String toString() {
		return super.toString()
				+ (waveform != null ? " waveform: " + waveform : "");
	}

	/**
	 * Make a defensive copy.
	 */
	@Override
	protected Object readResolve() throws ObjectStreamException {
		Result result = (Result) super.readResolve();
		return new SimulationResult(result, waveform);
	}

}
