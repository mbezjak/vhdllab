/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import java.util.List;




public class SMath {
	
	public static final int ERROR = Integer.MIN_VALUE;
	
	/**
	 * Vraca najblizi port od lokacije
	 * (x, y) u sustavu komponente
	 * ako je takav udaljen manje od
	 * dist. Ako je udaljen vise od dist
	 * ili ne postoji, vraca se ERROR.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 * @param ports
	 * Lista portova. Vraca se ERROR za null.
	 */
	public static int calcClosestPort(int x, int y, int dist, List<SchemaPort> ports) {
		if (ports == null) return ERROR;
		
		int index = ERROR, i = 0;
		double mindist = dist;
		double ndist = 0.f;
		
		for (SchemaPort port : ports) {
			ndist = Math.sqrt((x - port.getOffset().x) * (x - port.getOffset().x) 
					+ (y - port.getOffset().y) * (y - port.getOffset().y));
			if (ndist <= mindist) {
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
	 * ili ne postoji, vraca se ERROR.
	 * 
	 * @param dist
	 * @param ports
	 * Lista portova. Vraca se ERROR za null.
	 */
	public static int calcClosestPort(XYLocation location, int dist, List<SchemaPort> ports) {
		return calcClosestPort(location.x, location.y, dist, ports);
	}
	
	/**
	 * Vraca najblizi segment od lokacije
	 * location ako je takav udaljen manje od
	 * dist. Ako je udaljen vise ili jednako od
	 * dist ili ne postoji, vraca se ERROR.
	 * 
	 * @param location
	 * @param dist
	 * @param segments
	 * Ako je null, vratit ce se ERROR.
	 * @return
	 * Indeks segmenta zice ako postoji
	 * segment u blizini location, a
	 * ERROR inace.
	 */
	public static int calcClosestSegment(XYLocation location, int dist, List<WireSegment> segments) {
		if (segments == null) return ERROR;
		
		int index = ERROR, i = 0, mindist = dist;
		
		for (WireSegment ws : segments) {
			int ndist = ws.calcDist(location.x, location.y);
			if (ndist < mindist) {
				mindist = ndist;
				index = i;
			}
			i++;
		}
		
		return index;
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
	 */
	public static XYLocation toCompCoord(XYLocation location, XYLocation componentCoord) {
		return new XYLocation(location.x - componentCoord.x,
				location.y - componentCoord.y);
	}
	
	/**
	 * Vraca manji od dva broja.
	 * 
	 * @param a
	 * @param b
	 * @return
	 * Manji broj. Ako su jednaki, vraca b.
	 */
	public static int min(int a, int b) {
		if (a < b) return a;
		return b;
	}
	
	/**
	 * Vraca veci od dva broja.
	 * 
	 * @param a
	 * @param b
	 * @return
	 * Veci broj. Ako su jednaki, vraca b.
	 */
	public static int max(int a, int b) {
		if (a > b) return a;
		return b;
	}
	
	/**
	 * Vraca true ako je num u zatvorenom
	 * intervalu [a, b].
	 * 
	 */
	public static boolean within(int num, int a, int b) {
		return (num >= a && num <= b);
	}
	
	/**
	 * Vraca true ako je num u zatvorenom
	 * intervalu [a, b] ako je a <= b,
	 * ili u zatvorenom intervalu [b, a] ako
	 * je a > b.
	 * 
	 */
	public static boolean withinOrd(int num, int a, int b) {
		if (a > b) {
			int t = b;
			b = a;
			a = t;
		}
		return (num >= a && num <= b);
	}
	
	/**
	 * Vraca true ako je num u otvorenom
	 * intervalu (a, b).
	 * 
	 */
	public static boolean inside(int num, int a, int b) {
		return (num > a && num < b);
	}
	
	/**
	 * Vraca true ako je num u otvorenom
	 * intervalu (a, b) ako je a <= b,
	 * ili u otvorenom intervalu (b, a) ako
	 * je a > b.
	 * 
	 */
	public static boolean insideOrd(int num, int a, int b) {
		if (a > b) {
			int t = b;
			b = a;
			a = t;
		}
		return (num > a && num < b);
	}
}










