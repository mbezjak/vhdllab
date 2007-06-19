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
}
