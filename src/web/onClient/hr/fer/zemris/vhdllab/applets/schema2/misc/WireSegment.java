package hr.fer.zemris.vhdllab.applets.schema2.misc;

public final class WireSegment {
	
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
	
	/**
	 * Odreduje da li je zica okomita.
	 * 
	 * @return
	 */
	public final boolean isVertical() {
		return (loc1.x == loc2.x);
	}
	
	/**
	 * Vraca udaljenost segmenta
	 * od neke tocke.
	 * 
	 * @param xlkp
	 * @param ylkp
	 * @return
	 */
	public final int calcDist(int xlkp, int ylkp) {
		if (this.isVertical()) {
			int ly = (this.loc1.y > this.loc2.y) ? (this.loc2.y) : (this.loc1.y);
			int hy = (ly == this.loc1.y) ? (this.loc2.y) : (this.loc1.y);
			
			if (ylkp >= ly && ylkp <= hy) {
				return Math.abs(xlkp - this.loc1.x);
			} else if (ylkp > hy) {
				return getlen(xlkp - this.loc1.x, ylkp - hy);
			} else {
				return getlen(xlkp - this.loc1.x, ylkp - ly);
			}
		} else {
			int lx = (this.loc1.x > this.loc2.x) ? (this.loc2.x) : (this.loc1.x);
			int rx = (lx == this.loc1.x) ? (this.loc2.x) : (this.loc1.x);
			
			if (xlkp >= lx && xlkp <= rx) {
				return Math.abs(ylkp - this.loc1.y);
			} else if (xlkp > rx) {
				return getlen(xlkp - rx, ylkp - this.loc1.y);
			} else {
				return getlen(xlkp - lx, ylkp - this.loc1.y);
			}
		}
	}
	
	private final int getlen(int dx, int dy) {
		return (int)Math.round(Math.sqrt(dx * dx + dy * dy));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof WireSegment)) return false;
		WireSegment ws = (WireSegment)obj;
		return (ws.loc1.equals(loc1) && ws.loc2.equals(loc2));
	}

	@Override
	public int hashCode() {
		return loc1.hashCode() << 16 + loc2.hashCode();
	}
	
	

}
