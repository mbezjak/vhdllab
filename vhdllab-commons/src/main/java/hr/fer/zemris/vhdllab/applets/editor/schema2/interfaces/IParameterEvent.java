package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;

import java.util.List;









/**
 * Svaki IParameter moze sadrzavati ovakav objekt.
 * Nakon promjene vrijednosti parametra ovaj objekt radi
 * promjene na citavoj shemi.
 * BITNO: <b>NAKON promjene vrijednosti parametra.</b>
 * 
 * @author brijest
 *
 */
public interface IParameterEvent {
	
	/**
	 * Vraca deep copy ovog objekta.
	 * 
	 * @return
	 * Deep copy ovog objekta.
	 */
	IParameterEvent copyCtor();

	/**
	 * Lista promjena koje obavlja ovaj event.
	 * Lista koja se vraca ne smije biti mijenjana,
	 * a implementirana je kao staticki read-only
	 * objekt.
	 * 
	 * @return
	 */
	List<ChangeTuple> getChanges();
	
	/**
	 * Svaki IParameterEvent putem ove metode obavlja
	 * promjene na shemi. Promjene se obavljaju na info
	 * objektu u skladu s navedenim parametrom. Treci
	 * argument je ovisan o samom tipu eventa.
	 * 
	 * @param oldvalue
	 * Vrijednost parametra, prije nego sto je promijenjena.
	 * @param parameter
	 * @param info
	 * @param wire
	 * Ako je parametar dio zice, inace null.
	 * @param component
	 * Ako je parametar dio komponente, inace null.
	 * @return
	 * Ako je promjena uspjesna, vratit ce true. Ako nije,
	 * vratit ce false i PRITOM NECE napraviti nikakve promjene.
	 */
	boolean performChange(Object oldvalue, IParameter parameter, ISchemaInfo info,
			ISchemaWire wire, ISchemaComponent component);
	
	
	/**
	 * Odgovara da li su promjene koje izvrsi ovaj event
	 * funkcija ISKLJUCIVO vrijednosti parametra koji je specificiran.
	 * 
	 * @return
	 */
	boolean isUndoable();
}













