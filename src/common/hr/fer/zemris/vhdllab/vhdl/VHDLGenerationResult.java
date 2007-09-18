package hr.fer.zemris.vhdllab.vhdl;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

public final class VHDLGenerationResult extends Result {

	private static final long serialVersionUID = 1L;
	
	private String vhdl;
	
	@SuppressWarnings("unchecked")
	public VHDLGenerationResult(String vhdl) {
		this(Integer.valueOf(0), true, new ArrayList<VHDLGenerationMessage>(0), vhdl);
	}
	
	public VHDLGenerationResult(Integer status, boolean successful,
			List<? extends VHDLGenerationMessage> messages, String vhdl) {
		super(status, successful, messages);
		this.vhdl = vhdl;
	}
	
	protected VHDLGenerationResult(Result result, String vhdl) {
		super(result);
		this.vhdl = vhdl;
	}
	
	public String getVhdl() {
		return vhdl;
	}

	/**
	 * Make a defensive copy.
	 */
	@Override
	protected Object readResolve() throws ObjectStreamException {
		Result result = (Result) super.readResolve();
		return new VHDLGenerationResult(result, vhdl);
	}

}
