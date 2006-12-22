package hr.fer.zemris.vhdllab.vhdl;

import java.util.List;

public class CompilationResult extends Result {

	public CompilationResult(Integer status, boolean isSuccessful, List<? extends CompilationMessage> messages) {
		super(status, isSuccessful, messages);
	}
	
	protected CompilationResult(Result result) {
		super(result);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends CompilationMessage> getMessages() {
		return (List<? extends CompilationMessage>)super.getMessages();
	}

	public static CompilationResult deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Result result = Result.deserialize(data);
		return new CompilationResult(result);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}