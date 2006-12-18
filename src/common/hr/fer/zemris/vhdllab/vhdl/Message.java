package hr.fer.zemris.vhdllab.vhdl;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.Properties;

public class Message {
	
	protected static final String MESSAGE_TEXT = "message.text";
	protected static final String MESSAGE_ENTITY = "message.entity";

	private String messageText;
	private String messageEntity;

	@Deprecated
	public Message(String message) {
		super();
		if(message == null) throw new NullPointerException("Message can not be null");
		this.messageText = message;
	}

	/**
	 * @param entity Entity name of unit for which message is generated. Please note that this
	 *               can be an empty string, if appropriate information could not be determined.  
	 * @param message message
	 */
	public Message(String entity, String message) {
		super();
		if(message == null) throw new NullPointerException("Message can not be null");
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
	
	public String serialize() {
		Properties prop = new Properties();
		prop.setProperty(MESSAGE_TEXT, messageText);
		prop.setProperty(MESSAGE_ENTITY, messageEntity);
		return XMLUtil.serializeProperties(prop);
	}
	
	public static Message deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties prop = XMLUtil.deserializeProperties(data);
		if(prop == null) throw new IllegalArgumentException("Unknown serialization format: data");
		String messageText = prop.getProperty(MESSAGE_TEXT);
		String messageEntity = prop.getProperty(MESSAGE_ENTITY,null);
		return new Message(messageEntity, messageText);
	}
	
	@Override
	public String toString() {
		if(messageEntity!=null && !messageEntity.equals("")) return "["+messageEntity+"] "+messageText;
		return messageText;
	}
}
