package hr.fer.zemris.vhdllab.vhdl;

import java.io.ObjectStreamException;
import java.util.List;

public class CompilationResult extends Result {

	private static final long serialVersionUID = 1L;

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

	@Override
	public String toString() {
		return super.toString();
	}
	
	/**
	 * Make a defensive copy.
	 */
	@Override
	protected Object readResolve() throws ObjectStreamException {
		Result result = (Result) super.readResolve();
		return new CompilationResult(result);
	}

}