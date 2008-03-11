package hr.fer.zemris.vhdllab.server.results;

import java.io.Serializable;

/**
 * Represents a single result message.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class Message implements Serializable {
	/*
	 * This class is immutable and therefor thread-safe.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * A type of a message.
	 */
	private final MessageType type;
	/**
	 * A text of a message.
	 */
	private final String messageText;
	/**
	 * Entity name of a unit for which message is generated. Please note that
	 * this can be an empty string, if appropriate information could not be
	 * determined.
	 */
	private final String entityName;

	/**
	 * Copy constructor
	 * 
	 * @param message
	 *            a message to copy
	 * @throws NullPointerException
	 *             if <code>message</code> is <code>null</code>
	 */
	protected Message(Message message) {
		if (message == null) {
			throw new NullPointerException("Message cant be null");
		}
		if(message.getType() == null) {
			throw new NullPointerException("Message type cant be null");
		}
		if(message.getMessageText() == null) {
			throw new NullPointerException("Message text cant be null");
		}
		this.type = message.getType();
		this.messageText = message.getMessageText();
		this.entityName = message.getEntityName();
	}

	/**
	 * Constructs a message out of specified parameters. Entity name will be set
	 * to <code>null</code>.
	 * 
	 * @param type
	 *            a type of a message
	 * @param messageText
	 *            a text of a message
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public Message(MessageType type, String messageText) {
		this(type, messageText, null);
	}

	/**
	 * Constructs a message out of specified parameters. Entity name will be set
	 * to <code>null</code>.
	 * 
	 * @param type
	 *            a type of a message
	 * @param messageText
	 *            a text of a message
	 * @param entityName
	 *            an entity name of a unit for which message is generated.
	 *            Please note that this can be an empty string, if appropriate
	 *            information could not be determined
	 * @throws NullPointerException
	 *             if <code>type</code> or <code>messageText</code> is
	 *             <code>null</code>
	 */
	public Message(MessageType type, String messageText, String entityName) {
		if (type == null) {
			throw new NullPointerException("Message type cant be null");
		}
		if (messageText == null) {
			throw new NullPointerException("Message text cant be null");
		}
		this.type = type;
		this.messageText = messageText;
		this.entityName = entityName;
	}

	/**
	 * Returns a type of a message.
	 * 
	 * @return a type of a message
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * Returns a text of a message.
	 * 
	 * @return a text of a message
	 */
	public String getMessageText() {
		return messageText;
	}

	/**
	 * Returns an entity name of a unit for which message is generated. Please
	 * note that this can be an empty string, if appropriate information could
	 * not be determined
	 * 
	 * @return an entity name of a unit for which message is generated
	 */
	public String getEntityName() {
		return entityName;
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
		result = prime * result + type.hashCode();
		result = prime * result
				+ ((entityName == null) ? 0 : entityName.hashCode());
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
		if (!type.equals(other.type))
			return false;
		if (entityName == null) {
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName))
			return false;
		if (!messageText.equals(other.messageText))
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
		StringBuilder sb = new StringBuilder(50);
		if (entityName != null && !entityName.equals("")) {
			sb.append("@").append(entityName);
		}
		sb.append(":").append(messageText);
		return sb.toString();
	}

	/**
	 * Make a defensive copy.
	 */
	protected Object readResolve() {
		return new Message(type, messageText, entityName);
	}

}
