package hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization;

import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;
import hr.fer.zemris.vhdllab.service.ci.PortType;




public class PortFactory {
	
	private static VectorDirection toVecDir(String ascend) {
		if (ascend.equals(PortWrapper.ASCEND)) return VectorDirection.TO;
		else if (ascend.equals(PortWrapper.DESCEND)) return VectorDirection.DOWNTO;
		else throw new NotImplementedException("Ascension '" + ascend + "' unknown.");
	} 
	
	public static Port createPort(PortWrapper portwrap) {
		if (portwrap.getType().equals(PortWrapper.STD_LOGIC)) {
			PortDirection dir;
			Type tp = new Type(PortType.valueOf(PortWrapper.STD_LOGIC.toUpperCase()), Range.SCALAR);
			if (portwrap.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = PortDirection.IN;
			else if (portwrap.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = PortDirection.OUT;
			else throw new NotImplementedException("Direction '" + portwrap.getDirection() + "' unknown.");
			
			return new Port(portwrap.getName(), dir, tp);
		} else if (portwrap.getType().equals(PortWrapper.STD_LOGIC_VECTOR)) {
			PortDirection dir;
			if (portwrap.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = PortDirection.IN;
			else if (portwrap.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = PortDirection.OUT;
			else throw new NotImplementedException("Direction '" + portwrap.getDirection() + "' unknown.");
			
			int[] range = new int[2];
			range[0] = Integer.parseInt(portwrap.getLowerBound());
			range[1] = Integer.parseInt(portwrap.getUpperBound());
			
			VectorDirection vecdir = toVecDir(portwrap.getVectorAscension());
			
			Type tp = new Type(PortType.valueOf(PortWrapper.STD_LOGIC_VECTOR.toUpperCase()), new Range(range[0], vecdir, range[1]));
			
			return new Port(portwrap.getName(), dir, tp);
		} else throw new NotImplementedException("Port type '" + portwrap.getType() + "' is unknown.");
	}
	
	@SuppressWarnings("unused")
	private static void swapTwoElemArr(int[] arr) {
		int t = arr[0];
		arr[0] = arr[1];
		arr[1] = t;
	}
	
}

















