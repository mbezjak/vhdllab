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
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;





/**
 * Mice komponentu danog imena na specificirano mjesto.
 * 
 * @version 1.0
 * Ako je na neki port komponente bila spojena neka zica,
 * tad se toj zici dodaje jos jedan segment tako da bude
 * produzena do nove lokacije porta.
 * Ova ce komanda biti reimplementirana tako da to bude
 * pametnije rijeseno pomocu AutoConnect-a, ovo je samo
 * privremeno rjesenje.
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
		return false;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaComponentCollection components = info.getComponents();
		ISchemaComponent cmp = components.fetchComponent(cmpname);
		
		if (cmp == null) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME,
					"Component '" + cmpname.toString() + "' does not exist."));
		}
		
		// find old location
		XYLocation oldloc = components.getComponentLocation(cmpname);
		
		// find mapped ports
		Map<Caseless, Integer> toexpand = new HashMap<Caseless, Integer>();
		int i = 0;
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			Caseless mapping = sp.getMapping();
			if (Caseless.isNullOrEmpty(mapping)) continue;
			toexpand.put(mapping, i);
			i++;
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
				throw new IllegalStateException("Component could not be added back after removal.");
			} catch (OverlapException e1) {
				throw new IllegalStateException("Component caused overlap while being added back to old location.");
			}
			return new CommandResponse(new SchemaError(EErrorTypes.COMPONENT_OVERLAP,
					"Component overlaps other components at desired location."));
		}
		
		// add new wire segments
		ISchemaWireCollection wires = info.getWires();
		for (Entry<Caseless, Integer> entry : toexpand.entrySet()) {
			ISchemaWire wire = wires.fetchWire(entry.getKey());
			if (wire == null) throw new IllegalStateException("Port was mapped to '" + entry.getKey() + "' and this wire does not exist.");
			wire.insertSegment(new WireSegment(oldloc.x, oldloc.y, oldloc.x, loc.y));
			wire.insertSegment(new WireSegment(oldloc.x, loc.y, loc.x, loc.y));
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		throw new InvalidCommandOperationException("Undo not applicible here.");
	}
	

}























