/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

















