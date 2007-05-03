package hr.fer.zemris.vhdllab.applets.schema2.misc;

import java.util.List;




public class SMath {
	
	/**
	 * Vraca najblizi port od lokacije
	 * (x, y) u sustavu komponente
	 * ako je takav udaljen manje od
	 * dist. Ako je udaljen vise od dist
	 * ili ne postoji, vraca se -1.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 * @param ports
	 * Lista portova. Vraca se -1 za null.
	 * @return
	 */
	public static int calcClosestPort(int x, int y, int dist, List<SchemaPort> ports) {
		if (ports == null) return -1;
		
		int index = -1, i = 0;
		double mindist = dist;
		double ndist = 0.f;
		
		for (SchemaPort port : ports) {
			ndist = Math.sqrt((x - port.getOffset().x) * (x - port.getOffset().x) 
					+ (y - port.getOffset().y) * (y - port.getOffset().y));
			if (ndist < mindist) {
				mindist = ndist;
				index = i;
			}
			i++;
		}
		
		return index;
	}
	
	/**
	 * Vraca najblizi port od lokacije
	 * location u sustavu komponente
	 * ako je takav udaljen manje od
	 * dist. Ako je udaljen vise od dist
	 * ili ne postoji, vraca se -1.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 * @param ports
	 * Lista portova. Vraca se -1 za null.
	 * @return
	 */
	public static int calcClosestPort(XYLocation location, int dist, List<SchemaPort> ports) {
		return calcClosestPort(location.x, location.y, dist, ports);
	}
	
	/**
	 * Pretvara zadane (x, y) koordinate u sustavu
	 * sheme u koordinate u sustavu komponente cija
	 * je gornja lijeva koordinata odredena s
	 * componentCoord.
	 * 
	 * @param x
	 * @param y
	 * @param componentCoord
	 * @return
	 */
	public static XYLocation toCompCoord(int x, int y, XYLocation componentCoord) {
		return new XYLocation(x - componentCoord.x, y - componentCoord.y);
	}
	
	/**
	 * Pretvara zadane s location koordinate u sustavu
	 * sheme u koordinate u sustavu komponente cija
	 * je gornja lijeva koordinata odredena s
	 * componentCoord.
	 * 
	 * @param location
	 * @param componentCoord
	 * @return
	 */
	public static XYLocation toCompCoord(XYLocation location, XYLocation componentCoord) {
		return new XYLocation(location.x - componentCoord.x,
				location.y - componentCoord.y);
	}
	
}




