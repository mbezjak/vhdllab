package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownComponentPrototypeException;
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
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;




/**
 * Ovom komandom se na shemi instancira
 * komponenta.
 * 
 * @author Axel
 *
 */
public class InstantiateComponentCommand implements ICommand {
	
	public final String COMMAND_NAME = "InstantiateComponentCommand";
	
	
	private Caseless cpType;
	private Caseless instName;
	private int xpos, ypos;
	
	
	
	
	/**
	 * Konstruktor za zahtjev za stvaranjem i dodavanjem
	 * nove komponente. 
	 * 
	 * @param componentTypeName
	 * Ime tipa komponente.
	 * @param x
	 * Ovo je x pozicija na canvasu.
	 * @param y
	 * Ovo je y pozicija na canvasu.
	 */
	public InstantiateComponentCommand(Caseless componentTypeName, int x, int y) {
		cpType = componentTypeName;
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
			ISchemaComponentCollection components = info.getComponents();
			ISchemaComponent component = info.getPrototyper().
				clonePrototype(cpType, components.getComponentNames());
			
			if (component.isInvalidated())
				throw new IllegalStateException("Component within prototyper is invalidated.");
			
			// remember chosen instance name, so you can remove it on undo
			instName = component.getName();
			
			components.addComponent(xpos, ypos, component);
			
			// plug wires in if there are any at schema port locations
			ISchemaWireCollection wires = info.getWires();
			XYLocation cmploc = components.getComponentLocation(instName);
			for (SchemaPort sp : component.getSchemaPorts()) {
				XYLocation spxy = sp.getOffset();
				int xsp = cmploc.x + spxy.x, ysp = cmploc.y + spxy.y;
				Set<ISchemaWire> wat = wires.fetchAllWires(xsp, ysp);
				if (wat != null && !wat.isEmpty()) {
					ISchemaWire w = wat.iterator().next();
					Set<WireSegment> segs = w.segmentsAt(xsp, ysp);
					if (segs != null && !segs.isEmpty()) {
						sp.setMapping(w.getName());
					}
				}
			}
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
			throws InvalidCommandOperationException
	{
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
















