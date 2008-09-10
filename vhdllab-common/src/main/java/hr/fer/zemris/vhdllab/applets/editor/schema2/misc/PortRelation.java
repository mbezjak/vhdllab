package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;

import java.util.ArrayList;
import java.util.List;

public class PortRelation {
	public Port port;
	public EOrientation orientation;
	public List<SchemaPort> relatedTo;

	public PortRelation(Port p, EOrientation orient) {
		port = p;
		orientation = orient;
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
