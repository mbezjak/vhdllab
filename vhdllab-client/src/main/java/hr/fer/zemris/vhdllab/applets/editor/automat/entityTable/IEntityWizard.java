package hr.fer.zemris.vhdllab.applets.editor.automat.entityTable;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;
/**
 * Interface for table wizard used to create CircuitInterface for new circuits
 * WARNING Source of this interface might still be changed, but existing methods
 * will remain
 * @author Davor Delac
 *
 */
public interface IEntityWizard {
	/**
	 * Sets up systemContainer the same way as in IEditor
	 * @param container
	 */
	public void setPlatformContainer(PlatformContainer container);
	
	/**
	 * Sets the initial data for the table. If data==null initially table ind circuit
	 * name are blank.
	 * @param data
	 */
	public void setData(CircuitInterface data);
	
	/**
	 * Method provides the mean of getting result formatted as a class implementing
	 * circuit interface.
	 * @return Circuit interface representation of entity blok.
	 */
	public CircuitInterface getCircuitInterface();
	
	/**
	 * Call this metod to stop the editing of the table and remove the editor component.
	 *
	 */
	public void updateTable();
	
	/**
	 * Call this method to test if current data is correct in. Usefull in wizards if
	 * you want to disable next or finish buttons in order to prevent incorrect data
	 * input.
	 */
	public boolean isDataCorrect();
	
	/**
	 * initialize GUI
	 *
	 */
	public void init();
}
