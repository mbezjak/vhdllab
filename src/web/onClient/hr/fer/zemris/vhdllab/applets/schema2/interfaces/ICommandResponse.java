package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.misc.InfoMap;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;



/**
 * Interface koji propisuje odgovor ISchemaCore-a
 * na zahtjev.
 * 
 * @author Axel
 *
 */
public interface ICommandResponse {
	
	/**
	 * Odgovara na pitanje da li je zahtjev (komanda)
	 * izveden uspjesno.
	 * 
	 * @return
	 * True ako je komanda uspjesna,
	 * false ako nije.
	 */
	boolean isCommandSuccessful();
	
	/**
	 * Vraca poruku greske.
	 * 
	 * @return
	 * Ako isCommandSuccessful() vraca true,
	 * onda ova metoda vraca poruku koja je
	 * daje objasnjenje
	 * U protivnom ova metoda moze vratiti
	 * bilo sto (null ili neku vrijednost).
	 * 
	 */
	SchemaError getError();
	
	/**
	 * Vraca kolekciju
	 * string-objekt zapisa koji sadrze
	 * eventualne informacije koje se
	 * trebaju vratiti posiljatelju
	 * ICommand objekta.
	 * 
	 * @return
	 */
	InfoMap getInfoMap();
}











