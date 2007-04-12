package hr.fer.zemris.vhdllab.applets.schema2.misc;

import org.apache.commons.digester.xmlrules.XmlLoadException;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;



public final class LocDimRotator {
	
	
	/**
	 * Vraca zarotiranu koordinatu, gdje
	 * se rotira u odnosu na (0, 0) i to
	 * na cetiri nacina - NORTH,
	 * SOUTH, EAST, WEST.
	 *  
	 * @param location
	 * Koordinata kakva bi bila kad bi
	 * orientation bio NORTH.
	 * @param orientation
	 * @return
	 * Ako je orijentacija NORTH,
	 * vraca istu vrijednost.
	 * Ako je SOUTH, WEST ili EAST,
	 * XYLocation se rotira na odgovarajucu
	 * stranu, tako da 0 ostaje 0.
	 */
	public final XYLocation rotateLocation(XYLocation location, Dimension2d dimension, EOrientation orientation) {
		return rotateLocation(location.x, location.y, dimension.width, dimension.height, orientation);
	}
	
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
	 * @return
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









