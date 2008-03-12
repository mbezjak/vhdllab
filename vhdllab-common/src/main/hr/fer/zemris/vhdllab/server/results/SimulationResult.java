package hr.fer.zemris.vhdllab.server.results;

import java.util.List;

/**
 * Represents a simulation result.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class SimulationResult extends ContentResult<SimulationMessage> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a simulation result out of specified parameters. Status will
	 * be set based on <code>successful</code> flag:
	 * <p>
	 * <blockquote>
	 * <code>successful ? Integer.valueOf(0) : Integer.valueOf(1)</code>
	 * </blockquote>
	 * </p>
	 * 
	 * @param successful
	 *            a flag indicating if result finished successfully
	 * @param messages
	 *            a list of result messages
	 * @param content
	 *            a result content
	 * @throws NullPointerException
	 *             if <code>messages</code> or <code>content</code> are
	 *             <code>null</code>
	 */
	public SimulationResult(boolean successful,
			List<SimulationMessage> messages, String content) {
		super(null, successful, messages, content);
	}

	/**
	 * Constructs a simulation result out of specified parameters.
	 * 
	 * @param status
	 *            a result status
	 * @param successful
	 *            a flag indicating if result finished successfully
	 * @param messages
	 *            a list of result messages
	 * @param content
	 *            a result content
	 * @throws NullPointerException
	 *             if <code>status</code>, <code>messages</code> or
	 *             <code>content</code> are <code>null</code>
	 */
	public SimulationResult(Integer status, boolean successful,
			List<SimulationMessage> messages, String content) {
		super(status, successful, messages, content);
	}

	/**
	 * Returns a waveform.
	 * 
	 * @return a waveform
	 */
	protected String getWaveform() {
		return getContent();
	}

}
