package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.util.List;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownComponentPrototypeException;




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
	 * 
	 * @return
	 * Deep copy prototipa.
	 * 
	 */
	ISchemaComponent clonePrototype(String componentTypeName) throws UnknownComponentPrototypeException;
	
	
	/**
	 * Vraca listu imena prototipova koji se nalaze
	 * u kolekciji.
	 * 
	 * @return
	 */
	List<String> getPrototypeNames();
	
	
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
	 * 
	 */
	void addPrototype(ISchemaComponent componentPrototype);
}









