package hr.fer.zemris.vhdllab.vhdl;

import java.util.List;

public class CompilationResult extends Result {

	public CompilationResult(Integer status, boolean isSuccessful, List<? extends CompilationMessage> messages) {
		super(status, isSuccessful, messages);
	}
	
	@SuppressWarnings("unchecked")
	public static CompilationResult deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Result result = Result.deserialize(data);
		Integer status = result.getStatus();
		boolean isSuccessful = result.isSuccessful();
		List<CompilationMessage> messages = (List<CompilationMessage>) result.getMessages();
		return new CompilationResult(status, isSuccessful, messages);
	}
}