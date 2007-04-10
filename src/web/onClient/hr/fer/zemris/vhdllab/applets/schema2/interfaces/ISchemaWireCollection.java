package hr.fer.zemris.vhdllab.applets.schema2.interfaces;



/**
 * Sucelje za skup zica koje se nalaze
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaWireCollection extends ISerializable {
	
	/**
	 * Dohvaca zicu zadanog imena.
	 * 
	 * @param wireName
	 * Jedinstveni String identifikator
	 * zice (signala).
	 * 
	 * @return
	 * Vraca zicu ciji je jedinstveni
	 * identifikator zadano ime, ili null
	 * ako takva ne postoji.
	 * Pritom ime nije dio IParameterCollection,
	 * tj. sama zica ne zna svoje ime.
	 * Ime je jedinstveni identifikator po kojem
	 * je moguce dohvacati zice.
	 * 
	 */
	ISchemaWire fetchWire(String wireName);
	
	
	/**
	 * Odreduje da li postoji zadano ime.
	 * 
	 * @param wireName
	 * Ime zice.
	 * 
	 * @return
	 * True ako zica zadanog imena
	 * postoji u kolekciji, false inace.
	 * 
	 */
	boolean containsName(String wireName);
	
	
	/**
	 * Dohvaca zicu na zadanim koordinatama.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 *  
	 * @return
	 * Vraca zicu ako takva postoji na zadanim
	 * koordinatama ili je udaljena za dist od njih.
	 * Ako ista ne postoji, vraca se null.
	 * 
	 */
	ISchemaWire fetchWire(int x, int y, int dist);
	
	
	/**
	 * Odreduje da li postoji zica
	 * na koordinatama.
	 * 
	 * @param x
	 * @param y
	 * @return
	 * Vraca zicu ako takva postoji na zadanim
	 * koordinatama ili je udaljena za dist od njih.
	 * Ako ista ne postoji, vraca se null.
	 * 
	 */
	boolean containsAt(int x, int y, int dist);
}













