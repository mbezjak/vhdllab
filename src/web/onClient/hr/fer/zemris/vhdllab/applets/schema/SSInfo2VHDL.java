package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingComponentEnvelope;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire.WireConnection;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SSInfo2VHDL {
	private StringBuilder sb = null;
	private SchemaSerializableInformation info;
	private Map<String, HashMap<Integer, String>> mapping;
	private Set<String> compTypeSet;
	
	public String generateVHDLFromSerializableInfo(SchemaSerializableInformation ssInfo) {
		sb = new StringBuilder();
		info = ssInfo;
		mapping = new HashMap<String, HashMap<Integer, String>>();
		compTypeSet = new HashSet<String>();
		
		appendEntityBlock();
		appendEmptyRows();
		appendArchitecturalBlock();
		
		return sb.toString();
	}

	private void appendEntityBlock() {
		sb.append("ENTITY ").append(info.getCircuitInterface().getEntityName()).append(" IS\n");
		sb.append("\tPORT\n\t\t(\n");
		
		// proiterirati circuit interface - srediti portove
		boolean firstEntry = true;
		Type porttip = null;
		List<Port> portlist = info.getCircuitInterface().getPorts();
		for (Port port : portlist) {
			if (firstEntry) {
				firstEntry = false;
			} else {
				sb.append(";\n");
			}
			porttip = port.getType();
			sb.append("\t\t").append(port.getName()).append("\t: ");
			sb.append(port.getDirection()).append("\t");
			sb.append(porttip.getTypeName());
			if (porttip.isScalar()) {
			} else if (porttip.isVector()) {
				sb.append("(").append(porttip.getRangeFrom()).append(" ");
				sb.append(porttip.getVectorDirection()).append(" ");
				sb.append(porttip.getRangeTo()).append(")");
			}
		}
		
		sb.append("\n\t\t);\nEND ").append(info.getCircuitInterface().getEntityName()).append(";");
	}
	
	private void appendEmptyRows() {
		sb.append("\n\n\n");
	}
	
	private void appendArchitecturalBlock() {
		sb.append("ARCHITECTURE structural OF ").append(info.getCircuitInterface().getEntityName()).append(" IS\n");
		
		// proiterirati kroz zice i stvoriti na temelju njih signale
		appendAndMapComponents();
		mapSignalsToComponents();
		appendSignals();
		appendComponentSignals();
		
		sb.append("\nBEGIN\n");
		
		appendMappings();
		
		sb.append("\nEND structural;\n");
	}
	
	private void appendAndMapComponents() {
		List<SchemaDrawingComponentEnvelope> envlist = info.getEnvelopeList();
		for (SchemaDrawingComponentEnvelope env : envlist) {
			AbstractSchemaComponent comp = env.getComponent();
			String compTypeName = comp.getComponentName();
			
			if (!compTypeSet.contains(compTypeName)) {
				compTypeSet.add(compTypeName);
				if (comp.hasComponentBlock()) {
					sb.append("COMPONENT ").append(compTypeName).append('\n');
					sb.append(comp.getEntityBlock());
					sb.append("\nEND COMPONENT;\n");
				}
			}
			
			mapping.put(comp.getComponentInstanceName(), new HashMap<Integer, String>());
		}
	}
	
	private void appendSignals() {
		List<AbstractSchemaWire> wirelist = info.wireList;
		for (AbstractSchemaWire wire : wirelist) {
			sb.append("SIGNAL\t").append(wire.getWireName()).append("\t: " + wire.getSignalType() + ";\n");
		}
	}
	
	private void appendComponentSignals() {
		List<SchemaDrawingComponentEnvelope> envlist = info.getEnvelopeList();
		for (SchemaDrawingComponentEnvelope env : envlist) {
			AbstractSchemaComponent comp = env.getComponent();
			String sigs = comp.getAdditionalSignals();
			if (sigs != null) sb.append(sigs).append('\n');
		}
	}
	
	private void mapSignalsToComponents() {
		List<AbstractSchemaWire> wirelist = info.getWireList();
		for (AbstractSchemaWire wire : wirelist) {
			for (WireConnection conn : wire.connections) {
				mapping.get(conn.componentInstanceName).put(conn.portIndex, wire.getWireName());
			}
		}
	}
	
	private void appendMappings() {
		List<SchemaDrawingComponentEnvelope> envlist = info.getEnvelopeList();
		for (SchemaDrawingComponentEnvelope env : envlist) {
			AbstractSchemaComponent comp = env.getComponent();
			sb.append(comp.getMapping(mapping.get(comp.getComponentInstanceName()))).append('\n');
		}
	}
}











