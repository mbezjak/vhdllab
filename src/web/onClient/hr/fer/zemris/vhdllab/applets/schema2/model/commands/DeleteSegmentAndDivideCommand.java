package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;




/**
 * Brise jedan segment odredene zice i u slucaju
 * da nakon brisanja tog segmenta zice bude podijeljena
 * u vise cjelina, obavlja podjelu zice na dvije nove.
 * 
 * @author brijest
 *
 */
public class DeleteSegmentAndDivideCommand implements ICommand {

	/* static fields */

	/* private fields */

	/* ctors */

	/* methods */

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

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		// TODO Auto-generated method stub
		return null;
	}
	
}









