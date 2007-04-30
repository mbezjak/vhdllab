package hr.fer.zemris.vhdllab.applets.schema2.commands;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;




/**
 * Ovom komandom se na shemi instancira
 * komponenta.
 * 
 * @author Axel
 *
 */
public class InstantiateComponentCommand implements ICommand {

	public String getCommandName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUndoable() {
		// TODO Auto-generated method stub
		return false;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException {
		// TODO Auto-generated method stub
		return null;
	}

}
