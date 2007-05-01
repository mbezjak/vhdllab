package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.CommandExecutorException;

import java.beans.PropertyChangeListener;
import java.util.List;


/**
 * Ovaj interface propisuje nacin funkcioniranja same logike schematica. Njegove
 * implementacije sadrze informacije o stanju sheme, komponentama koje su na
 * shemi, zicama, itd.
 * 
 * @author Axel
 * 
 */
public interface ISchemaCore {

	/**
	 * Dohvaca podatke o shemi.
	 * 
	 * @return
	 */
	ISchemaInfo getSchemaInfo();

	/**
	 * Obavlja navedenu komandu. Ako je ona uspjesno izvedena, i ako
	 * command.isUndoable() vraca true, stavlja je na stog undo komandi, a stog
	 * redo komandi se brise. Ako command.isUndoable() vraca false, stog
	 * prethodno izvedenih komandi se brise.
	 * 
	 * @return Vraca objekt koji vraca sama komanda pri izvodenju.
	 * 
	 */
	ICommandResponse executeCommand(ICommand command);

	/**
	 * Za odredivanje da li je moguc undo.
	 * 
	 * @return Ako stog prethodno izvedenih komandi nije prazna, vraca true, u
	 *         protivnom false.
	 * 
	 */
	boolean canUndo();

	/**
	 * Za odredivanje da li je moguc redo.
	 * 
	 * @return Slicno kao i prethodna metoda.
	 */
	boolean canRedo();

	/**
	 * Vraca listu naziva komandi koje su prethodno obavljene. Vraca praznu
	 * listu, ako takvih nema.
	 * 
	 * @return Jasno samo po sebi.
	 * 
	 */
	List<String> getUndoList();

	/**
	 * Vraca listu naziva komandi koje ce tek biti obavljene. Ako takvih nema,
	 * vraca praznu listu.
	 * 
	 * @return Jasno samo po sebi.
	 * 
	 */
	List<String> getRedoList();

	/**
	 * Obavlja komandu na vrhu stoga undo komandi, i ako je rezultat uspjesan,
	 * popne je i stavi na stog redo komandi. OPREZ: U principu se ne bi trebalo
	 * dogoditi da je rezultat izvodenja neuspjesan, no ako se to dogodi, brisu
	 * se oba stoga, i undo i redo stog.
	 * 
	 * @throws CommandExecutorException
	 * Ako je stog undo komandi prazan, ili se dogodila neka druga greska.
	 * Pritom odmah cisti oba stoga.
	 * 
	 * @return Objekt koji govori o uspjesnosti izvodenja undo komande.
	 */
	ICommandResponse undo() throws CommandExecutorException;

	/**
	 * Obavlja komandu s vrha redo stoga i stavlja je na undo stog.
	 * 
	 * @throws CommandExecutorException
	 * Slicno kao i prethodno, samo za redo stog.
	 * Pritom odmah cisti oba stoga.
	 * 
	 * @return Vidi prethodnu metodu.
	 */
	ICommandResponse redo() throws CommandExecutorException;
	
	
	/**
	 * Dodaje novi listener. Pri bilo kakvoj
	 * promjeni u modelu sheme svi ce listeneri
	 * biti obavijesteni.
	 * 
	 * @param listener
	 * @param changeType
	 * Tip promjene za koji se listener aktivira.
	 * ANY_CHANGE za sve vrste promjena.
	 */
	void addListener(EPropertyChange changeType, PropertyChangeListener listener);
	
	
	/**
	 * Mice listener s popisa.
	 * 
	 * @param listener
	 */
	void removeListener(PropertyChangeListener listener);
	
}












