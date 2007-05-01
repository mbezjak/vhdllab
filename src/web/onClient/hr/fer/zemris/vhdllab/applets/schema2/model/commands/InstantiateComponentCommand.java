package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;




/**
 * Ovom komandom se na shemi instancira
 * komponenta.
 * 
 * @author Axel
 *
 */
public class InstantiateComponentCommand implements ICommand {
	private Caseless cpType;
	private Caseless instName;
	
	
	public final String COMMAND_NAME = "InstantiateComponentCommand";
	
	public InstantiateComponentCommand(Caseless componentTypeName, Caseless instanceName) {
		cpType = componentTypeName;
		instName = instanceName;
	}
	
	
	

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		if (info.getComponents().containsName(instName)) {
			ICommandResponse resp = new CommandResponse(
					new SchemaError(EErrorTypes.DUPLICATE_COMPONENT_NAME, "Component name not unique."));
			
			return resp;
		}
		
		return null;
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException {
		// TODO Auto-generated method stub
		return null;
	}

}





