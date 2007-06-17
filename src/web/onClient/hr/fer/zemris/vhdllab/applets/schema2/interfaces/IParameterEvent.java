package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;

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
	 * @param arg
	 * Ovisan o tipu eventa - to moze biti ime sklopa
	 * na kojem se promijenila vrijednost parametra,
	 * ili ime zice.
	 */
	void performChange(IParameter parameter, ISchemaInfo info, String arg);
	
}













