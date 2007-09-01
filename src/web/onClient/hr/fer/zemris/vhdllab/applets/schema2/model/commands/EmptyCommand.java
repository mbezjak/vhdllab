package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;


public class EmptyCommand implements ICommand {	

	/* static fields */
	public static final String COMMAND_NAME = EmptyCommand.class.getSimpleName();

	/* private fields */
	private EPropertyChange proptrig;
	

	/* ctors */
	
	public EmptyCommand(EPropertyChange propertyTrigger) {
		proptrig = propertyTrigger;
	}
	
	

	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		return new CommandResponse(new ChangeTuple(proptrig));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		return new CommandResponse(true);
	}

}














