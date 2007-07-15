package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.ArrayList;
import java.util.List;

public class PortRelation {
	public Port port;
	public List<SchemaPort> relatedTo;

	public PortRelation(Port p) {
		port = p;
		relatedTo = new ArrayList<SchemaPort>();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof PortRelation)) return false;
		PortRelation pr = (PortRelation)obj;
		return (pr.port.equals(this.port) && pr.relatedTo.equals(this.relatedTo));
	}

	@Override
	public int hashCode() {
		return port.hashCode() << 16 + relatedTo.hashCode();
	}
	
	
}
