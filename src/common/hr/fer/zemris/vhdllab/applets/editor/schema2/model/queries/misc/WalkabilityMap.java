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
	public static final int FROM_EAST = 4;
	public static final int FROM_WEST = 8;
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
	
	

}














