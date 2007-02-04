package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.ArrayList;
import java.util.List;

public class SSInfo2VHDL {
	private StringBuilder sb = null;
	private SchemaSerializableInformation info;
	
	public String generateVHDLFromSerializableInfo(SchemaSerializableInformation ssInfo) {
		sb = new StringBuilder();
		info = ssInfo;
		
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
		appendComponents();
		appendSignals();
		
		sb.append("\nBEGIN\n");
		
		appendMappings();
		
		sb.append("\nEND structural;\n");
	}
	
	private void appendComponents() {
	}
	
	private void appendSignals() {
		ArrayList<AbstractSchemaWire> wirelist = info.wireList;
		for (AbstractSchemaWire wire : wirelist) {
			sb.append("\tSIGNAL\t").append(wire.getWireName()).append("\t: " + wire.getSignalType() + ";\n");
		}
	}
	
	private void appendMappings() {
	}
}











