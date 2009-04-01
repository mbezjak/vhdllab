package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;




/**
 * Na temelju info objekta ISchemaCorea generira
 * VHDL kod.
 * Koristi se unutar SchemaVHDLGeneratora.
 * 
 * @author Axel
 *
 */
public class SchemaInfo2VHDL {
	
	private static class WireMapping {
		public ISchemaWire wire;
		public Caseless inSingleName, outSingleName;
		public int ioins, ioouts, normins, normouts;
		public WireMapping(ISchemaWire w) {
			wire = w;
			inSingleName = outSingleName = null;
			ioins = ioouts = normins = normouts = 0;
		}
	}
	
	/* private fields */
	private StringBuilder sb;
	private ISchemaInfo info;
	private CircuitInterface circint;
	private Map<Caseless, Caseless> renamedsignals;
	
	/**
	 * Generira VHDL kod.
	 */
	public Result generateVHDL(ISchemaInfo schemaInfo) {		
		info = schemaInfo;

		Result validres = isValid();
		if (validres != null) return validres;
		
		sb = new StringBuilder();
		circint = info.getEntity().getCircuitInterface(schemaInfo);
		renamedsignals = new HashMap<Caseless, Caseless>();
		
		findRedundantSignals();
		
		appendHeader();
		appendEntityBlock();
		appendArchitecturalBlock("structural");
		
		return new Result(sb.toString());
	}
	
	private void findRedundantSignals() {
		/*
		 * redundant signals are:
		 * -signals mapped to one INOUT component that has a Direction.IN port,
		 * 	and mapped to Direction.IN ports of other components, but not
		 * 	mapped to Direction.OUT ports of other components, with the
		 * 	exception of INOUT components with a Direction.OUT port
		 * 	(these are replaced with the name of the INOUT component's Direction.IN port)
		 * -signals mapped to one INOUT component that has a Direction.OUT port,
		 * 	and mapped only to normal components with Direction.OUT ports
		 * 	(these are replaced with the name of the INOUT component's Direction.OUT port)
		 */
		Map<Caseless, WireMapping> wiremap = new HashMap<Caseless, WireMapping>();
		ISchemaWireCollection wires = info.getWires();
		ISchemaComponentCollection components = info.getComponents();
		
		/* add all wires to wiremap */
		for (ISchemaWire w : wires) {
			wiremap.put(w.getName(), new WireMapping(w));
		}
		
		/* build wire mappings */
		for (PlacedComponent plc : components) {
			if (plc.comp.getComponentType().equals(EComponentType.IN_OUT)) {
				for (SchemaPort p : plc.comp.getSchemaPorts()) {
					Caseless mappedto = p.getMapping();
					
					if (Caseless.isNullOrEmpty(mappedto)) continue;
					
					Port origin = plc.comp.getPort(p.getPortindex());
					WireMapping wm = wiremap.get(mappedto);
					
					if (origin.isIN()) {
						wm.ioins++;
						if (wm.ioins == 1) wm.inSingleName = getPortWithIndexName(plc, origin, p);
					} else if (origin.isOUT()) {
						wm.ioouts++;
						if (wm.ioouts == 1) wm.outSingleName = getPortWithIndexName(plc, origin, p);
					} else {
						throw new NotImplementedException("Only IN and OUT directions implemented.");
					}
				}
			} else {
				for (SchemaPort p : plc.comp.getSchemaPorts()) {
					Caseless mappedto = p.getMapping();
					
					if (Caseless.isNullOrEmpty(mappedto)) continue;
					
					Port origin = plc.comp.getPort(p.getPortindex());
					WireMapping wm = wiremap.get(mappedto);
					
					if (origin.isIN()) {
						wm.normins++;
					} else if (origin.isOUT()) {
						wm.normouts++;
					} else {
						throw new NotImplementedException("Only IN and OUT directions implemented.");
					}
				}
			}
		}
		
		/* build renamedsignals map */
		for (Entry<Caseless, WireMapping> entry : wiremap.entrySet()) {
			WireMapping wm = entry.getValue();
			if (wm.ioins == 1 && wm.normouts == 0) {
				renamedsignals.put(entry.getKey(), wm.inSingleName);
				continue;
			}
			if (wm.ioouts == 1 && wm.ioins == 0 && wm.normins == 0) {
				renamedsignals.put(entry.getKey(), wm.outSingleName);
				continue;
			}
		}
	}

	private Caseless getPortWithIndexName(PlacedComponent plc, Port origin,
            SchemaPort schport) {
        if (origin.isScalar()) {
            return new Caseless(origin.getName());
        }
        List<SchemaPort> schemaports = plc.comp.getRelatedTo(schport
                .getPortindex());
        int schindex = 0;
        for (SchemaPort sp : schemaports) {
            if (sp.equals(schport))
                break;
            schindex++;
        }
        if (origin.isTO()) {
            schindex += origin.getFrom();
        } else {
            schindex = origin.getTo() - schindex;
        }
        return new Caseless(origin.getName() + "(" + schindex + ")");
    }

	private Result isValid() {
		for (PlacedComponent plc : info.getComponents()) {
			// find invalidated components
			if (plc.comp.isInvalidated()) {
				List<String> errors = new ArrayList<String>();
				errors.add("Component '" + plc.comp.getName() + "' is invalidated and must be replaced.");
				return new Result(errors);
			}
			
			// check if inout
			if (plc.comp.getComponentType().equals(EComponentType.IN_OUT)) continue;
			
			// find empty ports with IN direction
			List<String> errors = new ArrayList<String>();
			for (int i = 0, sz = plc.comp.schemaPortCount(); i < sz; i++) {
				SchemaPort sp = plc.comp.getSchemaPort(i);
				Caseless mapping = sp.getMapping();
				if (Caseless.isNullOrEmpty(mapping)) {
					Port p = plc.comp.getPort(sp.getPortindex());
					if (p.isIN()) {
						errors.add("Component '" + plc.comp.getName() + "' has a port '" +
								p.getName() + "' with direction IN that is not connected to a signal.");
					}
				}
			}
			if (!errors.isEmpty()) {
				return new Result(errors);
			}
		}
		
		return null;
	}

	private void appendHeader() {
		sb.append("library ieee;\nuse ieee.std_logic_1164.all;\n\n");
	}

	private void appendEntityBlock() {
		sb.append(circint.toString()).append("\n\n\n");
	}
	
	private void appendArchitecturalBlock(String archName) {
		sb.append("ARCHITECTURE ").append(archName).append(" OF ").append(circint.getName());
		sb.append(" IS\n");
		
		IVHDLSegmentProvider provider = null;
		
		// prepare signals
		for (ISchemaWire wire : info.getWires()) {
			/* if signal is redundant do not declare it */
			if (renamedsignals.containsKey(wire.getName())) continue;
			
			/* declare signal */
			sb.append("SIGNAL ").append(wire.getName()).append(": std_logic;\n");
		}
		
		// prepare components
		for (Caseless name : info.getComponents().getComponentNames()) {
			ISchemaComponent comp = info.getComponents().fetchComponent(name);
			
			provider = comp.getVHDLSegmentProvider();
			
			sb.append(provider.getSignalDefinitions(info));
			sb.append('\n');
		}
		
		sb.append("BEGIN\n");
		
		// map everything
		for (Caseless name : info.getComponents().getComponentNames()) {
			ISchemaComponent comp = info.getComponents().fetchComponent(name);
			
			provider = comp.getVHDLSegmentProvider();
			
			sb.append(provider.getInstantiation(info, renamedsignals));
			sb.append('\n');
		}
		
		sb.append("END ARCHITECTURE ").append(archName).append(";\n");
	}
		
}











