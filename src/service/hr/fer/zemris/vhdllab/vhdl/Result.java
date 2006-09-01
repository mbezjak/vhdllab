package hr.fer.zemris.vhdllab.vhdl;

import java.util.Collections;
import java.util.List;

public class Result {

	Integer status;
	boolean isSuccessful;
	List<? extends Message> messages;
	
	public Result(Integer status, boolean isSuccessful, List<? extends Message> messages) {
		super();
		this.status = status;
		this.isSuccessful = isSuccessful;
		this.messages = Collections.unmodifiableList(messages);
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
}