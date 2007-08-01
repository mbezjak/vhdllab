package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.model.queries.FindDisjointSegments;




/**
 * Brise jedan segment odredene zice i u slucaju
 * da nakon brisanja tog segmenta zice bude podijeljena
 * u vise cjelina, obavlja podjelu zice na dvije nove.
 * 
 * @author brijest
 *
 */
public class DeleteSegmentAndDivideCommand implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = "DeleteSegmentAndDivideCommand";

	
	/* private fields */
	private Caseless wirename;
	private WireSegment segment;
	private Caseless dividedwire;
	
	

	/* ctors */
	
	public DeleteSegmentAndDivideCommand(Caseless wireToEditName, WireSegment segmentToDelete) {
		wirename = wireToEditName;
		segment = segmentToDelete;
	}
	
	
	

	/* methods */

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		// remove segment
		if (!wire.removeSegment(segment)) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_SEGMENT,
					"'" + segment.toString() + "' not among segments belonging to wire '" + wirename + "'."));
		}
		
		// now divide wire into 2 wires if necessary
		FindDisjointSegments findsegs = new FindDisjointSegments(wirename);
		IQueryResult result = findsegs.performQuery(info);
		
		if (!result.isSuccessful()) {
			wire.insertSegment(segment);
			return new CommandResponse(new SchemaError(EErrorTypes.INNER_QUERY_NOT_PERFORMED,
					"Could not analyze disjoint sets to find potential wire divisions."));
		}
		
		
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		wire.insertSegment(segment);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
}









