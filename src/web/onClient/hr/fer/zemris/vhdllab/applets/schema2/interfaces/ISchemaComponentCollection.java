package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.awt.Rectangle;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;



/**
 * Sucelje za skup komponenti koje 
 * se nalaze u shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaComponentCollection extends Iterable<PlacedComponent> {
		
	/**
	 * Dohvaca komponentu zadanog imena.
	 * 
	 * @param componentName
	 * Jedinstveni String identifikator
	 * komponente.
	 * 
	 * @return
	 * Vraca komponentu ciji je jedinstveni
	 * identifikator zadano ime, ili null
	 * ako takva ne postoji.
	 * Pritom ime nije dio IParameterCollection,
	 * tj. sama komponenta ne zna svoje ime.
	 * Ime je jedinstveni identifikator po kojem
	 * je moguce dohvacati komponente.
	 * 
	 */
	ISchemaComponent fetchComponent(Caseless componentName);
	
	
	/**
	 * Odreduje da li postoji zadano ime.
	 * 
	 * @param componentName
	 * Ime komponente.
	 * 
	 * @return
	 * True ako komponenta zadanog imena
	 * postoji u kolekciji, false inace.
	 * 
	 */
	boolean containsName(Caseless componentName);
	
	/**
	 * Dohvaca komponentu danog imena i vraca njenu
	 * lokaciju.
	 * 
	 * @param componentName
	 * Caseless ime komponente.
	 * 
	 * @return
	 * Null ako takva komponenta ne postoji, a
	 * lokacija komponente inace.
	 */
	XYLocation getComponentLocation(Caseless componentName);
	
	
	/**
	 * Dohvaca pravokutnik koji obuhvaca komponentu
	 * danog imena.
	 * 
	 * @param componentName
	 * Caseless ime komponente.
	 * @return
	 * Null ako komponenta tog imena ne postoji,
	 * a minimalni pravokutnik koji obuhvaca
	 * komponentu inace.
	 */
	Rectangle getComponentBounds(Caseless componentName); 
	
	
	
	/**
	 * Dohvaca komponentu na zadanim koordinatama.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 *  
	 * @return
	 * Vraca komponentu ako takva postoji na (x, y)
	 * koordinati ili je udaljena najmanje za <code>dist</code>
	 * od koordinate (x, y).
	 * 
	 * Pritom, ako postoji komponenta na koordinati (x, y)
	 * bit ce vracena prva na koju se naide.
	 * Ako ne postoji komponenta na toj koordinati, onda ce
	 * biti vracena komponenta koja je najmanje udaljena
	 * od koordinate (x, y), ali manje udaljena od <code>dist</code>.
	 * Inace se vraca null.
	 * 
	 * Komponenta postoji na nekoj koordinati (x, y)
	 * ako bounding box komponente obuhvaca koordinatu (x, y).
	 * 
	 */
	ISchemaComponent fetchComponent(int x, int y, int dist);
	
	/**
	 * Dohvaca sve komponente zadanog tipa.
	 * 
	 * @param componentType
	 * @return
	 */
	Set<ISchemaComponent> fetchComponents(EComponentType componentType);
	
	/**
	 * Odreduje da li postoji komponenta
	 * na koordinatama (x, y), s tolerancijom
	 * <code>dist</code>.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 * 
	 * @return
	 * Vraca true ako postoji, false inace.
	 * Komponenta postoji na koordinati ako
	 * bounding box komponente, uvecan za
	 * toleranciju <code>dist</code> obuhvaca
	 * koordinatu.
	 * 
	 */
	boolean containsAt(int x, int y, int dist);
	
	
	/**
	 * Dodaje komponentu na shemu,
	 * na zadane koordinate.
	 * 
	 * @param x
	 * @param y
	 * @param component
	 * 
	 * @throws DuplicateKeyException
	 * Ako postoji komponenta tog imena.
	 * @throws OverlapException
	 * Ako dolazi do preklapanja.
	 * 
	 */
	void addComponent(int x, int y, ISchemaComponent component) throws DuplicateKeyException, OverlapException;
	
	/**
	 * Zadanu komponentu mice sa sheme.
	 * 
	 * @param name
	 * Jedinstveni identifikator komponente
	 * koja ce biti removeana sa sheme.
	 * @throws UnknownKeyException
	 * Ako ne postoji komponenta tog imena.
	 * 
	 */
	void removeComponent(Caseless name) throws UnknownKeyException;
	
	
	/**
	 * Vraca skup imena komponenata na shemi,
	 * preko kojeg je moguce iterirati po
	 * komponentama.
	 * 
	 * @return
	 */
	Set<Caseless> getComponentNames();
	
	/**
	 * Brise sve komponente iz kolekcije.
	 *
	 */
	void clear();
	
}



















