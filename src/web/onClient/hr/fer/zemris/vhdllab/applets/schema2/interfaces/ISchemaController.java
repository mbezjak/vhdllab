package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;

import java.beans.PropertyChangeListener;
import java.util.List;



/**
 * Posrednik izmedu ISchemaCorea (modela)
 * i bilo kojeg view-a.
 * 
 * @author Axel
 *
 */
public interface ISchemaController {
	
	/**
	 * Salje ICommand objekt do registriranog modela.
	 * ICommand objekt radi sve nuzne promjene na modelu
	 * sukladno tome o kojoj se komandi radi i vraca 
	 * ICommandResponse koji sadrzi informaciju o uspjesnosti
	 * izvodenja komande.
	 * 
	 * @param command
	 * @return
	 */
	ICommandResponse send(ICommand command);
	
	/**
	 * Registrira core na controller. Ako se core ne registrira
	 * na controller, zahtjevi od view-ova nece biti proslijedeni
	 * nikome.
	 * 
	 * @param core
	 * Core kojem ce biti proslijedeni zahtjevi.
	 * Ako se proslijedi null, zahtjevi nece biti proslijedivani.
	 */
	void registerCore(ISchemaCore core);
	
	/**
	 * Ovom metodom model dojavljuje controlleru promjene.
	 * 
	 * @param changes
	 * Lista promjena. Za null ili praznu listu se
	 * nista nece prijaviti.
	 */
	void handleChanges(List<ChangeTuple> changes);

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









