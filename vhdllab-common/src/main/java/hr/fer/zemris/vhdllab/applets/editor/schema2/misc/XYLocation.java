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


/**
 * Wrapper za koordinatu na shemi.
 * 
 * @author Axel
 *
 */
public final class XYLocation implements Comparable<XYLocation> {
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
	
	
	
	
	
	public final int getX() {
		return x;
	}
	
	public final void setX(int xloc) {
		x = xloc;
	}
	
	public final int getY() {
		return y;
	}
	
	public final void setY(int yloc) {
		y = yloc;
	}
	
	/**
	 * Da li je lokacija u bounding boxu odredenom
	 * sa specificiranim pravokutnikom.
	 * @param xpos
	 * @param ypos
	 * @param wdt
	 * @param hgt
	 */
	public final boolean in(int xpos, int ypos, int wdt, int hgt) {
		return (x >= xpos && x <= (xpos + wdt) && y >= ypos && y <= (ypos + hgt));
	}
	
	public final int manhattan(int x, int y) {
		return Math.abs(this.x - x) + Math.abs(this.y - y);
	}
	
	public final double euclid(int x, int y) {
		return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
	}
	
	public final int chebyshev(int x, int y) {
		int dx = Math.abs(this.x - x), dy = Math.abs(this.y - y);
		return (dx > dy) ? (dx) : (dy);
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
		return x << 16 + y;
	}

	@Override
	public final String toString() {
		return "(" + x + ", " + y + ")";
	}

	public int compareTo(XYLocation other) {
		if (this.x < other.x) return -1;
		if (this.x > other.x) return 1;
		if (this.y < other.y) return -1;
		if (this.y > other.y) return 1;
		return 0;
	}
	
	
	
	
}














