package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.AutoRenamer;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.model.queries.FindDisjointSegments;
import hr.fer.zemris.vhdllab.applets.schema2.model.queries.FindDisjointSegments.DisjointSets;




/**
 * Brise jedan segment odredene zice i u slucaju
 * da nakon brisanja tog segmenta zice bude podijeljena
 * u vise cjelina, obavlja podjelu zice na dvije nove.
 * Ako ova komanda obrise posljednji segment zice, bit
 * ce obrisana i citava zica.
 * 
 * @author brijest
 *
 */
public class DeleteSegmentAndDivideCommand implements ICommand {
	
	private enum EActionState {
		normal() {
			@Override
			public ICommandResponse housework(ISchemaInfo info, DeleteSegmentAndDivideCommand command) {
				// do absolutely nothing
				return null;
			}
		},
		deleted() {
			@Override
			public ICommandResponse housework(ISchemaInfo info, DeleteSegmentAndDivideCommand command) {
				SchemaWire wire = new SchemaWire(command.wirename);
				
				wire.insertSegment(command.segment);
				try {
					info.getWires().addWire(wire);
				} catch (DuplicateKeyException e) {
					throw new IllegalStateException("Duplicate key problems when performing undo.", e);
				} catch (OverlapException e) {
					throw new IllegalStateException("Overlap problems when performing undo.", e);
				}
				
				return null;
			}
		},
		divided() {
			@Override
			public ICommandResponse housework(ISchemaInfo info, DeleteSegmentAndDivideCommand command) {
				ISchemaWireCollection wires = info.getWires();
				ISchemaWire divwire = wires.fetchWire(command.dividedname);
				ISchemaWire wire = wires.fetchWire(command.wirename);
				
				if (divwire == null) throw new IllegalStateException("Could not find wire from division.");
				if (wire == null) throw new IllegalStateException("Could not find wire '"
						+ command.wirename + "'.");
				
				// put all segments from the divided wire to the original wire
				for (WireSegment ws : divwire.getSegments()) {
					wire.insertSegment(ws);
				}
				
				// remove divided wire
				try {
					wires.removeWire(command.dividedname);
				} catch (UnknownKeyException e) {
					throw new IllegalStateException("Could not remove wire '" + command.dividedname
							+ "' that exists.", e);
				}
				
				// assign null to divided wire name, as it does not exist anymore
				command.dividedname = null;
				
				return null;
			}
		};
		
		/**
		 * @return
		 * Returns null if everything went well,
		 * leaving the calling method to do the work.
		 */
		public abstract ICommandResponse housework(ISchemaInfo info, DeleteSegmentAndDivideCommand command);
	}
	

	/* static fields */
	public static final String COMMAND_NAME = "DeleteSegmentAndDivideCommand";

	
	/* private fields */
	private Caseless wirename;
	private WireSegment segment;
	private Caseless dividedname;
	private EActionState actionstate;
	
	

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
		ISchemaWireCollection wires = info.getWires();
		ISchemaWire wire = wires.fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		// remove segment
		if (!wire.removeSegment(segment)) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_SEGMENT,
					"'" + segment.toString() + "' not among segments belonging to wire '" + wirename + "'."));
		}
		
		actionstate = EActionState.normal;
		
		// now check if this was the last segment, and delete wire if necessary
		if (wire.getSegments().size() == 0) {
			try {
				wires.removeWire(wirename);
			} catch (UnknownKeyException e) {
				throw new IllegalStateException("Wire '" + wirename + "' cannot be removed, " +
						"yet it has been found.", e);
			}
			
			actionstate = EActionState.deleted;
		} else { // divide wire into 2 wires if necessary
			FindDisjointSegments findsegs = new FindDisjointSegments(wirename);
			IQueryResult result = findsegs.performQuery(info);
			
			if (!result.isSuccessful()) {
				wire.insertSegment(segment);
				return new CommandResponse(new SchemaError(EErrorTypes.INNER_QUERY_NOT_PERFORMED,
						"Could not analyze disjoint sets to find potential wire divisions."));
			}
			
			DisjointSets dissets = (DisjointSets)result.get(FindDisjointSegments.KEY_SEGMENT_SETS);
			int dsize = dissets.segmentsets.size();
			if (dsize != 1) {
				if (dsize != 2) throw new IllegalStateException("Removal of segment caused " + dsize +
						" disjunct segment sets. Removal of a single segment should create at most 2.");
				
				// create a new wire
				dividedname = AutoRenamer.getFreeName(wirename, wires.getWireNames());
				ISchemaWire nwire = new SchemaWire(dividedname);
				
				// transform wire segments to the new wire
				for (WireSegment ws : dissets.segmentsets.get(1)) {
					wire.removeSegment(ws);
					nwire.insertSegment(ws);
				}
				
				// put new wire to wire collection
				try {
					wires.addWire(nwire);
				} catch (DuplicateKeyException e) {
					throw new IllegalStateException("Autorenamer approved the name, " +
							"yet name seems to already exist.", e);
				} catch (OverlapException e) {
					throw new IllegalStateException("Overlapping problems with the new wire, " +
							"and they didn't seem to exist with the old wire.", e);
				}
				
				actionstate = EActionState.divided;
			}
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		// perform housework
		ICommandResponse resp = actionstate.housework(info, this);
		if (resp != null && !resp.isSuccessful()) return resp;
		
		// fetch wire and insert the segment back
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		wire.insertSegment(segment);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
}









