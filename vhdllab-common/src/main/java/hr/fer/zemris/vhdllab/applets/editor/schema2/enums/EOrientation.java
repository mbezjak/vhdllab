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
package hr.fer.zemris.vhdllab.applets.editor.schema2.enums;




/**
 * Popis orijentacija komponente.
 * 
 * @author brijest
 *
 */
public enum EOrientation {
	
	NORTH() {

		@Override
		public EOrientation opposite() {
			return SOUTH;
		}

		@Override
		public String serialize() {
			return "NORTH";
		}

		@Override
		public EOrientation clockwise90() {
			return EOrientation.EAST;
		}

		@Override
		public EOrientation counterclockwise90() {
			return EOrientation.WEST;
		}
		
	},
	SOUTH() {

		@Override
		public EOrientation opposite() {
			return NORTH;
		}

		@Override
		public String serialize() {
			return "SOUTH";
		}

		@Override
		public EOrientation clockwise90() {
			return EOrientation.WEST;
		}

		@Override
		public EOrientation counterclockwise90() {
			return EOrientation.EAST;
		}
		
	},
	WEST() {

		@Override
		public EOrientation opposite() {
			return EAST;
		}

		@Override
		public String serialize() {
			return "WEST";
		}

		@Override
		public EOrientation clockwise90() {
			return EOrientation.NORTH;
		}

		@Override
		public EOrientation counterclockwise90() {
			return EOrientation.SOUTH;
		}

	},
	EAST() {

		@Override
		public EOrientation opposite() {
			return WEST;
		}

		@Override
		public String serialize() {
			return "EAST";
		}

		@Override
		public EOrientation clockwise90() {
			return EOrientation.SOUTH;
		}

		@Override
		public EOrientation counterclockwise90() {
			return EOrientation.NORTH;
		}
		
	};
	
	
	
	public abstract EOrientation opposite();
	public abstract String serialize();
	public abstract EOrientation counterclockwise90();
	public abstract EOrientation clockwise90();
	
	public final EOrientation counterclockwise90(int times) {
		EOrientation curr = this;
		
		if (times > 0) {
			times %= 4;
			
			for (int i = 0; i < times; i++) {
				curr = curr.counterclockwise90();
			}
		} else {
			times = -times;
			times %= 4;
			
			for (int i = times; i > 0; i--) {
				curr = curr.clockwise90();
			}
		}
		
		return curr;
	}
	
	public final EOrientation clockwise90(int times) {
		return counterclockwise90(-times);
	}
	
	public static EOrientation parse(String ocode) {
		if (ocode.equals("SOUTH")) return EOrientation.SOUTH;
		if (ocode.equals("NORTH")) return EOrientation.NORTH;
		if (ocode.equals("EAST")) return EOrientation.EAST;
		if (ocode.equals("WEST")) return EOrientation.WEST;
		throw new IllegalStateException("Orientation '" + ocode + "' is unknown.");
	}
	
	
	
	
}












