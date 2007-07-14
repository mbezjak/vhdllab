package hr.fer.zemris.vhdllab.applets.schema2.misc;


/**
 * Klasa za opis vertikalnog ili horizontalnog segmenta zice.
 * 
 * @author Axel
 *
 */
public final class WireSegment {
	
	private XYLocation loc1;
	private XYLocation loc2;
	
	
	public WireSegment() {
		loc1 = new XYLocation();
		loc2 = new XYLocation();
	}
	
	public WireSegment(int x1, int y1, int x2, int y2) {
		loc1 = new XYLocation(x1, y1);
		loc2 = new XYLocation(x2, y2);
		check();
	}
	
	
	
	
	/**
	 * Vraca sjeciste s drugim zicnim segmentom ako ono postoji.
	 * Pritom NE RAZMATRA sjecista koja nastaju na rubovima oba
	 * zicna segmenta. Npr.:
	 * 
	 *  1)
	 * ----*
	 *     |
	 *     | 2)
	 *     |
	 *     
	 * Zvjezdica se nece smatrati sjecistem.
	 * U sljedecem primjeru:
	 * 
	 *   1)
	 * -------*------
	 *        |
	 *        | 2)
	 *        |
	 *        
	 * Zvjezdica se smatra sjecistem.
	 * 
	 * U slucaju da su oba segmenta vertikalna ili horizontalna, i pritom
	 * se preklapaju, sjecistem ce se smatrati pocetna ili zavrsna tocka
	 * jednog od segmenata, pri cemu prednost ima pocetna tocka.
	 * 
	 * @param other
	 * @return
	 * Vraca null ako nema sjecista.
	 * Povratna vrijednost je kopija.
	 */
	public XYLocation intersection(WireSegment other) {
		if (this.isVertical()) {
			if (other.isVertical()) {
				if (this.loc1.x == other.loc1.x) {
					if (SMath.insideOrd(this.loc1.y, other.loc1.y, other.loc2.y))
						return new XYLocation(this.loc1);
					if (SMath.insideOrd(this.loc2.y, other.loc1.y, other.loc2.y))
						return new XYLocation(this.loc2);
				}
			} else {
				if (SMath.insideOrd(other.loc1.y, this.loc1.y, this.loc2.y)) {
					if (SMath.withinOrd(this.loc1.x, other.loc1.x, other.loc2.x))
						return new XYLocation(this.loc1.x, other.loc1.y);
				}
				if (SMath.insideOrd(this.loc1.x, other.loc1.x, other.loc2.x)) {
					if (other.loc1.y == this.loc1.y) return new XYLocation(this.loc1);
					if (other.loc1.y == this.loc2.y) return new XYLocation(this.loc2);
				}
			}
		} else {
			if (!other.isVertical()) {
				if (this.loc1.y == other.loc1.y) {
					if (SMath.insideOrd(this.loc1.x, other.loc1.x, other.loc2.x))
						return new XYLocation(this.loc1);
					if (SMath.insideOrd(this.loc2.x, other.loc1.x, other.loc2.x))
						return new XYLocation(this.loc2);
				}
			} else {
				if (SMath.insideOrd(other.loc1.x, this.loc1.x, this.loc2.x)) {
					if (SMath.withinOrd(this.loc1.y, other.loc1.y, other.loc2.y))
						return new XYLocation(other.loc1.x, this.loc1.y);
				}
				if (SMath.insideOrd(this.loc1.y, other.loc1.y, other.loc2.y)) {
					if (other.loc1.x == this.loc1.x) return new XYLocation(this.loc1);
					if (other.loc1.x == this.loc2.x) return new XYLocation(this.loc2);
				}
			}
		}
		return null;
	}
	
	/**
	 * Odreduje tocku dodira ovog segmenta i segmenta <code>other</code>,
	 * pri cemu je tocka dodira ona tocka koja je na rubu jednog i na rubu
	 * drugog segmenta istovremeno (rubovi se dodiruju). Npr.:
	 * 
	 *  1)
	 * ----*
	 *     |
	 *     | 2)
	 *     |
	 * 
	 * Zvjezdica je tocka dodira.
	 * 
	 * @param other
	 * @return
	 * Vraca null ako se ovaj i drugi segment ne diraju na rubovima.
	 * Povratna vrijednost je kopija.
	 */
	public XYLocation edgepoint(WireSegment other) {
		if (other.loc1.equals(this.loc1) || other.loc1.equals(this.loc2))
			return new XYLocation(other.loc1);
		if (other.loc2.equals(this.loc1) || other.loc2.equals(this.loc2))
			return new XYLocation(other.loc2);
		return null;
	}
	
	public void setEnd(XYLocation loc2) {
		this.loc2 = loc2;
		check();
	}

	public XYLocation getEnd() {
		return loc2;
	}

	public void setStart(XYLocation loc1) {
		this.loc1 = loc1;
		check();
	}
	
	public XYLocation getStart() {
		return loc1;
	}
	
	private final void check() {
		if (loc1.x != loc2.x && loc1.y != loc2.y) {
			System.out.println("Segment not ortogonal: " + loc1.toString() + " ... " + loc2.toString());
			throw new IllegalArgumentException("WireSegment can only be vertical or horizontal.");
		}
	}
	
	/**
	 * Odreduje da li je zica okomita.
	 * 
	 * @return
	 */
	public final boolean isVertical() {
		return (loc1.x == loc2.x);
	}
	
	public final void setX1(int x1) {
		loc1.x = x1;
	}
	
	public final void setY1(int y1) {
		loc1.y = y1;
	}
	
	public final void setX2(int x2) {
		loc2.x = x2;
	}
	
	public final void setY2(int y2) {
		loc2.y = y2;
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
	
	public final int length() {
		return (isVertical()) ? (Math.abs(loc2.y - loc1.y)) : (Math.abs(loc2.x - loc1.x));
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

	@Override
	public String toString() {
		return "WireSegment[(" + loc1.x + ", " + loc1.y + "), (" + loc2.x + ", " + loc2.y + ")";
	}

	




	
	
	

}











