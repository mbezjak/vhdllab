package hr.fer.zemris.vhdllab.vhdl;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private String messageText;
	private String messageEntity;

	@Deprecated
	public Message(String message) {
		super();
		if (message == null)
			throw new NullPointerException("Message can not be null");
		this.messageText = message;
	}

	/**
	 * @param entity
	 *            Entity name of unit for which message is generated. Please
	 *            note that this can be an empty string, if appropriate
	 *            information could not be determined.
	 * @param message
	 *            message
	 */
	public Message(String entity, String message) {
		super();
		if (message == null)
			throw new NullPointerException("Message can not be null");
		this.messageText = message;
		this.messageEntity = entity;
	}

	protected Message(Message message) {
		super();
		this.messageText = message.messageText;
		this.messageEntity = message.messageEntity;
	}

	public String getMessageText() {
		return messageText;
	}

	public String getMessageEntity() {
		return messageEntity;
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
		result = prime * result
				+ ((messageEntity == null) ? 0 : messageEntity.hashCode());
		result = prime * result + messageText.hashCode();
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
		if (!(obj instanceof Message))
			return false;
		final Message other = (Message) obj;
		if (messageEntity == null) {
			if (other.messageEntity != null)
				return false;
		} else if (!messageEntity.equals(other.messageEntity))
			return false;
		if (!messageText.equals(other.messageText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (messageEntity != null && !messageEntity.equals(""))
			return "[" + messageEntity + "] " + messageText;
		return messageText;
	}
}
