package hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;




public class PortFactory {
	
	public static Port createPort(PortWrapper portwrap) {
		if (portwrap.getType().equals(PortWrapper.STD_LOGIC)) {
			PortDirection dir;
			if (portwrap.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = PortDirection.IN;
			else if (portwrap.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = PortDirection.OUT;
			else throw new NotImplementedException("Direction '" + portwrap.getDirection() + "' unknown.");
			
			return new Port(portwrap.getName(), dir);
		} else if (portwrap.getType().equals(PortWrapper.STD_LOGIC_VECTOR)) {
			PortDirection dir;
			if (portwrap.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = PortDirection.IN;
			else if (portwrap.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = PortDirection.OUT;
			else throw new NotImplementedException("Direction '" + portwrap.getDirection() + "' unknown.");
			
			int[] range = new int[2];
			range[0] = Integer.parseInt(portwrap.getLowerBound());
			range[1] = Integer.parseInt(portwrap.getUpperBound());
			
			return new Port(portwrap.getName(), dir, range[0], range[1]);
		} else throw new NotImplementedException("Port type '" + portwrap.getType() + "' is unknown.");
	}
	
	@SuppressWarnings("unused")
	private static void swapTwoElemArr(int[] arr) {
		int t = arr[0];
		arr[0] = arr[1];
		arr[1] = t;
	}
	
}

















