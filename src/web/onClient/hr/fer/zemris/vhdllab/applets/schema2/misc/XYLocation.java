package hr.fer.zemris.vhdllab.applets.schema2.misc;


/**
 * Wrapper za koordinatu na shemi.
 * 
 * @author Axel
 *
 */
public final class XYLocation {
	public int x;
	public int y;
	
	public XYLocation() {
		x = 0;
		y = 0;
	}
	
	public XYLocation(int xLoc, int yLoc) {
		x = xLoc;
		y = yLoc;
	}
	
	public XYLocation(XYLocation location) {
		x = location.x;
		y = location.y;
	}
	
	/**
	 * Da li je komponenta u bounding boxu odredenom
	 * sa specificiranim pravokutnikom.
	 * @param xpos
	 * @param ypos
	 * @param wdt
	 * @param hgt
	 * @return
	 */
	public final boolean in(int xpos, int ypos, int wdt, int hgt) {
		return (x >= xpos && x <= (xpos + wdt) && y >= ypos && y <= (ypos + hgt));
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null || !(obj instanceof XYLocation)) return false;
		XYLocation loc = (XYLocation)obj;
		if (loc.x == x && loc.y == y) return true;
		return false;
	}

	@Override
	public final int hashCode() {
		return 37 * x + y;
	}

	@Override
	public final String toString() {
		return '(' + x + ", " + y + ')';
	}
	
	
}














