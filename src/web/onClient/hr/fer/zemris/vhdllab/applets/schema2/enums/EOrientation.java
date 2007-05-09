package hr.fer.zemris.vhdllab.applets.schema2.enums;


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
		
	},
	SOUTH() {

		@Override
		public EOrientation opposite() {
			return NORTH;
		}
		
	},
	WEST() {

		@Override
		public EOrientation opposite() {
			return EAST;
		}
		
	},
	EAST() {

		@Override
		public EOrientation opposite() {
			return WEST;
		}
		
	};
	
	public abstract EOrientation opposite();
}
