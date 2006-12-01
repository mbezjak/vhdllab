package hr.fer.zemris.vhdllab.applets.schema.wires;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;


public class AbstractSchemaWire {
	protected class Connection {
		public Ptr<AbstractSchemaComponent> refComponent;
		public int portIndex;
	}
	
	HashMap<Point, Connection> connections;
}
