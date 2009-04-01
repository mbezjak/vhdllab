package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.InfoMap;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;

import java.util.List;



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
	boolean isSuccessful();
	
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
	 */
	InfoMap getInfoMap();
	
	/**
	 * Vraca listu svojstava koja su promijenjena
	 * ovom komandom. Na temelju te liste ISchemaCore
	 * aktivira odgovarajuce registrirane listenere.
	 * 
	 * @return
	 * Praznu listu ili null ako nista nije promijenjeno.
	 */
	List<ChangeTuple> getPropertyChanges();
}











