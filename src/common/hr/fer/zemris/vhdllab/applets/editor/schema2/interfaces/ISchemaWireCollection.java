package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;

import java.util.Set;



/**
 * Sucelje za skup zica koje se nalaze
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaWireCollection extends Iterable<ISchemaWire> {
	
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
	 * Vraca prvu zicu ako takva postoji na zadanim
	 * koordinatama ili je udaljena za dist od njih.
	 * Ako ista ne postoji, vraca se null.
	 * 
	 */
	ISchemaWire fetchWire(int x, int y, int dist);
	
	
	/**
	 * Vraca skup svih zica na danoj koordinati.
	 * @param x
	 * @param y
	 * @return
	 * Null ako nema zica na navedenoj koordinati.
	 */
	Set<ISchemaWire> fetchAllWires(int x, int y);
	
	
	
	/**
	 * Za zicu danog imena vraca pravokutnik
	 * koji je obuhvaca.
	 * 
	 * @param wireName
	 * @return
	 * Minimalni pravokutnik koji obuhvaca
	 * zicu, a null zica tog imena ne postoji.
	 */
	Rect2d getBounds(Caseless wireName);
	
	
	
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
	 * @throws OverlapException
	 * Ako dolazi do preklapanja s postojecim zicama.
	 */
	void addWire(ISchemaWire wire) throws DuplicateKeyException, OverlapException;
	
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
	
	/**
	 * Vraca skup imena zica na shemi,
	 * preko kojeg je moguce iterirati
	 * po zicama.
	 * 
	 * @return
	 */
	Set<Caseless> getWireNames();
	
	
	/**
	 * Brise sve zice iz kolekcije.
	 *
	 */
	void clear();
	
	
}













