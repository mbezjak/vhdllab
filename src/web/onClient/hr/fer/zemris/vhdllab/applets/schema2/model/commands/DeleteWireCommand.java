package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;






public class DeleteWireCommand implements ICommand {
	
	private static class ConnectionInfo {
		public Caseless cmpname, portname;
		public ConnectionInfo(Caseless componentName, Caseless schemaPortName) {
			cmpname = componentName;
			portname = schemaPortName;
		}
	}

	/* static fields */
	public static final String COMMAND_NAME = "DeleteWireCommand";
	
	
	/* private fields */
	private Caseless name;
	private ISchemaWire deleted;
	private List<ConnectionInfo> cachedconnections;
	
	
	/* ctors */
	
	public DeleteWireCommand(Caseless wireName) {
		name = wireName;
		deleted = null;
		cachedconnections = new ArrayList<ConnectionInfo>();
	}
	
	
	
	
	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		deleted = info.getWires().fetchWire(name);
		
		if (deleted == null) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
					"Wire with name '" + name.toString() + "' could not be found."));
		}
		
		try {
			info.getWires().removeWire(name);
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Wire was found once, but not twice!");
		}
		
		unplugAndCacheConnections(info);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.ANY_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		if (deleted == null)
			throw new InvalidCommandOperationException("Cannot call undo before command has been performed.");
		
		try {
			info.getWires().addWire(deleted);
		} catch (DuplicateKeyException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.DUPLICATE_WIRE_NAME,
					"Wire with name '" + name.toString() + "' already exists."));
		} catch (OverlapException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.WIRE_OVERLAP,
					"Wire with name '" + name.toString() + "' would overlap something else."));
		}
		
		plugCachedConnections(info);
		
		deleted = null;
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	private void unplugAndCacheConnections(ISchemaInfo info) {
		SchemaPort sp = null;
		Iterator<SchemaPort> spit = null;
		for (PlacedComponent placed : info.getComponents()) {
			spit = placed.comp.schemaPortIterator();
			while (spit.hasNext()) {
				sp = spit.next();
				if (name.equals(sp.getMapping())) {
					cachedconnections.add(new ConnectionInfo(placed.comp.getName(), sp.getName()));
					sp.setMapping(null);
				}
			}
		}
	}
	
	private void plugCachedConnections(ISchemaInfo info) {
		ISchemaComponentCollection components = info.getComponents();
		for (ConnectionInfo ci : cachedconnections) {
			ISchemaComponent cmp = components.fetchComponent(ci.cmpname);
			SchemaPort sp = cmp.getSchemaPort(ci.portname);
			sp.setMapping(name);
		}
		cachedconnections.clear();
	}
	
	
	
	
	
}
















