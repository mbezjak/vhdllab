package hr.fer.zemris.vhdllab.applets.schema2.misc;

public class WireSegment {
	
	public XYLocation loc1;
	public XYLocation loc2;

	public WireSegment() {
		loc1 = new XYLocation();
		loc2 = new XYLocation();
	}
	
	public WireSegment(int x1, int y1, int x2, int y2) {
		loc1 = new XYLocation(x1, y1);
		loc2 = new XYLocation(x2, y2);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof WireSegment)) return false;
		WireSegment ws = (WireSegment)obj;
		return (ws.loc1.equals(loc1) && ws.loc2.equals(loc2));
	}

	@Override
	public int hashCode() {
		return 119 * loc1.hashCode() + loc2.hashCode();
	}
	
	

}
