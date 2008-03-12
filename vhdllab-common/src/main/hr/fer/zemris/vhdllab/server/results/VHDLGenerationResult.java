package hr.fer.zemris.vhdllab.server.results;

import java.util.List;

/**
 * Represents a vhdl generation result.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class VHDLGenerationResult extends
		ContentResult<VHDLGenerationMessage> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a vhdl generation result out of specified parameters. Status
	 * will be set based on <code>successful</code> flag:
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
	public VHDLGenerationResult(boolean successful,
			List<VHDLGenerationMessage> messages, String content) {
		super(null, successful, messages, content);
	}

	/**
	 * Constructs a vhdl generation result out of specified parameters.
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
	public VHDLGenerationResult(Integer status, boolean successful,
			List<VHDLGenerationMessage> messages, String content) {
		super(status, successful, messages, content);
	}

	/**
	 * Returns a vhdl code.
	 * 
	 * @return a vhdl code
	 */
	protected String getVHDL() {
		return getContent();
	}

}
