package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaWire;

import java.io.InputStream;

import org.apache.commons.digester.Digester;





public class SchemaDeserializer {
	/* static fields */

	
	/* private fields */

	
	
	/* ctors */

	public SchemaDeserializer() {
	}
	
	
	
	/* methods */
	
	public SchemaInfo deserializeSchema(InputStream is) {
		Digester digester = new Digester();
		digester.setValidating(false);
		
		// schema info
		digester.addObjectCreate("schemaInfo", SchemaInfo.class);

		// wires
		digester.addObjectCreate("schemaInfo/wires/schemaWire", SchemaWire.class);
		
		digester.addObjectCreate("schemaInfo/wires/schemaWire/nodes/node", XYLocation.class);
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/nodes/node/x", "x");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/nodes/node/y", "y");
		digester.addSetNext("schemaInfo/wires/schemaWire/nodes/node", "addNode");
		
		digester.addObjectCreate("schemaInfo/wires/schemaWire/segments/segment", WireSegment.class);
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/segments/segment/x1", "x1");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/segments/segment/y1", "y1");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/segments/segment/x2", "x2");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/segments/segment/y2", "y2");
		digester.addSetNext("schemaInfo/wires/schemaWire/segments/segment", "addWireSegment");
		
		digester.addSetNext("schemaInfo/wires/schemaWire", "addWire");
		
		try {
			return (SchemaInfo)digester.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
















