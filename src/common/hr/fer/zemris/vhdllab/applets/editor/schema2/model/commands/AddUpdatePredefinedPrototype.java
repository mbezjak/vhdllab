package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.UserComponent;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;


/**
 * Updatea postojeci prototip istog imena, ili
 * dodaje novi u slucaju da prototip tog imena
 * ne postoji.
 * 
 * @author Axel
 *
 */
public class AddUpdatePredefinedPrototype implements ICommand {
	
	/* static fields */
	public static final String COMMAND_NAME = AddUpdatePredefinedPrototype.class.getSimpleName();

	/* private fields */
	private CircuitInterface addupdci;
	

	/* ctors */
	
	public AddUpdatePredefinedPrototype(CircuitInterface toAddOrUpdate) {
		addupdci = toAddOrUpdate;
	}
	
	

	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return false;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaPrototypeCollection prototyper = info.getPrototyper();
		Caseless ciname = new Caseless(addupdci.getEntityName());
		
		if (prototyper.containsPrototype(ciname)) {
			// remove existing
			prototyper.removePrototype(ciname);
		}
		
		// add new
		try {
			prototyper.addPrototype(new UserComponent(addupdci));
		} catch (DuplicateKeyException e) {
			// this should never happen
			e.printStackTrace();
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.PROTOTYPES_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
		throws InvalidCommandOperationException
	{
		throw new InvalidCommandOperationException();
	}

}














