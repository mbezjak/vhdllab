package hr.fer.zemris.vhdllab.vhdl;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Result {

	protected static final String PROP_RESULT_STATUS = "result.status";
	protected static final String PROP_RESULT_IS_SUCCESSFUL = "result.is.successful";
	protected static final String PROP_RESULT_MESSAGE_TYPE = "result.message.type";
	protected static final String PROP_RESULT_MESSAGE_SERIALIZATION = "result.message.serialization";
	
	private Integer status;
	private boolean isSuccessful;
	private List<? extends Message> messages;
	
	public Result(Integer status, boolean isSuccessful, List<? extends Message> messages) {
		super();
		if(status == null || messages == null) {
			throw new NullPointerException("Status or message can not be null.");
		}
		this.status = status;
		this.isSuccessful = isSuccessful;
		this.messages = Collections.unmodifiableList(messages);
	}
	
	protected Result(Result result) {
		super();
		this.status = result.status;
		this.isSuccessful = result.isSuccessful;
		this.messages = result.messages;
	}

	public Integer getStatus() {
		return status;
	}
	
	public boolean isSuccessful() {
		return isSuccessful;
	}

	public List<? extends Message> getMessages() {
		return messages;
	}
	
	public String serialize() {
		Properties prop = new Properties();
		prop.setProperty(PROP_RESULT_STATUS, String.valueOf(status.intValue()));
		prop.setProperty(PROP_RESULT_IS_SUCCESSFUL, String.valueOf(isSuccessful));
		
		int i = 1;
		for(Message msg : messages) {
			prop.setProperty(PROP_RESULT_MESSAGE_TYPE + "." + i, msg.getClass().getCanonicalName());
			prop.setProperty(PROP_RESULT_MESSAGE_SERIALIZATION + "." + i, msg.serialize());
			i++;
		}
		return XMLUtil.serializeProperties(prop);
	}

	public static Result deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties prop = XMLUtil.deserializeProperties(data);
		if(prop == null) throw new IllegalArgumentException("Unknown serialization format: data");
		Integer status = Integer.parseInt(prop.getProperty(PROP_RESULT_STATUS));
		boolean isSuccessful = Boolean.parseBoolean(prop.getProperty(PROP_RESULT_IS_SUCCESSFUL));
		List<Message> messages = new ArrayList<Message>();
		for(int i = 1; true; i++) {
			String className = prop.getProperty(PROP_RESULT_MESSAGE_TYPE + "." + i);
			if(className == null) break;
			String methodData = prop.getProperty(PROP_RESULT_MESSAGE_SERIALIZATION + "." + i);
			try {
				Method method = Class.forName(className).getMethod("deserialize", new Class[] {String.class});
				Message msg = (Message)method.invoke(null, new Object[] {methodData});
				messages.add(msg);
			} catch (Exception e) {
				throw new IllegalStateException("Class " + className + " does not support " +
						"deserialize(String) method.");
			}
		}
		return new Result(status, isSuccessful, messages);
	}
	
	@Override
	public String toString() {
		return "Status: "+status+" "+(isSuccessful?"successful" : "failure")+": "+messages;
	}
}