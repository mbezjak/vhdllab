package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;





/**
 * Mice komponentu danog imena na specificirano mjesto.
 * 
 * @version 1.0
 * Ako je na neki port komponente bila spojena neka zica,
 * komanda nece biti izvrsena.
 * 
 * @author brijest
 *
 */
public class MoveComponentCommand implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = "MoveComponentCommand";
	

	/* private fields */
	private Caseless cmpname;
	private XYLocation loc;
	private XYLocation oldloc;
	

	/* ctors */

	/**
	 * Konstruktor komande.
	 * 
	 * @param componentName
	 * Ime komponente koju se zeli pomaknuti.
	 * @param moveTarget
	 * Mjesto na koje se zeli pomaknuti komponentu.
	 */
	public MoveComponentCommand(Caseless componentName, XYLocation moveTarget) {
		cmpname = componentName;
		loc = moveTarget;
	}
	
	

	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaComponentCollection components = info.getComponents();
		ISchemaComponent cmp = components.fetchComponent(cmpname);
		
		if (cmp == null) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME,
					"Component '" + cmpname.toString() + "' does not exist."));
		}
		
		// find old location
		oldloc = components.getComponentLocation(cmpname);
		
		// find mapped ports
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			Caseless mapping = sp.getMapping();
			if (Caseless.isNullOrEmpty(mapping)) continue;
			return new CommandResponse(new SchemaError(EErrorTypes.MAPPING_ERROR,
					COMMAND_NAME + " cannot move components that have mapped ports."));
		}
		
		// remove component
		try {
			components.removeComponent(cmpname);
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Component could not be removed, after it was found!");
		}
		
		// add component to new location
		try {
			components.addComponent(loc.x, loc.y, cmp);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("Component could not be added back after removal.");
		} catch (OverlapException e) {
			try {
				components.addComponent(oldloc.x, oldloc.y, cmp);
			} catch (DuplicateKeyException e1) {
				throw new IllegalStateException("Component could not be added back after removal " +
						"due to duplicate key.");
			} catch (OverlapException e1) {
				throw new IllegalStateException("Component caused overlap while being added back to " +
						"old location.");
			}
			return new CommandResponse(new SchemaError(EErrorTypes.COMPONENT_OVERLAP,
					"Component overlaps other components at desired location."));
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		ISchemaComponentCollection components = info.getComponents();
		ISchemaComponent cmp = components.fetchComponent(cmpname);
		
		if (cmp == null) {
			throw new IllegalStateException("Component '" + cmpname + "' cannot be found while " +
					"performing undo.");
		}
		
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			Caseless mapping = sp.getMapping();
			if (Caseless.isNullOrEmpty(mapping)) continue;
			throw new IllegalStateException("Component has mapped ports while performing undo.");
		}
		
		try {
			components.removeComponent(cmpname);
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Component could not be removed, after it was found!");
		}
		
		// add component to new location
		try {
			components.addComponent(oldloc.x, oldloc.y, cmp);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("Component could not be added back after removal.");
		} catch (OverlapException e) {
			try {
				components.addComponent(oldloc.x, oldloc.y, cmp);
			} catch (DuplicateKeyException e1) {
				throw new IllegalStateException("Component could not be added back after removal " +
						"due to duplicate key.");
			} catch (OverlapException e1) {
				throw new IllegalStateException("Component caused overlap while being added back to " +
						"old location.");
			}
			throw new IllegalStateException("Component overlaps other components at desired location " +
					"while performing undo.");
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	

}























