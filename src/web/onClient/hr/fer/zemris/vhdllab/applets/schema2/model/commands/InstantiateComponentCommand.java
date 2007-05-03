package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
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
	private int xpos, ypos;
	
	
	public final String COMMAND_NAME = "InstantiateComponentCommand";
	
	
	/**
	 * Konstruktor za zahtjev za stvaranjem i dodavanjem
	 * nove komponente. 
	 * 
	 * @param componentTypeName
	 * Ime tipa komponente.
	 * @param instanceName
	 * Ime instance novokreirane komponente.
	 * @param x
	 * Ovo je x pozicija na canvasu.
	 * @param y
	 * Ovo je y pozicija na canvasu.
	 */
	public InstantiateComponentCommand(Caseless componentTypeName, Caseless instanceName, int x, int y) {
		cpType = componentTypeName;
		instName = instanceName;
		xpos = x;
		ypos = y;
	}
	
	
	

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		
		try {
			ISchemaComponent component = info.getPrototyper().clonePrototype(cpType, instName);
			
			info.getComponents().addComponent(xpos, ypos, component);
		} catch (UnknownComponentPrototypeException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_PROTOTYPE,
				"Prototype not found."));
		} catch (OverlapException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.COMPONENT_OVERLAP,
				"Component overlaps."));
		} catch (DuplicateKeyException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.DUPLICATE_COMPONENT_NAME,
				"Duplicate component name."));
		}
		
		List<ChangeTuple> clist = new ArrayList<ChangeTuple>();
		clist.add(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
		return new CommandResponse(clist);
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException {
		try {
			info.getComponents().removeComponent(instName);
		} catch (UnknownKeyException e) {
			ICommandResponse resp = new CommandResponse(
					new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME));
			
			return resp;
		}
		
		List<ChangeTuple> clist = new ArrayList<ChangeTuple>();
		clist.add(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
		return new CommandResponse(clist);
	}

}





