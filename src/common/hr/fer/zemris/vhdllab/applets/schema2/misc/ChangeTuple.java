package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;



/**
 * Enkapsulira podatak o jednoj
 * promjeni.
 * 
 * @author Axel
 *
 */
public final class ChangeTuple {
	public EPropertyChange changetype;
	public Object oldval;
	public Object newval;
	
	/**
	 * Defaultni konstruktor
	 * postavlja tip promjene
	 * na ANY_CHANGE, a ostalo na
	 * null. 
	 *
	 */
	public ChangeTuple() {
		changetype = EPropertyChange.ANY_CHANGE;
		oldval = null;
		newval = null;
	}
	
	/**
	 * Postavlja oldval i newval na null,
	 * a changetype na navedenu vrijednost.
	 * 
	 * @param change
	 */
	public ChangeTuple(EPropertyChange change) {
		changetype = change;
		oldval = null;
		newval = null;
	}
	
	/**
	 * Postavlja sve vrijednosti kako je
	 * navedeno.
	 * 
	 * @param change
	 * @param oldValue
	 * @param newValue
	 */
	public ChangeTuple(EPropertyChange change, Object oldValue, Object newValue) {
		changetype = change;
		oldval = oldValue;
		newval = newValue;
	}
	
}
