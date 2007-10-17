package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;

import java.util.List;




/**
 * Sluzi za prosirivanje zice novim segmentom.
 * 
 * @author brijest
 *
 */
public class ExpandWireCommand implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = "ExpandWireCommand";
	
	
	/* private fields */
	private Caseless name;
	private WireSegment segment;
	private List<WireSegment> seglst;
	
	
	/* ctors */

	/**
	 * Prosiruje zicu imena wirename, sa
	 * segmentom odredenim s koordinatama.
	 * Ako segment nije horizontalan ili vertikalan,
	 * bit ce bacen exception.
	 */
	public ExpandWireCommand(Caseless wirename, int x1, int y1, int x2, int y2)
		throws IllegalArgumentException
	{
		name = wirename;
		
		if (x1 != x2 && y1 != y2)
			throw new IllegalArgumentException("Can only expand wires with" +
					"horizontal and vertical segments");
		
		segment = new WireSegment(x1, y1, x2, y2);
		seglst = null;
	}
	
	/**
	 * Prosiruje zicu imena wirename sa listom segmenata.
	 */
	public ExpandWireCommand(Caseless wirename, List<WireSegment> segments) {
		name = wirename;
		
		if (segments == null) throw new IllegalArgumentException("Segments cannot be null.");
		
		segment = null;
		seglst = segments;
	}
	
	

	/* methods */
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaWire wire = info.getWires().fetchWire(name);
		
		if (wire == null) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
					"Wire with name '" + name + "' does not exist"));
		}
		
		if (segment != null) {
			if (!wire.insertSegment(segment)) return new CommandResponse(
					new SchemaError(EErrorTypes.WIRE_OVERLAP,
					"Segment would overlap with other segments."));
		} else {
			int i = -1;
			for (WireSegment ws : seglst) {
				if (!wire.insertSegment(ws)) {
					// return to previous state
					for (; i >= 0; i--) {
						wire.removeSegment(seglst.get(i));
					}
					return new CommandResponse(
						new SchemaError(EErrorTypes.WIRE_OVERLAP,
						"One of the segments would overlap with other segments."));
				}
				i++;
			}
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		ISchemaWire wire = info.getWires().fetchWire(name);
		
		if (wire == null) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
					"Wire with name '" + name + "' does not exist"));
		}
		
		if (segment != null) {
			boolean found = wire.removeSegment(segment);
			if (!found) return new CommandResponse(new SchemaError(EErrorTypes.CANNOT_UNDO,
					"Formerly inserted wire segment not found."));
		} else {
			for (WireSegment ws : seglst) {
				boolean found = wire.removeSegment(ws);
				if (!found) return new CommandResponse(new SchemaError(EErrorTypes.CANNOT_UNDO,
						"Formerly inserted wire segment not found."));
			}
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
}













