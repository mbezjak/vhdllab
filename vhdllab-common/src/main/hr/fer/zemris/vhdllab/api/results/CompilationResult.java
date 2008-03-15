package hr.fer.zemris.vhdllab.api.results;

import java.util.List;

/**
 * Represents a result of a compilation.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class CompilationResult extends Result<CompilationMessage> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a compilation result out of specified parameters. Status will
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
	 * @throws NullPointerException
	 *             if <code>messages</code> is <code>null</code>
	 */
	public CompilationResult(boolean successful,
			List<CompilationMessage> messages) {
		super(successful, messages);
	}

	/**
	 * Constructs a compilation result out of specified parameters.
	 * 
	 * @param status
	 *            a result status
	 * @param successful
	 *            a flag indicating if result finished successfully
	 * @param messages
	 *            a list of result messages
	 * @throws NullPointerException
	 *             if <code>status</code> or <code>messages</code> are
	 *             <code>null</code>
	 */
	public CompilationResult(Integer status, boolean successful,
			List<CompilationMessage> messages) {
		super(status, successful, messages);
	}

}
