package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.util.List;



/**
 * Interface za objekt koji je zaduzen
 * za obavljanje komandi, i pamcenje
 * prethodno izvedenih komandi.
 * Tipicno se nalazi unutar ISchemaCore
 * objekta.
 * 
 * @author Axel
 *
 */
public interface ICommandExecutor {
	
	/**
	 * Obavlja navedenu komandu.
	 * Ako je ona uspjesno izvedena, i ako command.isUndoable()
	 * vraca true, stavlja je na listu komandi, a lista
	 * redo komandi se brise.
	 * Ako command.isUndoable() vraca false, lista prethodno
	 * izvedenih komandi se brise.
	 * 
	 * @return
	 * Vraca objekt koji vraca sama komanda pri izvodenju.
	 * 
	 */
	ICommandResponse executeCommand(ICommand command);
	
	
	/**
	 * Za odredivanje da li je moguc undo.
	 * 
	 * @return
	 * Ako lista prethodno izvedenih komandi nije
	 * prazna, vraca true, u protivnom false.
	 * 
	 */
	boolean canUndo();
	
	
	/**
	 * Za odredivanje da li je moguc redo.
	 * 
	 * @return
	 * Slicno kao i prethodna metoda.
	 */
	boolean canRedo();
	
	
	/**
	 * Vraca listu naziva komandi
	 * koje su prethodno obavljene.
	 * 
	 * @return
	 * Jasno samo po sebi.
	 * 
	 */
	List<String> getUndoList();
	
	
	/**
	 * Vraca listu naziva komandi
	 * koje ce tek biti obavljene.
	 * 
	 * @return
	 * Jasno samo po sebi.
	 * 
	 */
	List<String> getRedoList();
	
	
}





