package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.generic;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IGenericValue;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;







public class ParamPort implements IGenericValue {
	
	/* private fields */
	private DefaultPort port;
	
	
	
	/* ctors */
	
	public ParamPort() { }
	public ParamPort(DefaultPort p) { port = p; }
	
	
	
	/* methods */
	
	public IGenericValue copyCtor() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	public void deserialize(String code) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	public String serialize() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
}


















