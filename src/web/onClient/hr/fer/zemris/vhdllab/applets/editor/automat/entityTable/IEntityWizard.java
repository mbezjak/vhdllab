package hr.fer.zemris.vhdllab.applets.editor.automat.entityTable;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
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
	public void setProjectContainer(ISystemContainer container);
	
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
