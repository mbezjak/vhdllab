package hr.fer.zemris.vhdllab.applets.automat.entityTable;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
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
	 * Sets up projectContainer the same way as in IEditor
	 * @param pContainer
	 */
	public void setProjectContainer(ProjectContainer pContainer);
	
	/**
	 * Sets the initial data for the table. If data==null initially table ind circuit
	 * name are blank.
	 * @param data
	 */
	public void setData(TableData data);
	
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
}
