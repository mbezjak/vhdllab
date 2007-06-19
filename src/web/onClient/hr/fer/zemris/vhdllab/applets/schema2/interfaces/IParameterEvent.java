package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;

import java.util.List;









/**
 * Svaki IParameter moze sadrzavati ovakav objekt.
 * Pri promjeni vrijednosti parametra ovaj objekt radi
 * promjene na citavoj shemi.
 * 
 * @author brijest
 *
 */
public interface IParameterEvent {

	/**
	 * Lista promjena koje obavlja ovaj event.
	 * Lista koja se vraca ne smije biti mijenjana,
	 * a implementirana je kao staticki read-only
	 * objekt.
	 * 
	 * @return
	 */
	List<EPropertyChange> getChanges();
	
	/**
	 * Svaki IParameterEvent putem ove metode obavlja
	 * promjene na shemi. Promjene se obavljaju na info
	 * objektu u skladu s navedenim parametrom. Treci
	 * argument je ovisan o samom tipu eventa.
	 * 
	 * @param parameter
	 * @param info
	 * @param wire
	 * Ako je parametar dio zice, inace null.
	 * @param component
	 * Ako je parametar dio komponente, inace null.
	 */
	void performChange(IParameter parameter, ISchemaInfo info, ISchemaWire wire, ISchemaComponent component);
	
	
	/**
	 * Odgovara da li su promjene koje izvrsi ovaj event
	 * funkcija ISKLJUCIVO vrijednosti parametra koji je specificiran.
	 * 
	 * @return
	 */
	boolean isUndoable();
}













