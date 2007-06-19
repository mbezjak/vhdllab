package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.CommandExecutorException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;




/**
 * Lokalni kontroler - pretpostavlja
 * da su model i view unutar istog procesa.
 * 
 * @author Axel
 *
 */
public class LocalController implements ISchemaController {
	
	/* private fields */
	private PropertyChangeSupport support;
	private ISchemaCore core;
	private List<ICommand> undolist;
	private List<ICommand> redolist;
	
	
	
	
	/* ctors */
	
	public LocalController() {
		support = new PropertyChangeSupport(this);
		core = null;
		undolist = new LinkedList<ICommand>();
		redolist = new LinkedList<ICommand>();
	}
	
	public LocalController(ISchemaCore coreToSendTo) {
		support = new PropertyChangeSupport(this);
		core = coreToSendTo;
		undolist = new LinkedList<ICommand>();
		redolist = new LinkedList<ICommand>();
	}
	
	
	
	
	
	

	
	/* methods */

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
	
	public ICommandResponse send(ICommand command) {
		ICommandResponse response = core.executeCommand(command);
		if (response.isSuccessful()) {
			redolist.clear();
			if (command.isUndoable()) {
				undolist.add(command);
			} else {
				undolist.clear();
			}
		}
		
		reportChanges(response);

		return response;
	}
	
	private void reportChanges(ICommandResponse response) {
		if (response.getPropertyChanges() != null) for (ChangeTuple ct : response.getPropertyChanges()) {
			ct.changetype.firePropertyChanges(support, ct.oldval, ct.newval);
		}
	}
	
	public boolean canRedo() {
		return (!redolist.isEmpty());
	}

	public boolean canUndo() {
		return (!undolist.isEmpty() && undolist.get(undolist.size() - 1).isUndoable());
	}

	public List<String> getRedoList() {
		List<String> list = new LinkedList<String>();
		for (ICommand comm : redolist) {
			list.add(comm.getCommandName());
		}
		return list;
	}

	public List<String> getUndoList() {
		List<String> list = new LinkedList<String>();
		for (ICommand comm : undolist) {
			list.add(comm.getCommandName());
		}
		return list;
	}

	public ICommandResponse redo() throws CommandExecutorException {
		if (redolist.isEmpty())
			throw new CommandExecutorException("Empty redo command stack.");
		ICommand comm = redolist.get(redolist.size() - 1);
		redolist.remove(redolist.size() - 1);

		ICommandResponse response = core.executeCommand(comm);
		if (!response.isSuccessful()) {
			undolist.clear();
			redolist.clear();
			throw new CommandExecutorException("Cannot redo command.");
		} else
			undolist.add(comm);
		
		reportChanges(response);

		return response;
	}

	public ICommandResponse undo() throws CommandExecutorException {
		if (undolist.isEmpty())
			throw new CommandExecutorException("Empty undo command stack.");
		ICommand comm = undolist.get(undolist.size() - 1);
		undolist.remove(undolist.size() - 1);

		ICommandResponse response = core.executeCommand(comm);
		if (!response.isSuccessful()) {
			undolist.clear();
			redolist.clear();
			throw new CommandExecutorException("Cannot undo command. Reason: "
					+ response.getError().toString());
		} else
			redolist.add(comm);
		
		reportChanges(response);

		return response;
	}

	public void clearCommandCache() {
		undolist.clear();
		redolist.clear();
	}
	

}













