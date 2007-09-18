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
import hr.fer.zemris.vhdllab.vhdl.MessageType;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

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
	 * 
	 * @param info
	 * @return
	 */
	public VHDLGenerationResult generateVHDL(ISchemaInfo schemaInfo) {		
		info = schemaInfo;

		VHDLGenerationResult validres = isValid();
		if (validres != null) return validres;
		
		sb = new StringBuilder();
		circint = info.getEntity().getCircuitInterface(schemaInfo);
		renamedsignals = new HashMap<Caseless, Caseless>();
		
		findRedundantSignals();
		
		appendHeader();
		appendEntityBlock();
		appendArchitecturalBlock("structural");
		
		return new VHDLGenerationResult(sb.toString());
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
					Direction dir = origin.getDirection();
					WireMapping wm = wiremap.get(mappedto);
					
					if (dir.isIN()) {
						wm.ioins++;
						if (wm.ioins == 1) wm.inSingleName = getPortWithIndexName(plc, origin, p);
					} else if (dir.isOUT()) {
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
					Direction dir = origin.getDirection();
					WireMapping wm = wiremap.get(mappedto);
					
					if (dir.isIN()) {
						wm.normins++;
					} else if (dir.isOUT()) {
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

	private Caseless getPortWithIndexName(PlacedComponent plc, Port origin, SchemaPort schport) {
		Type tp = origin.getType();
		
		if (tp.isScalar()) {
			return new Caseless(origin.getName());
		} else {
			List<SchemaPort> schemaports = plc.comp.getRelatedTo(schport.getPortindex());
			int schindex = 0;
			for (SchemaPort sp : schemaports) {
				if (sp.equals(schport)) break;
				schindex++;
			}
			if (tp.hasVectorDirectionTO()) {
				schindex += tp.getRangeFrom();
			} else {
				schindex = tp.getRangeTo() - schindex;
			}
			return new Caseless(origin.getName() + "(" + schindex + ")");
		}
	}

	private VHDLGenerationResult isValid() {
		for (PlacedComponent plc : info.getComponents()) {
			// find invalidated components
			if (plc.comp.isInvalidated()) {
				List<VHDLGenerationMessage> errors = new ArrayList<VHDLGenerationMessage>();
				errors.add(new VHDLGenerationMessage(info.getEntity().getName().toString(), 
						"Component '" + plc.comp.getName() + "' is invalidated and must be replaced.",
						MessageType.ERROR));
				return new VHDLGenerationResult(0, false, errors, null);
			}
			
			// check if inout
			if (plc.comp.getComponentType().equals(EComponentType.IN_OUT)) continue;
			
			// find empty ports with IN direction
			List<VHDLGenerationMessage> errors = new ArrayList<VHDLGenerationMessage>();
			for (int i = 0, sz = plc.comp.schemaPortCount(); i < sz; i++) {
				SchemaPort sp = plc.comp.getSchemaPort(i);
				Caseless mapping = sp.getMapping();
				if (Caseless.isNullOrEmpty(mapping)) {
					Port p = plc.comp.getPort(sp.getPortindex());
					if (p.getDirection().isIN()) {
						errors.add(new VHDLGenerationMessage(info.getEntity().getName().toString(), 
								"Component '" + plc.comp.getName() + "' has a port '" +
								p.getName() + "' with direction IN that is not connected to a signal.",
								MessageType.ERROR));
					}
				}
			}
			if (!errors.isEmpty()) {
				return new VHDLGenerationResult(0, false, errors, null);
			}
		}
		
		return null;
	}

	private void appendHeader() {
		sb.append("library ieee;\nuse ieee.std_logic_1164.all;\n\n");
	}

	private void appendEntityBlock() {
		Port p;
		Type pt;
		int b1, b2;
		
		List<Port> ports = circint.getPorts();
		
		sb.append("ENTITY ").append(circint.getEntityName()).append(" IS\n");		
		
		if (!ports.isEmpty()) {
			sb.append("\tPORT(\n");
			
			for (int i = 0; i < ports.size(); i++) {
				p = ports.get(i);
				sb.append("\t\t").append(p.getName()).append(": ").append(p.getDirection()).append(' ');
				pt = p.getType();
				if (pt.isScalar()) {
					sb.append(pt.getTypeName());
				}
				if (pt.isVector()) {
					sb.append(pt.getTypeName()).append('(');
					b1 = pt.getRangeFrom(); //(pt.hasVectorDirectionTO()) ? (pt.getRangeFrom()) : (pt.getRangeTo());
					b2 = pt.getRangeTo(); //(pt.hasVectorDirectionTO()) ? (pt.getRangeTo()) : (pt.getRangeFrom());
					sb.append(b1).append(' ').append(pt.getVectorDirection()).append(' ').append(b2).append(")");
				}
				if (i != (ports.size() - 1)) sb.append(';');
				sb.append('\n');
			}
			sb.append("\t);\n");
		}
		
		sb.append("END ENTITY ").append(circint.getEntityName()).append(";\n\n\n\n");
	}
	
	private void appendArchitecturalBlock(String archName) {
		sb.append("ARCHITECTURE ").append(archName).append(" OF ").append(circint.getEntityName());
		sb.append(" IS\n");
		
		IVHDLSegmentProvider provider = null;
		
		// prepare signals
		for (ISchemaWire wire : info.getWires()) {
			/* if signal is redundant do not declare it */
			if (renamedsignals.containsKey(wire.getName().toString())) continue;
			
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











