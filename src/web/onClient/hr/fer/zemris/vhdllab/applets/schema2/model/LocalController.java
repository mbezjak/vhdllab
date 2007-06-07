package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;




/**
 * Lokalni kontroler - pretpostavlja
 * da su model i view unutar istog procesa.
 * 
 * @author Axel
 *
 */
public class LocalController implements ISchemaController {
	private PropertyChangeSupport support;
	private ISchemaCore core;
	
	
	public LocalController() {
		support = new PropertyChangeSupport(this);
		core = null;
	}
	
	public LocalController(ISchemaCore coreToSendTo) {
		support = new PropertyChangeSupport(this);
		core = coreToSendTo;
	}
	
	
	
	
	
	

	public ICommandResponse send(ICommand command) {
		return core.executeCommand(command);
	}

	public void registerCore(ISchemaCore coreToSendTo) {
		core = coreToSendTo;
	}
	
	public void addListener(EPropertyChange changeType, PropertyChangeListener listener) {
		changeType.assignListenerToSupport(listener, support);
	}


	public void removeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	public void handleChanges(List<ChangeTuple> changes) {
		if (changes != null) {
			for (ChangeTuple change : changes) {
				change.changetype.firePropertyChanges(support, change.oldval, change.oldval);
			}
		}
	}

	public ISchemaInfo getSchemaInfo() {
		return core.getSchemaInfo();
	}
	
	

}













