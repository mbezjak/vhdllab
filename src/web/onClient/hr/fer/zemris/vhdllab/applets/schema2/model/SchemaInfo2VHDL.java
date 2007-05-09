package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
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
		info = schemaInfo;
		sb = new StringBuilder();
		circint = info.getEntity().getCircuitInterface();
		
		appendEntityBlock();
		appendArchitecturalBlock("structural");
		
		return sb.toString();
	}
	
	private void appendEntityBlock() {
		Port p;
		Type pt;
		int b1, b2;
		
		sb.append("ENTITY ").append(circint.getEntityName()).append(" IS\n");
		sb.append("\tPORT(\n");
		
		List<Port> ports = circint.getPorts();
		for (int i = 0; i < ports.size(); i++) {
			p = ports.get(i);
			sb.append("\t\t").append(p.getName()).append(":\t").append(p.getDirection());
			pt = p.getType();
			if (pt.isScalar()) {
				sb.append(pt.getTypeName());
			}
			if (pt.isVector()) {
				sb.append(pt.getTypeName()).append('(');
				b1 = (pt.hasVectorDirectionTO()) ? (pt.getRangeFrom()) : (pt.getRangeTo());
				b2 = (pt.hasVectorDirectionTO()) ? (pt.getRangeTo()) : (pt.getRangeFrom());
				sb.append(b1).append(' ').append(pt.getVectorDirection()).append(' ').append(b2).append(")");
			}
			if (i != (ports.size() - 1)) sb.append(';');
			sb.append('\n');
		}
		
		sb.append("\t);\n");
		sb.append("END ENTITY ").append(circint.getEntityName()).append(";\n\n\n\n");
	}
	
	private void appendArchitecturalBlock(String archName) {
		sb.append("ARCHITECTURE ").append(archName).append(" OF ").append(circint.getEntityName());
		sb.append(" IS\n");
		
		IVHDLSegmentProvider provider = null;
		
		// pripremiti
		for (Caseless name : info.getComponents().getComponentNames()) {
			ISchemaComponent comp = info.getComponents().fetchComponent(name);
			
			provider = comp.getVHDLSegmentProvider();
			if (provider == null) continue;
			
			sb.append(provider.getSignalDefinitions());
			sb.append('\n');
		}
		
		sb.append("BEGIN\n");
		
		// mapirati
		for (Caseless name : info.getComponents().getComponentNames()) {
			ISchemaComponent comp = info.getComponents().fetchComponent(name);
			
			provider = comp.getVHDLSegmentProvider();
			
			sb.append(provider.getInstantiation());
			sb.append('\n');
		}
		
		sb.append("END ARCHITECTURE ").append(archName).append(";\n");
	}
		
}











