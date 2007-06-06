package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.CommandExecutorException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;




/**
 * 
 * @author Axel
 *
 */
public class SchemaCore implements ISchemaCore {
	private ISchemaInfo info;
	private List<ICommand> undolist;
	private List<ICommand> redolist;
	private ISchemaController controller_listener;
	
	private void reportChanges(List<ChangeTuple> changes) {
		
	}
	
	
	public SchemaCore() {
		info = new SchemaInfo(new Caseless("Default"));
		undolist = new LinkedList<ICommand>();
		redolist = new LinkedList<ICommand>();
		controller_listener = null;
	}
	
	/**
	 * Automatski postavlja controller-a
	 * kojem ce se slati obavijesti o promjenama.
	 * 
	 * @param controller
	 */
	public SchemaCore(ISchemaController controller) {
		info = new SchemaInfo(new Caseless("Default"));
		undolist = new LinkedList<ICommand>();
		redolist = new LinkedList<ICommand>();
		controller_listener = controller;
	}
	
	
	public ISchemaInfo getSchemaInfo() {
		return info;
	}

	public boolean canRedo() {
		return (!redolist.isEmpty());
	}

	public boolean canUndo() {
		return (!undolist.isEmpty() && undolist.get(undolist.size() - 1).isUndoable());
	}

	public ICommandResponse executeCommand(ICommand command) {
		ICommandResponse response = command.performCommand(info);
		if (response.isCommandSuccessful()) {
			redolist.clear();
			if (command.isUndoable()) {
				undolist.add(command);
			} else {
				undolist.clear();
			}
		}
		
		reportChanges(response.getPropertyChanges());
		
		return response;
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
		if (redolist.isEmpty()) throw new CommandExecutorException("Empty redo command stack.");
		ICommand comm = redolist.get(redolist.size() - 1);
		redolist.remove(redolist.size() - 1);
		
		ICommandResponse response = comm.performCommand(info);
		if (!response.isCommandSuccessful()) {
			undolist.clear();
			redolist.clear();
			throw new CommandExecutorException("Cannot redo command.");
		}
		else undolist.add(comm);
		
		reportChanges(response.getPropertyChanges());
		
		return response;
	}

	public ICommandResponse undo() throws CommandExecutorException {
		if (undolist.isEmpty()) throw new CommandExecutorException("Empty undo command stack.");
		ICommand comm = undolist.get(undolist.size() - 1);
		undolist.remove(undolist.size() - 1);
		
		ICommandResponse response = comm.performCommand(info);
		if (!response.isCommandSuccessful()) {
			undolist.clear();
			redolist.clear();
			throw new CommandExecutorException("Cannot undo command. Reason: "
					+ response.getError().toString());
		}
		else redolist.add(comm);
		
		reportChanges(response.getPropertyChanges());
		
		return response;
	}


	public void registerController(ISchemaController controller) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
}













