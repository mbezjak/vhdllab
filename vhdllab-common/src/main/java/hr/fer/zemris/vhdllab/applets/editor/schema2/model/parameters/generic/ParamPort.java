package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic;

import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IGenericValue;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;







public class ParamPort implements IGenericValue {
	
	/* static fields */
	private static final String DELIMITER = "#";
	
	/* private fields */
	private Port port;
	
	
	
	/* ctors */
	
	public ParamPort() { }
	public ParamPort(Port p) { port = p; }
	
	
	
	/* methods */
	
	public Port getPort() {
		return port;
	}
	
	public IGenericValue copyCtor() {
		Type thtp = port.getType();
		Range range = Range.SCALAR;
		if (thtp.getRange().isVector()) {
		    range = new Range(thtp.getRange().getFrom(), thtp.getRange().getDirection() ,thtp.getRange().getTo());
		}
		Type tp = new Type(thtp.getTypeName(), range);
		Port dp = new Port(port.getName(), port.getDirection(), tp);
		
		return new ParamPort(dp);
	}
	
	public void deserialize(String code) {
		this.port = PortFactory.createPort(createPortWrapper(code));
	}
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(port.getName()).append(DELIMITER);
		sb.append(port.getDirection().toString()).append(DELIMITER);
		Type pt = port.getType();
		sb.append(pt.getTypeName()).append(DELIMITER);
		if (pt.getRange().isScalar()) {
			sb.append(DELIMITER).append(DELIMITER);
		} else {
			sb.append(fromVecDir(pt.getRange().getDirection().toString())).append(DELIMITER);
			sb.append(Integer.toString(pt.getRange().getFrom())).append(DELIMITER);
			sb.append(Integer.toString(pt.getRange().getTo()));
		}
		
		return sb.toString();
	}
	
	private String fromVecDir(String vectorDirection) {
		if (vectorDirection.equals(VectorDirection.TO.toString())) return PortWrapper.ASCEND;
		else if (vectorDirection.equals(VectorDirection.DOWNTO.toString())) return PortWrapper.DESCEND;
		else throw new IllegalStateException("Vector direction '" + vectorDirection + "' is unknown.");
	}
	
	private PortWrapper createPortWrapper(String code) {
		String[] sf = code.split(DELIMITER);
		
		PortWrapper pw = new PortWrapper();
		pw.setName(sf[0]);
		pw.setDirection(sf[1]);
		pw.setType(sf[2]);
		if (sf[2].equalsIgnoreCase("std_logic_vector")) {
			pw.setVectorAscension(sf[3]);
			pw.setLowerBound(sf[4]);
			pw.setUpperBound(sf[5]);
		}
		
		return pw;
	}
	@Override
	public String toString() {
		return port.toString();
	}
	
	
	
}


















