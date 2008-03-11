package hr.fer.zemris.vhdllab.server.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a some kind of a result.
 * 
 * @param <T>
 *            a message type
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class Result<T extends Message> implements Serializable {
	/*
	 * This class is immutable and therefor thread-safe.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * A result status. For example, an exit code of processes exec method.
	 */
	private final Integer status;
	/**
	 * A flag indicating if result finished successfully.
	 */
	private final boolean successful;
	/**
	 * A list of result messages.
	 */
	private final List<T> messages;

	/**
	 * Copy constructor
	 * 
	 * @param result
	 *            a result to copy
	 * @throws NullPointerException
	 *             if <code>result</code> is <code>null</code>
	 */
	protected Result(Result<T> result) {
		if (result == null) {
			throw new NullPointerException("Result cant be null");
		}
		if (result.status == null) {
			throw new NullPointerException("Status cant be null");
		}
		if (result.messages == null) {
			throw new NullPointerException("Messages cant be null");
		}
		this.status = result.status;
		this.successful = result.successful;
		this.messages = Collections.unmodifiableList(new ArrayList<T>(
				result.messages));
	}

	/**
	 * Constructs a result out of specified parameters. Status will be set based
	 * on <code>successful</code> flag:
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
	 *             if <code>messages</code> are <code>null</code>
	 */
	public Result(boolean successful, List<T> messages) {
		this(successful ? Integer.valueOf(0) : Integer.valueOf(1), successful,
				messages);
	}

	/**
	 * Constructs a result out of specified parameters.
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
	public Result(Integer status, boolean successful, List<T> messages) {
		if (status == null) {
			throw new NullPointerException("Status cant be null");
		}
		if (messages == null) {
			throw new NullPointerException("Messages cant be null");
		}
		this.status = status;
		this.successful = successful;
		this.messages = Collections
				.unmodifiableList(new ArrayList<T>(messages));
	}

	/**
	 * Returns a status result.
	 * 
	 * @return a status result
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Returns a flag indicating if result finished successfully.
	 * 
	 * @return a flag indicating if result finished successfully
	 */
	public boolean isSuccessful() {
		return successful;
	}

	/**
	 * Returns an unmodifiable list of result messages.
	 * 
	 * @return an unmodifiable list of result messages
	 */
	public List<T> getMessages() {
		return messages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (successful ? 1231 : 1237);
		result = prime * result + status.hashCode();
		result = prime * result + messages.hashCode();
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
		if (obj == null)
			return false;
		if (!(obj instanceof Result))
			return false;
		final Result<?> other = (Result<?>) obj;
		if (successful != other.successful)
			return false;
		if (!status.equals(other.status))
			return false;
		if (!messages.equals(other.messages))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(15 + messages.size() * 50);
		sb.append("status=").append(status);
		sb.append(", successful=").append(successful);
		sb.append(", messages={").append(messages).append("}");
		return sb.toString();
	}

	/**
	 * Make a defensive copy.
	 */
	protected Object readResolve() {
		return new Result<T>(status, successful, messages);
	}

}
