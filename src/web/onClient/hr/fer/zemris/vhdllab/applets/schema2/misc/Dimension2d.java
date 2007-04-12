package hr.fer.zemris.vhdllab.applets.schema2.misc;


/**
 * Wrapper za dimenziju na shemi.
 * 
 * @author Axel
 *
 */
public final class Dimension2d {
	public int width;
	public int height;
	
	public Dimension2d() {
		width = 0;
		height = 0;
	}
	
	public Dimension2d(int wdt, int hgt) {
		width = wdt;
		height = hgt;
	}
	
	public Dimension2d(Dimension2d location) {
		width = location.width;
		height = location.height;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Dimension2d)) return false;
		Dimension2d loc = (Dimension2d)obj;
		if (loc.width == width && loc.height == height) return true;
		return false;
	}

	@Override
	public final int hashCode() {
		return 37 * width + height;
	}

	@Override
	public final String toString() {
		return '(' + width + ", " + height + ')';
	}
	
	
}














