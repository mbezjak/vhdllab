package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map.Entry;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.vhdl.model.Port;









public class SchemaSerializer {
	
	/* static fields */
	
	
	
	
	/* ctors */
	
	public SchemaSerializer() {
	}
	
	
	
	
	/* methods */
	
	public void serializeSchema(Writer writer, ISchemaInfo info) throws IOException {
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
		
		writer.append("<schemaInfo>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
		
		serializeEntity(writer, info.getEntity());
		serializeComponents(writer, info.getComponents());
		serializeWires(writer, info.getWires());
		
		writer.append("</schemaInfo>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
	}
	
	private final void appendLine(Writer writer) throws IOException {
		writer.append('\n'); // not platform independent :(
	}
	
	private void serializeEntity(Writer writer, ISchemaEntity entity) throws IOException {
		writer.append("<entity>");
		appendLine(writer);
		appendLine(writer);
		
		writer.append("<parameters>");
		appendLine(writer);
		for (Entry<String, IParameter> entry : entity.getParameters()) {
			serializeParameter(writer, entry.getValue());
		}
		writer.append("</parameters>");
		appendLine(writer);
		appendLine(writer);
		
		writer.append("<portList>");
		appendLine(writer);
		for (Port port : entity.getPorts()) {
			serializePort(writer, port);
		}
		writer.append("</portList>");
		appendLine(writer);
		appendLine(writer);
		
		writer.append("</entity>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
	}


	private void serializeComponents(Writer writer, ISchemaComponentCollection components) throws IOException {
		writer.append("<components>");
		appendLine(writer);
		appendLine(writer);
		
		for (ISchemaComponent component : components) {
			serializeComponent(writer, component, 0, 0);
		}
		
		writer.append("</components>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
	}
	
	
	private void serializeComponent(Writer writer, ISchemaComponent component, int x, int y) throws IOException {
		writer.append("<schemaComponent>");
		appendLine(writer);
		
		writer.append("<componentClassName>").append(component.getClass().getName()).append("</componentClassName>");
		appendLine(writer);
		writer.append("<x>").append(Integer.toString(x)).append("</x>");
		appendLine(writer);
		writer.append("<y>").append(Integer.toString(y)).append("</y>");
		appendLine(writer);
		
		writer.append("</schemaComponent>");
		appendLine(writer);
		appendLine(writer);
	}




	private void serializeWires(Writer writer, ISchemaWireCollection wires) throws IOException {
		writer.append("<wires>");
		appendLine(writer);
		appendLine(writer);
		
		for (ISchemaWire wire : wires) {
			serializeWire(writer, wire);
		}
		
		writer.append("</wires>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
	}
	
	private void serializeWire(Writer writer, ISchemaWire wire) throws IOException {
		writer.append("<schemaWire>");
		appendLine(writer);
		
		
		
		writer.append("</schemaWire>");
		appendLine(writer);
		appendLine(writer);
	}




	private void serializeParameter(Writer writer, IParameter parameter) throws IOException {
		writer.append("<parameter>");
		appendLine(writer);
		
		
		
		writer.append("</parameter>");
		appendLine(writer);
	}
	
	
	private void serializePort(Writer writer, Port port) throws IOException {
		writer.append("<port>");
		appendLine(writer);
		
		
		
		writer.append("</port>");
		appendLine(writer);
	}
	
	
	
}






















