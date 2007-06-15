package hr.fer.zemris.vhdllab.applets.schema2.enums;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISerializable;


/**
 * Popis orijentacija komponente.
 * 
 * @author brijest
 *
 */
public enum EOrientation implements ISerializable {
	NORTH() {

		@Override
		public EOrientation opposite() {
			return SOUTH;
		}

		public boolean deserialize(String code) {
			// TODO Auto-generated method stub
			return false;
		}

		public String serialize() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	},
	SOUTH() {

		@Override
		public EOrientation opposite() {
			return NORTH;
		}

		public boolean deserialize(String code) {
			// TODO Auto-generated method stub
			return false;
		}

		public String serialize() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	},
	WEST() {

		@Override
		public EOrientation opposite() {
			return EAST;
		}

		public boolean deserialize(String code) {
			// TODO Auto-generated method stub
			return false;
		}

		public String serialize() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	},
	EAST() {

		@Override
		public EOrientation opposite() {
			return WEST;
		}

		public boolean deserialize(String code) {
			// TODO Auto-generated method stub
			return false;
		}

		public String serialize() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	};
	
	public abstract EOrientation opposite();
}












