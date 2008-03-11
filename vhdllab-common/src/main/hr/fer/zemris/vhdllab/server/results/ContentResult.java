package hr.fer.zemris.vhdllab.server.results;

import java.util.List;

/**
 * Represents a result that has a content.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ContentResult<T extends Message> extends Result<T> {
	/*
	 * This class is immutable and therefor thread-safe.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * A result content.
	 */
	private final String content;

	/**
	 * Copy constructor.
	 * 
	 * @param result
	 *            a result to copy
	 * @param content
	 *            a result content
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	protected ContentResult(Result<T> result, String content) {
		super(result);
		if (content == null) {
			throw new NullPointerException("Content cant be null");
		}
		this.content = content;
	}

	/**
	 * Constructs a content result out of specified parameters. Status will be
	 * set based on <code>successful</code> flag:
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
	public ContentResult(boolean successful, List<T> messages, String content) {
		this(null, successful, messages, content);
	}

	/**
	 * Constructs a content result out of specified parameters.
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
	public ContentResult(Integer status, boolean successful, List<T> messages,
			String content) {
		super(status, successful, messages);
		if (content == null) {
			throw new NullPointerException("Content cant be null");
		}
		this.content = content;
	}

	/**
	 * Returns a result content.
	 * 
	 * @return a result content
	 */
	protected String getContent() {
		return content;
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
		result = prime * result + content.hashCode();
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
		if (!(obj instanceof ContentResult))
			return false;
		final ContentResult<?> other = (ContentResult<?>) obj;
		return content.equals(other.content);
	}

	/**
	 * Make a defensive copy.
	 */
	@Override
	protected Object readResolve() {
		return new ContentResult<T>(this, content);
	}

}
