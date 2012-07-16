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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.misc;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;

import java.util.HashMap;



/**
 * Klasa koja opisuje prolaznost polja na mapi.
 * Polja su medusobno udaljena za Constants.GRID_SIZE.
 * width i height oznacuju ukupne dimenzije koje zauzimaju
 * sve komponente na schematicu.
 * Naposljetku, walkmap sadrzi prolaznosti. Ako za odredenu koordinatu (x,y) ne
 * sadrzi nista (null), to znaci da je navedena koordinata u potpunosti prolazna.
 * Ako za pojedinu koordinatu sadrzi Integer, tad treba napraviti bitovnu I operaciju
 * s odredenom simbolickom konstantom (FROM_NORTH, FROM_SOUTH, itd.) kako bi se
 * ustanovila prolaznost na koordinatu (x,y) iz odredenog smjera.
 * 
 * Podatkovni clanovi ucinjeni su javnima radi brzeg pristupa ovoj mapi,
 * no to ne znaci da po njima treba prckat. Ovaj se objekt stvara pomocu
 * npr. InspectWalkability query-a, a koristi se u npr. SmartConnect query-u.
 * 
 * @author Axel
 *
 */
public final class WalkabilityMap {
	/* static fields */
	public static final int FROM_NORTH = 1;
	public static final int FROM_SOUTH = 2;
	public static final int FROM_WEST = 4;
	public static final int FROM_EAST = 8;
	public static final int STEP = Constants.GRID_SIZE;
	

	/* private fields */
	public int width, height;
	public HashMap<XYLocation, Integer> walkmap;
	

	/* ctors */
	
	public WalkabilityMap() {
		walkmap = new HashMap<XYLocation, Integer>();
	}
	

	/* methods */
	
	public final boolean isCellWalkable(XYLocation cellLocation, int comingFrom) {
		Integer walkindex = walkmap.get(cellLocation);
		if (walkindex == null) return true;
		return (walkindex & comingFrom) != 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		XYLocation loc = new XYLocation();
		for (int j = 0; j < 500; j += Constants.GRID_SIZE) {
			for (int i = 0; i < 600; i += Constants.GRID_SIZE) {
				loc.x = i; loc.y = j;
				Integer walk = walkmap.get(loc);
				if (walk == null) walk = 15;
				if (walk != 15) {
					if (walk < 10) sb.append(' ');
					sb.append(walk).append(' ');
				} else sb.append("-- ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}














