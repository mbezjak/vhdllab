package hr.fer.zemris.vhdllab.applets.schema2.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;





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
//		digester.addObjectCreate("schemaInfo/wires/schemaWire/nodes/node", XYLocation.class);
//		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/nodes/node/x", "x");
//		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/nodes/node/y", "y");
//		digester.addSetNext("schemaInfo/wires/schemaWire/nodes/node", "addNode");
		digester.addSetNext("schemaInfo/wires/schemaWire", "addWire");
		
		try {
			return (SchemaInfo)digester.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
















