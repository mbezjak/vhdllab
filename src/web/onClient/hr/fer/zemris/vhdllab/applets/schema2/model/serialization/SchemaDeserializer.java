package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.SchemaPortWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaWire;

import java.io.Reader;

import org.apache.commons.digester.Digester;





public class SchemaDeserializer {
	/* static fields */

	
	/* private fields */

	
	
	/* ctors */

	public SchemaDeserializer() {
	}
	
	
	
	/* methods */
	
	public SchemaInfo deserializeSchema(Reader reader) {
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
		
		digester.addCallMethod("schemaInfo/wires/schemaWire/drawerName", "setDrawer", 1);
		digester.addCallParam("schemaInfo/wires/schemaWire/drawerName", 0);
		
		digester.addObjectCreate("schemaInfo/wires/schemaWire/parameters/parameter", ParameterWrapper.class);
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/paramClassName", "paramClassName");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/generic", "generic");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/paramType", "paramType");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/name", "name");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/value", "value");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/valueType", "valueType");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/allowedValues", "allowedValues");
		digester.addBeanPropertySetter("schemaInfo/wires/schemaWire/parameters/parameter/eventName", "eventName");
		digester.addSetNext("schemaInfo/wires/schemaWire/parameters/parameter", "addParameter");
		
		digester.addSetNext("schemaInfo/wires/schemaWire", "addWire");
		
		
		// components
		digester.addObjectCreate("schemaInfo/components/schemaComponent", ComponentWrapper.class);
		
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/componentClassName", "componentClassName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/x", "x");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/y", "y");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/width", "width");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/height", "height");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/componentName", "componentName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/codeFileName", "codeFileName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/categoryName", "categoryName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/drawerName", "drawerName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/genericComponent", "genericComponent");
		
		digester.addObjectCreate("schemaInfo/components/schemaComponent/parameters/parameter", ParameterWrapper.class);
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/paramClassName", "paramClassName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/generic", "generic");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/paramType", "paramType");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/name", "name");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/value", "value");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/valueType", "valueType");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/allowedValues", "allowedValues");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/parameters/parameter/eventName", "eventName");
		digester.addSetNext("schemaInfo/components/schemaComponent/parameters/parameter", "addParameterWrapper");
		
		digester.addObjectCreate("schemaInfo/components/schemaComponent/portList/port", PortWrapper.class);
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/name", "name");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/direction", "direction");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/orientation", "orientation");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/type", "type");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/vectorAscension", "vectorAscension");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/lowerBound", "lowerBound");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/upperBound", "upperBound");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/portList/port/relations", "relations");
		digester.addSetNext("schemaInfo/components/schemaComponent/portList/port", "addPortWrapper");
		
		digester.addObjectCreate("schemaInfo/components/schemaComponent/schemaPortList/schemaPort", SchemaPortWrapper.class);
		digester.addCallMethod("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/x", "setXOffset", 1);
		digester.addCallParam("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/x", 0);
		digester.addCallMethod("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/y", "setYOffset", 1);
		digester.addCallParam("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/y", 0);
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/name", "stringName");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/mapping", "stringMapping");
		digester.addBeanPropertySetter("schemaInfo/components/schemaComponent/schemaPortList/schemaPort/portIndex", "portindex");
		digester.addSetNext("schemaInfo/components/schemaComponent/schemaPortList/schemaPort", "addSchemaPort");
		
		digester.addSetNext("schemaInfo/components/schemaComponent", "addComponent");
		
		
		// entity
		digester.addObjectCreate("schemaInfo/entity", EntityWrapper.class);
		
		digester.addObjectCreate("schemaInfo/entity/parameters/parameter", ParameterWrapper.class);
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/paramClassName", "paramClassName");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/generic", "generic");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/paramType", "paramType");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/name", "name");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/value", "value");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/valueType", "valueType");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/allowedValues", "allowedValues");
		digester.addBeanPropertySetter("schemaInfo/entity/parameters/parameter/eventName", "eventName");
		digester.addSetNext("schemaInfo/entity/parameters/parameter", "addParameterWrapper");
		
//		digester.addObjectCreate("schemaInfo/entity/portList/port", PortWrapper.class);
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/name", "name");
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/direction", "direction");
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/type", "type");
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/vectorAscension", "vectorAscension");
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/lowerBound", "lowerBound");
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/upperBound", "upperBound");
//		digester.addBeanPropertySetter("schemaInfo/entity/portList/port/relations", "relations");
//		digester.addSetNext("schemaInfo/entity/portList/port", "addPortWrapper");
		
		digester.addSetNext("schemaInfo/entity", "setEntityFromWrapper");
		
		
		
		
		try {
			return (SchemaInfo)digester.parse(reader);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
















