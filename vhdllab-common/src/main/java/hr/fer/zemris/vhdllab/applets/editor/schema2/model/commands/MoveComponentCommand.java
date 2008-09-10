package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.IntList;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;





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
	private Map<Integer, Caseless> unplugged;
	private IntList plugged;
	

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
		
		if (cmp.isInvalidated()) return new CommandResponse(
				new SchemaError(EErrorTypes.COMPONENT_INVALIDATED, "Component '" + cmpname.toString() +
						"' is invalidated."));
		
		// find old location
		oldloc = components.getComponentLocation(cmpname);
		
		// find mapped ports and unmap them
		int count = -1;
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			count++;
			Caseless mapping = sp.getMapping();
			if (Caseless.isNullOrEmpty(mapping)) continue;
			if (unplugged == null) unplugged = new HashMap<Integer, Caseless>();
			
			/* unplug and cache */
			sp.setMapping(null);
			unplugged.put(count, mapping);
		}
		
		// find wires at port locations and map them, caching them
		ISchemaWireCollection wires = info.getWires();
		XYLocation cmploc = components.getComponentLocation(cmpname);
		count = -1;
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			count++;
			XYLocation spxy = sp.getOffset();
			int xsp = cmploc.x + spxy.x, ysp = cmploc.y + spxy.y;
			Set<ISchemaWire> wat = wires.fetchAllWires(xsp, ysp);
			if (wat != null && !wat.isEmpty()) {
				ISchemaWire w = wat.iterator().next();
				Set<WireSegment> segs = w.segmentsAt(xsp, ysp);
				if (segs != null && !segs.isEmpty()) {
					sp.setMapping(w.getName());
					if (plugged == null) plugged = new IntList();
					plugged.add(count);
				}
			}
		}
		
		// add component to new location
		try {
			components.reinsertComponent(cmpname, loc.x, loc.y);
		} catch (OverlapException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.COMPONENT_OVERLAP,
					"Component overlaps other components at desired location."));
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Component could not found, although it has been before.");
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
		
		/* unplug plugged ports */
		if (plugged != null) {
			for (int i = 0, sz = plugged.size(); i < sz; i++) {
				cmp.getSchemaPort(plugged.get(i)).setMapping(null);
			}
			plugged = null;
		}
		
		/* plug back in cached mappings */
		if (unplugged != null) {
			for (Entry<Integer, Caseless> ntry : unplugged.entrySet()) {
				cmp.getSchemaPort(ntry.getKey()).setMapping(ntry.getValue());
			}
			unplugged = null;
		}
		
		// add component to new location
		try {
			components.reinsertComponent(cmpname, oldloc.x, oldloc.y);
		} catch (OverlapException e) {
			throw new IllegalStateException("Component overlaps other components at desired location " +
					"while performing undo.");
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Component could not found, although it has been before.");
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	

}























