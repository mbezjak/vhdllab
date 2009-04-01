package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;

import java.util.Map;
import java.util.Set;




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
	ISchemaComponent clonePrototype(Caseless componentTypeName, Set<Caseless> takennames)
		throws UnknownComponentPrototypeException;
	
	
	/**
	 * Vraca mapu prototipova koji se nalaze
	 * u kolekciji.
	 * 
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
	
	
	/**
	 * Brise sve prototipove.
	 *
	 */
	void clearPrototypes();
	
	/**
	 * Vraca true ako postoji prototip tog imena.
	 * @param prototypename
	 */
	boolean containsPrototype(Caseless prototypename);
	
	/**
	 * Brise samo prototipove navedenog tipa.
	 * @param cmptype
	 */
	void clearPrototypes(EComponentType cmptype);
	
	/**
	 * Brise prototip navedenog imena ako takav postoji.
	 * @param prototypename
	 */
	boolean removePrototype(Caseless prototypename);
	
}









