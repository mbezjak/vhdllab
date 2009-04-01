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









