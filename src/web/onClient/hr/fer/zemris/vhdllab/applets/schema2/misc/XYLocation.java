package hr.fer.zemris.vhdllab.applets.schema2.misc;


/**
 * Wrapper za koordinatu na shemi.
 * 
 * @author Axel
 *
 */
public class XYLocation {
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof XYLocation)) return false;
		XYLocation loc = (XYLocation)obj;
		if (loc.x == x && loc.y == y) return true;
		return false;
	}

	@Override
	public int hashCode() {
		return 37 * x + y;
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ')';
	}
	
	
}














