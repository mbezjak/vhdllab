package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.AutoRenamer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.IntList;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.FindDisjointSegments;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.FindDisjointSegments.DisjointSets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;




/**
 * Brise jedan segment odredene zice i u slucaju
 * da nakon brisanja tog segmenta zice bude podijeljena
 * u vise cjelina, obavlja podjelu zice na dvije nove.
 * Ako ova komanda obrise posljednji segment zice, bit
 * ce obrisana i citava zica.
 * Ako se obrise segment zice u doticaju s komponentom
 * u koju je ista zica ukopcana,
 * zica ce automatski biti iskopcana iz pripadnog porta
 * komponente.
 * 
 * @author brijest
 *
 */
public class DeleteSegmentAndDivideCommand implements ICommand {
	
	private static class UnplugWrapper {
		public Caseless compname;
		public int schemaportindex;
		public UnplugWrapper(Caseless cmpname, int index) {
			compname = cmpname;
			schemaportindex = index;
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof UnplugWrapper)) return false;
			UnplugWrapper other = (UnplugWrapper)obj;
			return other.compname.equals(this.compname) && other.schemaportindex == this.schemaportindex;
		}
		@Override
		public int hashCode() {
			return compname.hashCode() << 16 + schemaportindex;
		}
	}
	
	private static enum EActionState {
		normal() {
			@Override
			public ICommandResponse undoHousework(ISchemaInfo info, DeleteSegmentAndDivideCommand command) {
				// do absolutely nothing
				return null;
			}
		},
		deleted() {
			@Override
			public ICommandResponse undoHousework(ISchemaInfo info, DeleteSegmentAndDivideCommand command) {
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
			public ICommandResponse undoHousework(ISchemaInfo info, DeleteSegmentAndDivideCommand command) {
				ISchemaComponentCollection components = info.getComponents();
				ISchemaWireCollection wires = info.getWires();
				ISchemaWire wire = wires.fetchWire(command.wirename);
				
				// remap components to old wire
				for (Entry<Caseless, IntList> ntry : command.renamedmappings.entrySet()) {
					ISchemaComponent cmp = components.fetchComponent(ntry.getKey());
					IntList lst = ntry.getValue();
					for (int i = 0, sz = lst.size(); i < sz; i++) {
						cmp.getSchemaPort(lst.get(i)).setMapping(command.wirename);
					}
				}
				
				// for each name in divided set
				for (Caseless divname : command.divided) {
					ISchemaWire divwire = wires.fetchWire(divname);
				
					if (divwire == null) 
						throw new IllegalStateException("Could not find wire from division.");
					if (wire == null) throw new IllegalStateException("Could not find wire '"
							+ command.wirename + "'.");
				
					// put all segments from the divided wire to the original wire
					for (WireSegment ws : divwire.getSegments()) {
						wire.insertSegment(ws);
					}
				
					// remove divided wire
					try {
						wires.removeWire(divname);
					} catch (UnknownKeyException e) {
						throw new IllegalStateException("Could not remove wire '" + divname
								+ "' that exists.", e);
					}
				}
				
				// assign null to divided set, as nothing divided exists
				command.divided = null;
				
				return null;
			}
		};
		
		/**
		 * 
		 * @return
		 * Vraca null ako je sve dobro proslo.
		 */
		public abstract ICommandResponse undoHousework(ISchemaInfo info, DeleteSegmentAndDivideCommand command);
	}
	

	/* static fields */
	public static final String COMMAND_NAME = "DeleteSegmentAndDivideCommand";

	
	/* private fields */
	private Caseless wirename;
	private WireSegment segment;
	private Set<Caseless> divided;
	private Set<UnplugWrapper> unplugged;
	private Map<Caseless, IntList> renamedmappings;
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
		} else { // divide wire into 2 or more wires if necessary
			FindDisjointSegments findsegs = new FindDisjointSegments(wirename);
			IQueryResult result = findsegs.performQuery(info);
			
			if (!result.isSuccessful()) {
				wire.insertSegment(segment);
				return new CommandResponse(new SchemaError(EErrorTypes.INNER_QUERY_NOT_PERFORMED,
						"Could not analyze disjoint sets to find potential wire divisions."));
			}
			
			divided = new HashSet<Caseless>();
			DisjointSets dissets = (DisjointSets)result.get(FindDisjointSegments.KEY_SEGMENT_SETS);
			for (int i = 1, sz = dissets.segmentsets.size(); i < sz; i++) {
				// create a new wire
				Caseless divname = AutoRenamer.getFreeName(wirename, wires.getWireNames());
				divided.add(divname);
				ISchemaWire nwire = new SchemaWire(divname);
				
				// transform wire segments to the new wire
				for (WireSegment ws : dissets.segmentsets.get(i)) {
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
				
				// find components mapped to oldwire and remap them to new wire
				renamedmappings = new HashMap<Caseless, IntList>();
				for (PlacedComponent plc : info.getComponents()) {
					IntList changedports = null;
					int count = -1;
					for (SchemaPort sp : plc.comp.getSchemaPorts()) {
						count++;
						Caseless mappedto = sp.getMapping();
						if (Caseless.isNullOrEmpty(mappedto)) continue;
						/* check if port was mapped to old name */
						if (mappedto.equals(wirename)) {
							/* check if wire has segments at the position */
							XYLocation sploc = sp.getOffset();
							if (nwire.segmentsAt(plc.pos.x + sploc.x, plc.pos.y + sploc.y) != null) {
								/* set new mapping */
								sp.setMapping(divname);
								/* cache mapped port for undo */
								if (changedports == null) changedports = new IntList();
								changedports.add(count);
							}
						}
					}
					if (changedports != null) {
						renamedmappings.put(plc.comp.getName(), changedports);
					}
				}
				
				actionstate = EActionState.divided;
			}
		}
		
		// check if segment was plugged into any component and unplug wire if necessary
		ISchemaComponentCollection components = info.getComponents();
		XYLocation start = segment.getStart(), end = segment.getEnd();
		unplugged = new HashSet<UnplugWrapper>();
		unplugAt(start, components);
		unplugAt(end, components);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	private void unplugAt(XYLocation loc, ISchemaComponentCollection components) {
		ISchemaComponent cmp = components.fetchComponent(loc.x, loc.y, 1);
		
		if (cmp == null) return;
		if (cmp.isInvalidated()) return;
		
		XYLocation cloc = components.getComponentLocation(cmp.getName());
		int count = 0;
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			XYLocation portloc = sp.getOffset();
			if ((cloc.x + portloc.x) == loc.x && (cloc.y + portloc.y) == loc.y) {
				if (!wirename.equals(sp.getMapping())) continue;
				sp.setMapping(null);
				unplugged.add(new UnplugWrapper(cmp.getName(), count));
			}
			count++;
		}
	}
	
	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		// plug wire back into any component it may have been plugged into
		ISchemaComponentCollection components = info.getComponents();
		for (UnplugWrapper uw : unplugged) {
			ISchemaComponent cmp = components.fetchComponent(uw.compname);
			cmp.getSchemaPort(uw.schemaportindex).setMapping(wirename);
		}
		
		// perform housework
		ICommandResponse resp = actionstate.undoHousework(info, this);
		if (resp != null && !resp.isSuccessful()) return resp;
		
		// fetch wire and insert the segment back
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		wire.insertSegment(segment);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
}









