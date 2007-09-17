package hr.fer.zemris.vhdllab.applets.schema2.interfaces;




/**
 * Bit ce obrisano.
 * 
 * @author brijest
 *
 */
public interface ISchemaProperties {
	
	/**
	 * Dohvaca vrijednost.
	 * 
	 * @param key
	 * Kljuc propertya.
	 * @return
	 * Null ako ne postoji kljuc
	 * za taj property.
	 */
	Object get(String key);

	/**
	 * Dohvaca vrijednost u
	 * Integer tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije Integer, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	Integer getAsInteger(String key) throws ClassCastException;
	
	/**
	 * Dohvaca vrijednost u
	 * String tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije String, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	String getAsString(String key) throws ClassCastException;
	
	/**
	 * Dohvaca vrijednost u
	 * Double tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije Double, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	Double getAsDouble(String key) throws ClassCastException;
	
	/**
	 * Dohvaca vrijednost u
	 * Boolean tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije Boolean, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	Boolean getAsBoolean(String key) throws ClassCastException;
	
	
	/**
	 * Postavlja property pod tim kljucem na
	 * neku vrijednost. Ako je vec nesto postojalo
	 * pod tim kljucem, bit ce pregazeno.
	 * @param key
	 * @param obj
	 */
	void set(String key, Object obj);
	
	
}




