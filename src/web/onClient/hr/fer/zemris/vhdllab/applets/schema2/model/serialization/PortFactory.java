package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;




public class PortFactory {
	
	private static String toVecDir(String ascend) {
		if (ascend.equals(PortWrapper.ASCEND)) return DefaultType.VECTOR_DIRECTION_TO;
		else if (ascend.equals(PortWrapper.DESCEND)) return DefaultType.VECTOR_DIRECTION_DOWNTO;
		else throw new NotImplementedException("Ascension '" + ascend + "' unknown.");
	} 
	
	public static Port createPort(PortWrapper portwrap) {
		if (portwrap.getType().equals(PortWrapper.STD_LOGIC)) {
			Direction dir;
			Type tp = new DefaultType(PortWrapper.STD_LOGIC, DefaultType.SCALAR_RANGE,
					DefaultType.SCALAR_VECTOR_DIRECTION);
			if (portwrap.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = Direction.IN;
			else if (portwrap.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = Direction.OUT;
			else throw new NotImplementedException("Direction '" + portwrap.getDirection() + "' unknown.");
			
			return new DefaultPort(portwrap.getName(), dir, tp);
		} else if (portwrap.getType().equals(PortWrapper.STD_LOGIC_VECTOR)) {
			Direction dir;
			int[] range = new int[2];
			range[0] = Integer.parseInt(portwrap.getLowerBound());
			range[1] = Integer.parseInt(portwrap.getUpperBound());
			Type tp = new DefaultType(PortWrapper.STD_LOGIC_VECTOR, range, toVecDir(portwrap.getVectorAscension()));
			if (portwrap.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = Direction.IN;
			else if (portwrap.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = Direction.OUT;
			else throw new NotImplementedException("Direction '" + portwrap.getDirection() + "' unknown.");
			
			return new DefaultPort(portwrap.getName(), dir, tp);
		} else throw new NotImplementedException("Port type '" + portwrap.getType() + "' is unknown.");
	}
	
}
