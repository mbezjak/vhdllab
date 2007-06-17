package hr.fer.zemris.vhdllab.applets.schema2.misc;

import java.util.Set;




/**
 * Trazi prvo slobodno ime na
 * temelju ponudenog i na temelju
 * popisa zauzetih imena.
 * 
 * @author brijest
 *
 */
public class AutoRenamer {

	
	public static Caseless getFreeName(Caseless offered, Set<Caseless> taken) {
		int i = 1;
		
		while (taken.contains(offered)) {
			offered = new Caseless(offered.toString() + ((i < 100) ? ("0" + i) : (i)));
			i++;
		}
		
		return offered;
	}
	
	
}




