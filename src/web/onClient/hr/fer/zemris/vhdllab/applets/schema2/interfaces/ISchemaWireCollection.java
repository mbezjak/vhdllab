package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;



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
	ISchemaWire fetchWire(Caseless wireName);
	
	
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
	boolean containsName(Caseless wireName);
	
	
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
	
	
	/**
	 * Dodaje zicu u kolekciju.
	 * 
	 * @param wire
	 * Zica jedinstvenog imena.
	 * @return
	 * @throws DuplicateKeyException
	 * Ako postoji zica tog imena u kolekciji.
	 */
	void addWire(ISchemaWire wire) throws DuplicateKeyException;
	
	/**
	 * Mice zicu iz kolekcije.
	 * 
	 * @param wireName
	 * Ime zice.
	 * @throws UnknownKeyException
	 * Ako zica (signal) tog imena ne postoji
	 * u kolekciji.
	 */
	void removeWire(Caseless wireName) throws UnknownKeyException;
}













