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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidatedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PortRelation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.drawers.InvalidatedDrawer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.GenericParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.events.NameChanger;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic.Orientation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.SchemaPortWrapper;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class InvalidatedComponent implements ISchemaComponent {
	
    protected class PortIterator implements Iterator<Port> {
		private Iterator<PortRelation> prit = ports.iterator();
		public boolean hasNext() {
			return prit.hasNext();
		}
		public Port next() {
			return prit.next().port;
		}
		public void remove() {
			prit.remove();
		}
	}
	
	
	/* static fields */
	private static final Caseless INVALIDATED_COMPONENT = new Caseless("INVALIDATED");
	

	/* private fields */
	private IParameterCollection parameters;
	private InvalidatedDrawer drawer;
	private List<SchemaPort> schemaports, ro_schemaports;
	private List<PortRelation> ports;
	private int width, height;
	

	/* ctors */
	
	/**
	 * Iskljucivo za deserijalizaciju.
	 */
	public InvalidatedComponent(ComponentWrapper wrapper) {
		deserialize(wrapper);
	}
	
	public InvalidatedComponent(String name) {
		width = 50;
		height = 100;
		init(name);
	}
	
	public InvalidatedComponent(String name, int w, int h) {
		width = w;
		height = h;
		init(name);
	}
	

	/* methods */

	private void init(String name) {
		drawer = new InvalidatedDrawer(this);
		parameters = new SchemaParameterCollection();
		schemaports = new ArrayList<SchemaPort>();
		ro_schemaports = Collections.unmodifiableList(schemaports);
		ports = new ArrayList<PortRelation>();
		
		initDefaultParameters(name);
	}
	
	/**
	 * Stvara 2 defaultna parametra - ime (koje je predano metodi)
	 * i orijentaciju.
	 * 
	 * @param name
	 */
	protected void initDefaultParameters(String name) {
		// default parameter - name
		CaselessParameter cslpar =
			new CaselessParameter(ISchemaComponent.KEY_NAME, false, new Caseless(name));
		cslpar.setParameterEvent(new NameChanger());
		parameters.addParameter(cslpar);
		
		// default parameter - component orientation
		GenericParameter<Orientation> orientpar =
			new GenericParameter<Orientation>(ISchemaComponent.KEY_ORIENTATION, false,
					new Orientation());
		orientpar.getConstraint().setPossibleValues(Orientation.allAllowed);
		parameters.addParameter(orientpar);
	}

	public ISchemaComponent copyCtor() {
		return new InvalidatedComponent(getName().toString(), width, height);
	}

	public void deserialize(ComponentWrapper compwrap) {
		// basic
		width = compwrap.getWidth();
		height = compwrap.getHeight();
		
		// drawer
		drawer = new InvalidatedDrawer(this);
		
		// parameters
		deserializeParams(compwrap.getParamWrappers());
		
		// ports
		deserializePorts(compwrap.getPortWrappers());
		
		// physical schema ports
		deserializeSchemaPorts(compwrap.getSchemaPorts());
	}
	
	protected void deserializeParams(List<ParameterWrapper> params) {
		parameters = new SchemaParameterCollection();
		if (params != null) {
			for (ParameterWrapper parwrap : params) {
				IParameter par = ParameterFactory.createParameter(parwrap);
				parameters.addParameter(par);
			}
		}
	}
	
	private void deserializePorts(List<PortWrapper> portwrappers) {
		ports = new ArrayList<PortRelation>();
		
		Port port;
		PortRelation portrel;
		for (PortWrapper portwrap : portwrappers) {
			port = PortFactory.createPort(portwrap);
			portrel = new PortRelation(port, EOrientation.parse(portwrap.getOrientation()));
			ports.add(portrel);
		}
	}
	
	private void deserializeSchemaPorts(List<SchemaPortWrapper> portwrappers) {
		schemaports = new ArrayList<SchemaPort>();
		for (SchemaPortWrapper spw : portwrappers) {
			SchemaPort sp = new SchemaPort(spw);
			sp.setPortindex(spw.getPortindex());
			schemaports.add(sp);
		}
		for (int i = 0, sz = schemaports.size(); i < sz; i++) {
			SchemaPort sp = schemaports.get(i);
			if (sp.getPortindex() != SchemaPort.NO_PORT) ports.get(sp.getPortindex()).relatedTo.add(sp);
		}
	}

	public String getCategoryName() {
		return Constants.Categories.OTHER;
	}

	public CircuitInterface getCircuitInterface() {
		CircuitInterface ci = new CircuitInterface(INVALIDATED_COMPONENT.toString());
		for (PortRelation pr : ports) {
		    ci.add(new Port(pr.port));
		}
		return ci;
	}

	public String getCodeFileName() {
		return null;
	}

	public EOrientation getComponentOrientation() {
		try {
			return ((Orientation) (parameters.getParameter(ISchemaComponent.KEY_ORIENTATION).getValue())).orientation;
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Orientation parameter not found.");
		}
	}

	public EComponentType getComponentType() {
		return EComponentType.NON_BASIC;
	}

	public IComponentDrawer getDrawer() {
		return drawer;
	}

	public int getHeight() {
		return height;
	}

	public Caseless getName() {
		try {
			return (Caseless) parameters.getParameter(ISchemaComponent.KEY_NAME).getValue();
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Name parameter not found in parameters.");
		}
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public Port getPort(int index) {
		return ports.get(index).port;
	}

	public EOrientation getPortOrientation(int index) {
		return ports.get(index).orientation;
	}

	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		int ind = SMath.calcClosestPort(xoffset, yoffset, dist, schemaports);
		if (ind == SMath.ERROR) return null;
		return schemaports.get(ind);
	}

	public SchemaPort getSchemaPort(int index) {
		if (index < 0 || index >= schemaports.size())
			return null;
		return schemaports.get(index);
	}

	public SchemaPort getSchemaPort(Caseless name) {
		for (SchemaPort sp : schemaports) {
			if (sp.getName().equals(name)) return sp;
		}
		return null;
	}

	public List<SchemaPort> getSchemaPorts() {
		return ro_schemaports;
	}

	public List<SchemaPort> getRelatedTo(int portIndex) {
		return new ArrayList<SchemaPort>();
	}

	public Caseless getTypeName() {
		return INVALIDATED_COMPONENT;
	}

	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		return null;
	}

	public int getWidth() {
		return width;
	}

	public boolean isGeneric() {
		return false;
	}

	public boolean isInvalidated() {
		return true;
	}

	public int portCount() {
		return ports.size();
	}

	public Iterator<Port> portIterator() {
		return new PortIterator();
	}

	public int schemaPortCount() {
		return schemaports.size();
	}

	public Iterator<SchemaPort> schemaPortIterator() {
		return schemaports.iterator();
	}

	public void setComponentOrientation(EOrientation orient) {
		throw new InvalidatedException("Invalidated components cannot be modified.");
	}

	public void setName(Caseless name) {
		throw new InvalidatedException("Invalidated components cannot be modified.");
	}

	public void setPort(int index, Port port) {
		throw new InvalidatedException("Invalidated components cannot be modified.");
	}

}














