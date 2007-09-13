package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.util.List;




/**
 * Na temelju info objekta ISchemaCorea generira
 * VHDL kod.
 * Koristi se unutar SchemaVHDLGeneratora.
 * 
 * @author Axel
 *
 */
public class SchemaInfo2VHDL {
	StringBuilder sb;
	ISchemaInfo info;
	CircuitInterface circint;
	
	/**
	 * Generira VHDL kod.
	 * 
	 * @param info
	 * @return
	 */
	public String generateVHDL(ISchemaInfo schemaInfo) {
		if (!isValid(schemaInfo)) return null;
		
		sb = new StringBuilder();
		info = schemaInfo;
		circint = info.getEntity().getCircuitInterface(schemaInfo);
		
		appendHeader();
		appendEntityBlock();
		appendArchitecturalBlock("structural");
		
		return sb.toString();
	}
	
	private boolean isValid(ISchemaInfo info) {
		for (PlacedComponent plc : info.getComponents()) {
			// find invalidated components
			if (plc.comp.isInvalidated()) return false;
			
			// check if inout
			if (plc.comp.getComponentType().equals(EComponentType.IN_OUT)) continue;
			
			// find empty ports with IN direction
			for (int i = 0, sz = plc.comp.schemaPortCount(); i < sz; i++) {
				SchemaPort sp = plc.comp.getSchemaPort(i);
				Caseless mapping = sp.getMapping();
				if (Caseless.isNullOrEmpty(mapping)) {
					Port p = plc.comp.getPort(sp.getPortindex());
					if (p.getDirection().isIN()) return false;
				}
			}
		}
		
		return true;
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
			
			sb.append(provider.getInstantiation(info));
			sb.append('\n');
		}
		
		sb.append("END ARCHITECTURE ").append(archName).append(";\n");
	}
		
}











