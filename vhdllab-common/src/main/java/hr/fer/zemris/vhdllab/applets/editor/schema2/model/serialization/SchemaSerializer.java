/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISerializable;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;









public class SchemaSerializer {
	
	/* static fields */
	
	
	/* private fields */
	private Map<String, String> shortcutmap;
	private int sccount;
	
	/* ctors */
	
	public SchemaSerializer() {
		shortcutmap = new HashMap<String, String>();
	}
	
	
	
	
	/* methods */
	
	public void serializeSchema(Writer writer, ISchemaInfo info) throws IOException {
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
		
		writer.append("<schemaInfo_v2>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
		
		serializeShortcuts(writer, info);
		serializeEntity(writer, info.getEntity());
		serializeComponents(writer, info.getComponents());
		serializeWires(writer, info.getWires());
		
		writer.append("</schemaInfo_v2>");
		appendLine(writer);
		appendLine(writer);
		appendLine(writer);
	}
	
	private void serializeParametersInShortcutTable(Writer writer, IParameterCollection params)
		throws IOException
	{
		for (IParameter param : params) {
			String parname = param.getClass().getName();
			if (!shortcutmap.containsKey(parname)) {
				String shortcut = "p" + (++sccount);
				shortcutmap.put(parname, shortcut);
				writer.append("<s>");
				writer.append(shortcut + ShortcutTable.SPLIT_SYMBOL + parname);
				writer.append("</s>");
				appendLine(writer);
			}
			
			IParameterEvent event = param.getParameterEvent();
			if (event != null) {
				String evname = event.getClass().getName();
				if (!shortcutmap.containsKey(evname)) {
					String shortcut = "e" + (++sccount);
					shortcutmap.put(evname, shortcut);
					writer.append("<s>");
					writer.append(shortcut + ShortcutTable.SPLIT_SYMBOL + evname);
					writer.append("</s>");
					appendLine(writer);
				}
			}
			
			String valuename = param.getValue().getClass().getName();
			if (!shortcutmap.containsKey(valuename)) {
				String shortcut = "v" + (++sccount);
				shortcutmap.put(valuename, shortcut);
				writer.append("<s>");
				writer.append(shortcut + ShortcutTable.SPLIT_SYMBOL + valuename);
				writer.append("</s>");
				appendLine(writer);
			}
		}
	}
	
	private void serializeShortcuts(Writer writer, ISchemaInfo info) throws IOException {
		// create shortcut tag
		writer.append("<shortcuts>");
		appendLine(writer);
		appendLine(writer);
		
		// save drawer names, event names, and parameter names
		sccount = 0;
		shortcutmap.clear();
		for (ISchemaWire w : info.getWires()) {
			// drawer
			String drawername = w.getDrawer().getClass().getName();
			if (!shortcutmap.containsKey(drawername)) {
				String shortcut = "d" + (++sccount);
				shortcutmap.put(drawername, shortcut);
				writer.append("<s>");
				writer.append(shortcut + ShortcutTable.SPLIT_SYMBOL + drawername);
				writer.append("</s>");
				appendLine(writer);
			}
			
			// parameter names and events
			serializeParametersInShortcutTable(writer, w.getParameters());
		}
		for (PlacedComponent plc : info.getComponents()) {
			// component classname
			String cmpclsname = plc.comp.getClass().getName();
			if (!shortcutmap.containsKey(cmpclsname)) {
				String shortcut = "c" + (++sccount);
				shortcutmap.put(cmpclsname, shortcut);
				writer.append("<s>");
				writer.append(shortcut + ShortcutTable.SPLIT_SYMBOL + cmpclsname);
				writer.append("</s>");
				appendLine(writer);
			}
			
			// drawer
			String drawername = plc.comp.getDrawer().getClass().getName();
			if (!shortcutmap.containsKey(drawername)) {
				String shortcut = "d" + (++sccount);
				shortcutmap.put(drawername, shortcut);
				writer.append("<s>");
				writer.append(shortcut + ShortcutTable.SPLIT_SYMBOL + drawername);
				writer.append("</s>");
				appendLine(writer);
			}
			
			// parameter names and events
			serializeParametersInShortcutTable(writer, plc.comp.getParameters());
		}
		
		writer.append("</shortcuts>");
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
		
		writer.append("<params>");
		appendLine(writer);
		for (IParameter param : entity.getParameters()) {
			serializeParameter(writer, param);
		}
		writer.append("</params>");
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
	
	private String rename(String name) {
		String shortcutname = shortcutmap.get(name);
		return shortcutname;
	}
	
	private void serializeComponent(Writer writer, ISchemaComponent component, int x, int y) throws IOException {
		writer.append("<comp>");
		appendLine(writer);
		
		writer.append("<cls>").append(rename(component.getClass().getName())).append("</cls>");
		appendLine(writer);
		writer.append("<x>").append(Integer.toString(x)).append("</x>");
		appendLine(writer);
		writer.append("<y>").append(Integer.toString(y)).append("</y>");
		appendLine(writer);
		try {
			writer.append("<name>").append(component.getTypeName().toString()).append("</name>");
		} catch (NullPointerException e) {
			System.err.println("Kemija ga: " + component.getName() + ", tip=" + component.getTypeName());
			e.printStackTrace();
			throw e;
		}
		appendLine(writer);
		writer.append("<filenm>").append(component.getCodeFileName()).append("</filenm>");
		appendLine(writer);
		writer.append("<cat>").append(component.getCategoryName()).append("</cat>");
		appendLine(writer);
		writer.append("<drawer>").append(rename(component.getDrawer().getClass().getName())).append("</drawer>");
		appendLine(writer);
		writer.append("<generic>").append(Boolean.toString(component.isGeneric())).append("</generic>");
		appendLine(writer);
		writer.append("<wdt>").append(Integer.toString(component.getWidth())).append("</wdt>");
		appendLine(writer);
		writer.append("<hgt>").append(Integer.toString(component.getHeight())).append("</hgt>");
		appendLine(writer);
		writer.append("<params>");
		appendLine(writer);
		for (IParameter param : component.getParameters()) {
			serializeParameter(writer, param);
		}
		writer.append("</params>");
		appendLine(writer);
		writer.append("<ports>");
		appendLine(writer);
		for (int i = 0, sz = component.portCount(); i < sz; i++) {
			serializePort(writer, component.getPort(i), component.getPortOrientation(i));
		}
		writer.append("</ports>");
		appendLine(writer);
		writer.append("<schports>");
		appendLine(writer);
		Iterator<SchemaPort> spit = component.schemaPortIterator();
		while (spit.hasNext()) {
			serializeSchemaPort(writer, spit.next());
		}
		writer.append("</schports>");
		appendLine(writer);
		
		writer.append("</comp>");
		appendLine(writer);
		appendLine(writer);
	}

	private void serializeSchemaPort(Writer writer, SchemaPort port) throws IOException {
		writer.append("<sp>");
		appendLine(writer);
		
		XYLocation portoff = port.getOffset();
		writer.append("<x>").append(Integer.toString(portoff.x)).append("</x>");
		appendLine(writer);
		writer.append("<y>").append(Integer.toString(portoff.y)).append("</y>");
		appendLine(writer);
		writer.append("<nm>").append(port.getName().toString()).append("</nm>");
		appendLine(writer);
		writer.append("<mp>").append((port.getMapping() != null) ? (port.getMapping().toString()) : "").append("</mp>");
		appendLine(writer);
		writer.append("<p>").append(Integer.toString(port.getPortindex())).append("</p>");
		appendLine(writer);
		
		writer.append("</sp>");
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
		writer.append("<wire>");
		appendLine(writer);
		
//		writer.append("<nodes>");
//		appendLine(writer);
//		for (XYLocation node : wire.getNodes()) {
//			writer.append("<node>");
//			writer.append("<x>").append(Integer.toString(node.x)).append("</x>");
//			writer.append("<y>").append(Integer.toString(node.y)).append("</y>");
//			writer.append("</node>");
//			appendLine(writer);
//		}
//		writer.append("</nodes>");
//		appendLine(writer);
		
		writer.append("<segs>");
		appendLine(writer);
		for (WireSegment segment : wire.getSegments()) {
			writer.append("<seg>");
			appendLine(writer);
			writer.append("<x1>").append(Integer.toString(segment.getStart().x)).append("</x1>");
			writer.append("<y1>").append(Integer.toString(segment.getStart().y)).append("</y1>");
			appendLine(writer);
			writer.append("<x2>").append(Integer.toString(segment.getEnd().x)).append("</x2>");
			writer.append("<y2>").append(Integer.toString(segment.getEnd().y)).append("</y2>");
			appendLine(writer);
			writer.append("</seg>");
			appendLine(writer);
		}
		writer.append("</segs>");
		appendLine(writer);
		
		writer.append("<params>");
		appendLine(writer);
		for (IParameter param : wire.getParameters()) {
			serializeParameter(writer, param);
		}
		writer.append("</params>");
		appendLine(writer);
		
		writer.append("</wire>");
		appendLine(writer);
		appendLine(writer);
	}




	private void serializeParameter(Writer writer, IParameter parameter) throws IOException {
		writer.append("<par>");
		appendLine(writer);
		
		writer.append("<cls>").append(rename(parameter.getClass().getName())).append("</cls>");
		appendLine(writer);
		writer.append("<gen>").append(Boolean.toString(parameter.isGeneric())).append("</gen>");
		appendLine(writer);
		writer.append("<pt>").append(parameter.getType().toString()).append("</pt>");
		appendLine(writer);
		writer.append("<name>").append(parameter.getName()).append("</name>");
		appendLine(writer);
		EParamTypes tp = parameter.getType();
		if (tp == EParamTypes.OBJECT) {
			ISerializable serializable_object = (ISerializable)parameter.getValue();
			writer.append("<val>").append(serializable_object.serialize()).append("</val>");
		} else {
			writer.append("<val>").append(parameter.getValue().toString()).append("</val>");
		}
		appendLine(writer);
		writer.append("<vt>").append(rename(parameter.getValue().getClass().getName())).append("</vt>");
		appendLine(writer);
		Set<Object> allowed = parameter.getConstraint().getPossibleValues();
		writer.append("<allowed>");
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
		writer.append("</allowed>");
		appendLine(writer);
		writer.append("<event>").append((parameter.getParameterEvent() != null) 
				? (rename(parameter.getParameterEvent().getClass().getName())) : ("")).append("</event>");
		appendLine(writer);
		
		writer.append("</par>");
		appendLine(writer);
	}
	
	
	private void serializePort(Writer writer, Port port, EOrientation orient) throws IOException {
		writer.append("<port>");
		appendLine(writer);
		
		writer.append("<nm>").append(port.getName()).append("</nm>");
		appendLine(writer);
		writer.append("<dir>").append(port.getDirection().toString()).append("</dir>");
		appendLine(writer);
		writer.append("<or>").append(orient.toString()).append("</or>");
		appendLine(writer);
		writer.append("<tp>").append(port.getTypeName()).append("</tp>");
		appendLine(writer);
		if (port.isScalar()) {
			writer.append("<va></va>");
			appendLine(writer);
			writer.append("<lo></lo>");
			appendLine(writer);
			writer.append("<hi></hi>");
			appendLine(writer);
		} else {
			writer.append("<va>").append(fromVecDir(port)).append("</va>");
			appendLine(writer);
			writer.append("<lo>").append(Integer.toString(port.getFrom())).append("</lo>");
			appendLine(writer);
			writer.append("<hi>").append(Integer.toString(port.getTo())).append("</hi>");
			appendLine(writer);
		}
		
		writer.append("</port>");
		appendLine(writer);
	}

	private String fromVecDir(Port port) {
	    return port.isTO() ? PortWrapper.ASCEND : PortWrapper.DESCEND;
	}
	
	
}






















