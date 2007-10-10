package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IGenericValue;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;







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
		int [] range = DefaultType.SCALAR_RANGE;
		String dir = DefaultType.SCALAR_VECTOR_DIRECTION;
		if (thtp.isVector()) {
			range = new int[]{thtp.getRangeFrom(), thtp.getRangeTo()};
			dir = thtp.getVectorDirection();
		}
		DefaultType tp = new DefaultType(thtp.getTypeName(), range, dir);
		DefaultPort dp = new DefaultPort(port.getName(), port.getDirection(), tp);
		
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
		if (pt.isScalar()) {
			sb.append(DELIMITER).append(DELIMITER);
		} else {
			sb.append(fromVecDir(pt.getVectorDirection())).append(DELIMITER);
			sb.append(Integer.toString(pt.getRangeFrom())).append(DELIMITER);
			sb.append(Integer.toString(pt.getRangeTo()));
		}
		
		return sb.toString();
	}
	
	private String fromVecDir(String vectorDirection) {
		if (vectorDirection.equals(DefaultType.VECTOR_DIRECTION_TO)) return PortWrapper.ASCEND;
		else if (vectorDirection.equals(DefaultType.VECTOR_DIRECTION_DOWNTO)) return PortWrapper.DESCEND;
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


















