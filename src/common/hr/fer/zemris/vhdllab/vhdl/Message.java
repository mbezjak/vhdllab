package hr.fer.zemris.vhdllab.vhdl;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.Properties;

public class Message {
	
	protected static final String MESSAGE_TEXT = "message.text";

	private String messageText;

	public Message(String message) {
		super();
		if(message == null) throw new NullPointerException("Message can not be null");
		this.messageText = message;
	}

	public String getMessageText() {
		return messageText;
	}

	public String serialize() {
		Properties prop = new Properties();
		prop.setProperty(MESSAGE_TEXT, messageText);
		return XMLUtil.serializeProperties(prop);
	}
	
	public static Message deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties prop = XMLUtil.deserializeProperties(data);
		if(prop == null) throw new IllegalArgumentException("Unknown serialization format: data");
		String messageText = prop.getProperty(MESSAGE_TEXT);
		return new Message(messageText);
	}
}
