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
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;



public final class LocDimRotator {
	
	/**
	 * Vidi prethodnu metodu.
	 * 
	 * @param x
	 * Koordinata x kakva bi bila kad bi
	 * orientation bio NORTH.
	 * @param y
	 * Koordinata y kakva bi bila kad bi
	 * orientation bio NORTH.
	 * @param orientation
	 */
	public final XYLocation rotateLocation(int x, int y, int width, int height, EOrientation orientation) {
		switch (orientation) {
		case NORTH:
			return new XYLocation(x, y);
		case SOUTH:
			return new XYLocation(x, height - y);
		case EAST:
			return new XYLocation(y, width - x);
		case WEST:
			return new XYLocation(height - y, x);
		default:
			return new XYLocation(x, y);
		}
	}
	
}









