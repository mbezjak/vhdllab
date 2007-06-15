package hr.fer.zemris.vhdllab.applets.schema2.enums;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;



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
		
	};
	
	
	
	public abstract EOrientation opposite();
	public abstract String serialize();
	
	public static EOrientation parse(String ocode) {
		if (ocode.equals("SOUTH")) return EOrientation.SOUTH;
		if (ocode.equals("NORTH")) return EOrientation.NORTH;
		if (ocode.equals("EAST")) return EOrientation.EAST;
		if (ocode.equals("WEST")) return EOrientation.WEST;
		throw new NotImplementedException("Orientation '" + ocode + "' is unknown.");
	}
}












