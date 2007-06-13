package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.CommandExecutorException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;

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
		ICommandResponse response = core.executeCommand(command);
		
		if (response.isSuccessful()) {
			for (ChangeTuple ct : response.getPropertyChanges()) {
				ct.changetype.firePropertyChanges(support, ct.oldval, ct.newval);
			}
		}
		
		return response;
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
	
	public ICommandResponse redo() {
		try {
			return core.redo();
		} catch(CommandExecutorException e) {
			ICommandResponse resp = new CommandResponse(new SchemaError(EErrorTypes.CANNOT_REDO));
			return resp;
		}
	}
	
	public ICommandResponse undo() {
		try {
			return core.undo();
		} catch(CommandExecutorException e) {
			ICommandResponse resp = new CommandResponse(new SchemaError(EErrorTypes.CANNOT_UNDO));
			return resp;
		}
		
	}

}













