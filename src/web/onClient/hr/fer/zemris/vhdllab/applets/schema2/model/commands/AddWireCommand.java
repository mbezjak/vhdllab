package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaWire;

import java.util.List;





public class AddWireCommand implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = "AddWireCommand";
	

	/* private fields */
	private SchemaWire wire;
	
	
	/* ctors */

	/**
	 * Za komande koje stvaraju zicu sa samo jednim horizontalnim
	 * ili vertikalnim segmentom.
	 * 
	 * Ako segment nije horizontalan ili vertikalan, bit ce bacen
	 * exception.
	 * 
	 */
	public AddWireCommand(Caseless wirename, int x1, int y1, int x2, int y2)
		throws IllegalArgumentException
	{
		if (x1 != x2 && y1 != y2) 
			throw new IllegalArgumentException("Can only create horizontal and vertical segments.");
		
		wire = new SchemaWire(wirename);
		
		wire.insertSegment(new WireSegment(x1, y1, x2, y2));
	}
	
	/**
	 * Za komande koje stvaraju zicu na temelju liste
	 * horizontalnih i vertikalnih segmenata.
	 * 
	 * @throws IllegalArgumentException
	 * U slucaju da segmenti nisu vertikalni ili horizontalni
	 * baca se exception.
	 */
	public AddWireCommand(Caseless wirename, List<WireSegment> segments)
		throws IllegalArgumentException
	{
		wire = new SchemaWire(wirename);
		
		for (WireSegment seg : segments) {
			if (seg.getStart().x != seg.getEnd().x && seg.getStart().y != seg.getEnd().y) {
				wire = null;
				throw new IllegalArgumentException("Can only create wire with " +
						"horizontal and vertical segments.");
			}
			
			wire.insertSegment(seg);
		}
	}
	
	
	
	/* methods */

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		try {
			info.getWires().addWire(wire);
		} catch (DuplicateKeyException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.DUPLICATE_WIRE_NAME,
					"Wire with the name '" + wire.getName() + "' already exists."));
		} catch (OverlapException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.WIRE_OVERLAP,
					"Cannot add wire due to overlapping problems."));
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		try {
			info.getWires().removeWire(wire.getName());
		} catch (UnknownKeyException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
					"The wire '" + wire.getName() + "' does not exist."));
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.ANY_CHANGE));
	}
	
}



















