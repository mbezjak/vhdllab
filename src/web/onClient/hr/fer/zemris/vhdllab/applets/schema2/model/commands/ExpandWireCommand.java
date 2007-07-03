package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;




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
		
		wire.insertSegment(segment);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		ISchemaWire wire = info.getWires().fetchWire(name);
		
		if (wire == null) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
					"Wire with name '" + name + "' does not exist"));
		}
		
		boolean found = wire.removeSegment(segment);
		if (!found) return new CommandResponse(new SchemaError(EErrorTypes.CANNOT_UNDO,
				"Formerly inserted wire segment not found."));
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
}













