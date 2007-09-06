package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.SchemaPortWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidatedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PortRelation;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.InvalidatedDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.GenericParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.events.NameChanger;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.generic.Orientation;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class InvalidatedComponent implements ISchemaComponent {
	
	private class PortIterator implements Iterator<Port> {
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
	private static final Caseless INVALIDATED_COMPONENT = new Caseless("Invalidated component");
	

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
		List<Port> ciports = new ArrayList<Port>();
		for (PortRelation pr : ports) {
			ciports.add(new DefaultPort(pr.port.getName(), pr.port.getDirection(), pr.port.getType()));
		}
		return new DefaultCircuitInterface(INVALIDATED_COMPONENT.toString(), ciports);
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














