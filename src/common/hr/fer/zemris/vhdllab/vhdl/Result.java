package hr.fer.zemris.vhdllab.vhdl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer status;
	private boolean successful;
	private List<? extends Message> messages;

	public Result(Integer status, boolean successful,
			List<? extends Message> messages) {
		super();
		if (status == null || messages == null) {
			throw new NullPointerException("Status or message can not be null.");
		}
		this.status = status;
		this.successful = successful;
		this.messages = Collections.unmodifiableList(messages);
	}

	protected Result(Result result) {
		this(result.status, result.successful, result.messages);
	}

	public Integer getStatus() {
		return status;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public List<? extends Message> getMessages() {
		return messages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + status.hashCode();
		result = prime * result + (successful ? 1231 : 1237);
		result = prime * result + messages.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Result))
			return false;
		final Result other = (Result) obj;
		if (!status.equals(other.status))
			return false;
		if (successful != other.successful)
			return false;
		if (!messages.equals(other.messages))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Status: " + status + " "
				+ (successful ? "successful" : "failure") + ": " + messages;
	}

	/**
	 * Make a defensive copy.
	 */
	protected Object readResolve() throws ObjectStreamException {
		return new Result(this);
	}

}