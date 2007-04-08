package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.util.List;



/**
 * Ovaj interface propisuje nacin
 * funkcioniranja same logike schematica.
 * Njegove implementacije sadrze informacije
 * o stanju sheme, komponentama koje su
 * na shemi, zicama, itd.
 * 
 * @author Axel
 *
 */
public interface ISchemaCore {
	
	/**
	 * Prihvaca i obavlja proslijedenu komandu.
	 * 
	 * @param command
	 * Komanda.
	 * 
	 * @return
	 * Vraca objekt koji govori
	 * o uspjesnosti izvodenja komande i
	 * eventualnim povratnim vrijednostima. 
	 * 
	 */
	ICommandResponse receiveCommand(ICommand command);
	
	/**
	 * Tipicno proslijeduje poziv
	 * odgovorajucoj metodi ICommandExecutora.
	 * 
	 * @see ICommandExecutor
	 */
	boolean canUndoCommand();
	
	/**
	 * Tipicno proslijeduje poziv
	 * odgovorajucoj metodi ICommandExecutora.
	 * 
	 * @see ICommandExecutor
	 */
	boolean canRedoCommand();
	
	/**
	 * Tipicno proslijeduje poziv
	 * odgovorajucoj metodi ICommandExecutora.
	 * 
	 * @see ICommandExecutor
	 */
	List<String> getUndoCommandList();
	
	/**
	 * Tipicno proslijeduje poziv
	 * odgovorajucoj metodi ICommandExecutora.
	 * 
	 * @see ICommandExecutor
	 */
	List<String> getRedoCommandList();
	
	/**
	 * Tipicno proslijeduje poziv
	 * odgovorajucoj metodi ICommandExecutora.
	 * 
	 * @see ICommandExecutor
	 */
	ICommandResponse undoCommand();

	/**
	 * Tipicno proslijeduje poziv
	 * odgovorajucoj metodi ICommandExecutora.
	 * 
	 * @see ICommandExecutor
	 */
	ICommandResponse redoCommand();
}












