package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISerializable;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;









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
		for (IParameter param : entity.getParameters()) {
			serializeParameter(writer, param);
		}
		writer.append("</parameters>");
		appendLine(writer);
		appendLine(writer);
		
//		writer.append("<portList>");
//		appendLine(writer);
//		for (Port port : entity.getPorts()) {
//			serializePort(writer, port);
//		}
//		writer.append("</portList>");
//		appendLine(writer);
//		appendLine(writer);
		
		writer.append("</entity>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
	}


	private void serializeComponents(Writer writer, ISchemaComponentCollection components) throws IOException {
		writer.append("<components>");
		appendLine(writer);
		appendLine(writer);
		
		for (PlacedComponent placed : components) {
			serializeComponent(writer, placed.comp, placed.pos.x, placed.pos.y);
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
		try {
			writer.append("<componentName>").append(component.getTypeName().toString()).append("</componentName>");
		} catch (NullPointerException e) {
			System.err.println("Kemija ga: " + component.getName() + ", tip=" + component.getTypeName());
			e.printStackTrace();
			throw e;
		}
		appendLine(writer);
		writer.append("<codeFileName>").append(component.getCodeFileName()).append("</codeFileName>");
		appendLine(writer);
		writer.append("<categoryName>").append(component.getCategoryName()).append("</categoryName>");
		appendLine(writer);
		writer.append("<drawerName>").append(component.getDrawer().getClass().getName()).append("</drawerName>");
		appendLine(writer);
		writer.append("<genericComponent>").append(Boolean.toString(component.isGeneric())).append("</genericComponent>");
		appendLine(writer);
		writer.append("<width>").append(Integer.toString(component.getWidth())).append("</width>");
		appendLine(writer);
		writer.append("<height>").append(Integer.toString(component.getHeight())).append("</height>");
		appendLine(writer);
		writer.append("<parameters>");
		appendLine(writer);
		for (IParameter param : component.getParameters()) {
			serializeParameter(writer, param);
		}
		writer.append("</parameters>");
		appendLine(writer);
		writer.append("<portList>");
		appendLine(writer);
		Iterator<Port> portit = component.portIterator();
		while (portit.hasNext()) {
			serializePort(writer, portit.next());
		}
		writer.append("</portList>");
		appendLine(writer);
		writer.append("<schemaPortList>");
		appendLine(writer);
		Iterator<SchemaPort> spit = component.schemaPortIterator();
		while (spit.hasNext()) {
			serializeSchemaPort(writer, spit.next());
		}
		writer.append("</schemaPortList>");
		appendLine(writer);
		
		writer.append("</schemaComponent>");
		appendLine(writer);
		appendLine(writer);
	}

	private void serializeSchemaPort(Writer writer, SchemaPort port) throws IOException {
		writer.append("<schemaPort>");
		appendLine(writer);
		
		XYLocation portoff = port.getOffset();
		writer.append("<x>").append(Integer.toString(portoff.x)).append("</x>");
		appendLine(writer);
		writer.append("<y>").append(Integer.toString(portoff.y)).append("</y>");
		appendLine(writer);
		writer.append("<name>").append(port.getName().toString()).append("</name>");
		appendLine(writer);
		writer.append("<mapping>").append((port.getMapping() != null) ? (port.getMapping().toString()) : "").append("</mapping>");
		appendLine(writer);
		writer.append("<portIndex>").append(Integer.toString(port.getPortindex())).append("</portIndex>");
		appendLine(writer);
		
		writer.append("</schemaPort>");
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
		
		writer.append("<nodes>");
		appendLine(writer);
		for (XYLocation node : wire.getNodes()) {
			writer.append("<node>");
			writer.append("<x>").append(Integer.toString(node.x)).append("</x>");
			writer.append("<y>").append(Integer.toString(node.y)).append("</y>");
			writer.append("</node>");
			appendLine(writer);
		}
		writer.append("</nodes>");
		appendLine(writer);
		
		writer.append("<segments>");
		appendLine(writer);
		for (WireSegment segment : wire.getSegments()) {
			writer.append("<segment>");
			appendLine(writer);
			writer.append("<x1>").append(Integer.toString(segment.getStart().x)).append("</x1>");
			writer.append("<y1>").append(Integer.toString(segment.getStart().y)).append("</y1>");
			appendLine(writer);
			writer.append("<x2>").append(Integer.toString(segment.getEnd().x)).append("</x2>");
			writer.append("<y2>").append(Integer.toString(segment.getEnd().y)).append("</y2>");
			appendLine(writer);
			writer.append("</segment>");
			appendLine(writer);
		}
		writer.append("</segments>");
		appendLine(writer);
		
		writer.append("<parameters>");
		appendLine(writer);
		for (IParameter param : wire.getParameters()) {
			serializeParameter(writer, param);
		}
		writer.append("</parameters>");
		appendLine(writer);
		
		writer.append("</schemaWire>");
		appendLine(writer);
		appendLine(writer);
	}




	private void serializeParameter(Writer writer, IParameter parameter) throws IOException {
		writer.append("<parameter>");
		appendLine(writer);
		
		writer.append("<paramClassName>").append(parameter.getClass().getName()).append("</paramClassName>");
		appendLine(writer);
		writer.append("<generic>").append(Boolean.toString(parameter.isGeneric())).append("</generic>");
		appendLine(writer);
		writer.append("<paramType>").append(parameter.getType().toString()).append("</paramType>");
		appendLine(writer);
		writer.append("<name>").append(parameter.getName()).append("</name>");
		appendLine(writer);
		EParamTypes tp = parameter.getType();
		if (tp == EParamTypes.OBJECT) {
			ISerializable serializable_object = (ISerializable)parameter.getValue();
			writer.append("<value>").append(serializable_object.serialize()).append("</value>");
		} else {
			writer.append("<value>").append(parameter.getValue().toString()).append("</value>");
		}
		appendLine(writer);
		writer.append("<valueType>").append(parameter.getValue().getClass().getName()).append("</valueType>");
		appendLine(writer);
		Set<Object> allowed = parameter.getConstraint().getPossibleValues();
		writer.append("<allowedValues>");
		if (allowed != null) {
			if (tp == EParamTypes.OBJECT) {
				ISerializable serializable_object;
				for (Object obj : allowed) {
					serializable_object = (ISerializable)obj;
					writer.append(serializable_object.serialize()).append(Constants.ALLOWED_SET_DIVIDER);
				}
			} else {
				for (Object obj : allowed) {
					writer.append(obj.toString()).append(' ');
				}
			}
		}
		writer.append("</allowedValues>");
		appendLine(writer);
		writer.append("<eventName>").append((parameter.getParameterEvent() != null) 
				? (parameter.getParameterEvent().getClass().getName()) : ("")).append("</eventName>");
		appendLine(writer);
		
		writer.append("</parameter>");
		appendLine(writer);
	}
	
	
	private void serializePort(Writer writer, Port port) throws IOException {
		writer.append("<port>");
		appendLine(writer);
		
		writer.append("<name>").append(port.getName()).append("</name>");
		appendLine(writer);
		writer.append("<direction>").append(port.getDirection().toString()).append("</direction>");
		appendLine(writer);
		Type pt = port.getType();
		writer.append("<type>").append(pt.getTypeName()).append("</type>");
		appendLine(writer);
		if (pt.isScalar()) {
			writer.append("<vectorAscension></vectorAscension>");
			appendLine(writer);
			writer.append("<lowerBound></lowerBound>");
			appendLine(writer);
			writer.append("<upperBound></upperBound>");
			appendLine(writer);
		} else {
			writer.append("<vectorAscension>").append(fromVecDir(pt.getVectorDirection())).append("</vectorAscension>");
			appendLine(writer);
			writer.append("<lowerBound>").append(Integer.toString(pt.getRangeFrom())).append("</lowerBound>");
			appendLine(writer);
			writer.append("<upperBound>").append(Integer.toString(pt.getRangeTo())).append("</upperBound>");
			appendLine(writer);
		}
		
		writer.append("</port>");
		appendLine(writer);
	}

	private String fromVecDir(String vectorDirection) {
		if (vectorDirection.equals(DefaultType.VECTOR_DIRECTION_TO)) return PortWrapper.ASCEND;
		else if (vectorDirection.equals(DefaultType.VECTOR_DIRECTION_DOWNTO)) return PortWrapper.DESCEND;
		else throw new IllegalStateException("Vector direction '" + vectorDirection + "' is unknown.");
	}
	
	
}






















