package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.util.Map;




/**
 * Kolekcija koja skladisti prototipove
 * komponenti.
 * Sluzi za kreiranje novih instanci
 * komponenti na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaPrototypeCollection {
	
	/**
	 * Obavlja kloniranje jednog od prototipova.
	 * Postoji po jedan prototip u kolekciji
	 * za svaki tip komponente.
	 * 
	 * @param componentTypeName
     * Ime tipa komponente.
     * @param instanceName
     * Ime nove instance.
	 * 
	 * @return
	 * Deep copy prototipa.
	 * 
	 */
	ISchemaComponent clonePrototype(Caseless componentTypeName, Caseless instanceName)
	throws UnknownComponentPrototypeException;
	
	
	/**
	 * Vraca mapu prototipova koji se nalaze
	 * u kolekciji.
	 * 
	 * @return
	 */
	Map<Caseless, ISchemaComponent> getPrototypes();
	
	
	/**
	 * Dodaje novi prototip.
	 * 
	 * @param componentPrototype
	 * Komponenta koja ce postati
	 * prototip. NE smije se proslijediti
	 * komponenta koja se nakon toga koristi
	 * u shemi, jer ce nove kopije biti
	 * kopije trenutnog stanja komponente koja
	 * je trenutno na shemi.
	 * @throws DuplicateKeyException
	 * Baca ovaj exception ako se ustanovi
	 * da prototip tog istog tipa vec postoji.
	 */
	void addPrototype(ISchemaComponent componentPrototype) throws DuplicateKeyException;
}









