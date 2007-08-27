package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
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
import hr.fer.zemris.vhdllab.applets.schema2.misc.IntList;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;




/**
 * Povezuje dvije zice skupa. Pritom jedna zica nestaje sa sheme
 * i svi portovi na koje je bila povezana nisu vise povezani na nju,
 * vec na zicu s kojom se spojila.
 * 
 * @author brijest
 *
 */
public class BindWireCommand implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = "BindWireCommand";

	
	
	/* private fields */
	private Caseless wtobind, tobindto;
	private Map<Caseless, IntList> cachedports;
	private List<WireSegment> cachedsegments;
	private ISchemaWire cachedwire;

	
	
	/* ctors */
	
	/**
	 * Stvara komandu koja ce spojiti dvije zice u jednu.
	 * Pritom novostvorena zica ima ime jedne od zica.
	 * Svi portovi komponenata na koje su bile spojene zice bit ce nakon
	 * izvodenja komande spojeni na novonastalu zicu.
	 * 
	 * @param wireToBind
	 * Zica koja ce nestati - bit ce pretvorena u drugu zicu.
	 * @param wireToBeBoundTo
	 * Zica na koju ce prethodna zica biti povezana.
	 */
	public BindWireCommand(Caseless wireToBind, Caseless wireToBeBoundTo) {
		wtobind = wireToBind;
		tobindto = wireToBeBoundTo;
		
		cachedports = new HashMap<Caseless, IntList>();
		cachedsegments = new ArrayList<WireSegment>();
	}

	
	
	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaWire oldwire = info.getWires().fetchWire(wtobind);
		ISchemaWire tobindtowire = info.getWires().fetchWire(tobindto);
		
		if (oldwire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire with name '" + wtobind + "' does not exist."));
		if (tobindtowire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire with name '" + tobindto + "' does not exist."));
		
		// find all components mapped to oldwire, map them to tobeboundwire and cache ports
		for (PlacedComponent placed : info.getComponents()) {
			IntList portindices = null;
			int i = 0;
			for (SchemaPort sp : placed.comp.getSchemaPorts()) {
				Caseless mappedto = sp.getMapping();
				if (wtobind.equals(mappedto)) {
					if (portindices == null) portindices = new IntList();
					portindices.add(i);
					sp.setMapping(tobindto);
				}
				i++;
			}
			if (portindices != null) cachedports.put(placed.comp.getName(), portindices);
		}
		
		// cache old wire
		cachedwire = oldwire;
		
		// remove old wire
		try {
			info.getWires().removeWire(wtobind);
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Wire '" + wtobind.toString() + "' could not be removed, but it exists!", e);
		}
		
		// add all segments from old wire to tobindtowire, caching them
		for (WireSegment ws : oldwire.getSegments()) {
			tobindtowire.insertSegment(ws);
			cachedsegments.add(ws);
		}
		
		// add all nodes from old wire to tobintowire if they can be added, caching them
//		List<XYLocation> nodelist = tobindtowire.getNodes();
//		for (XYLocation node : oldwire.getNodes()) {
//			if (!(nodelist.contains(node))) {
//				nodelist.add(node);
//				cachednodes.add(node);
//			}
//		}
		
		return new CommandResponse(true);
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException
	{
		ISchemaWire oldwire = cachedwire;
		ISchemaWire tobindtowire = info.getWires().fetchWire(tobindto);
		
		if (oldwire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire with name '" + wtobind + "' does not exist."));
		if (tobindtowire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire with name '" + tobindto + "' does not exist."));
		
		// remove cached segments from tobindtowire
		for (WireSegment ws : cachedsegments) {
			tobindtowire.removeSegment(ws);
		}
		
		// remove cached node from tobindtowire
//		List<XYLocation> nodelist = tobindtowire.getNodes();
//		for (XYLocation node : cachednodes) {
//			nodelist.remove(node);
//		}
		
		// add old wire
		try {
			info.getWires().addWire(oldwire);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("Wire '" + oldwire.getName() + "' could not be added due to duplicate key.");
		} catch (OverlapException e) {
			throw new IllegalStateException("Wire '" + oldwire.getName() + "' could not be added due to overlap.");
		}
		
		// find all cached ports and map them to old wire
		ISchemaComponentCollection components = info.getComponents();
		for (Entry<Caseless, IntList> entry : cachedports.entrySet()) {
			ISchemaComponent cmp = components.fetchComponent(entry.getKey());
			List<SchemaPort> splist = cmp.getSchemaPorts();
			IntList portindices = entry.getValue();
			int sz = portindices.size();
			
			for (int i = 0; i < sz; i++) {
				splist.get(portindices.get(i)).setMapping(wtobind);
			}
		}
		
		// clear cache
		cachedports.clear();
		cachedsegments.clear();
		cachedwire = null;
		
		return new CommandResponse(true);
	}

}




















