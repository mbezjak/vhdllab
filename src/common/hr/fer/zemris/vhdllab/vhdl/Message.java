package hr.fer.zemris.vhdllab.vhdl;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MessageType type;
	private String messageText;
	private String messageEntity;

	/**
	 * @param entity
	 *            Entity name of unit for which message is generated. Please
	 *            note that this can be an empty string, if appropriate
	 *            information could not be determined.
	 * @param message
	 *            message
	 * @param type TODO
	 */
	public Message(String entity, String message, MessageType type) {
		super();
		if (message == null)
			throw new NullPointerException("Message can not be null");
		this.type = type;
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
	
	public MessageType getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageEntity == null) ? 0 : messageEntity.hashCode());
		result = prime * result
				+ ((messageText == null) ? 0 : messageText.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Message other = (Message) obj;
		if (messageEntity == null) {
			if (other.messageEntity != null)
				return false;
		} else if (!messageEntity.equals(other.messageEntity))
			return false;
		if (messageText == null) {
			if (other.messageText != null)
				return false;
		} else if (!messageText.equals(other.messageText))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (messageEntity != null && !messageEntity.equals(""))
			return messageEntity + ":" + messageText;
		return messageText;
	}
}
