package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.SchemaPortWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IGenericValue;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PortRelation;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.GenericParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.generic.Orientation;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;









/**
 * Klasa modelira komponentu koja predstavlja
 * ulaz ili izlaz na modeliranom sklopu.
 * 
 * @author brijest
 *
 */
public class InOutSchemaComponent implements ISchemaComponent {
	
	
	private class InOutVHDLSegmentProvider implements IVHDLSegmentProvider {
		public String getInstantiation(ISchemaInfo info) {
			StringBuilder sb = new StringBuilder();
			
			if (portrel.port.getType().isScalar()) {
				Caseless mappedto = schemaports.get(0).getMapping();
				if (!Caseless.isNullOrEmpty(mappedto)) {
					Direction dir = portrel.port.getDirection();
					if (dir.equals(Direction.IN)) {
						sb.append(mappedto.toString()).append(" <= ");
						sb.append(portrel.port.getName()).append(";\n");
					} else if (dir.equals(Direction.OUT)) {
						sb.append(portrel.port.getName()).append(" <= ");
						sb.append(mappedto.toString()).append(";\n");
					} else {
						throw new NotImplementedException("Direction '" + dir.toString() + "' not implemented.");
					}
				}
			} else {
				Direction dir = portrel.port.getDirection();
				if (dir.equals(Direction.IN)) {
					int i = 0;
					for (SchemaPort schport : schemaports) {
						Caseless mappedto = schport.getMapping();
						if (!Caseless.isNullOrEmpty(mappedto)) {
							sb.append(mappedto.toString()).append(" <= ");
							sb.append(portrel.port.getName()).append("(").append(i).append(");\n");
						}
						i++;
					}
				} else if (dir.equals(Direction.OUT)) {
					int i = 0;
					for (SchemaPort schport : schemaports) {
						Caseless mappedto = schport.getMapping();
						if (!Caseless.isNullOrEmpty(mappedto)) {
							sb.append(portrel.port.getName()).append("(").append(i).append(")");
							sb.append(" <= ").append(mappedto.toString()).append('\n');
						}
						i++;
					}
				} else {
					throw new NotImplementedException("Direction '" + dir.toString() + "' not implemented.");
				}
				
			}
			
			return sb.toString();
		}
		public String getSignalDefinitions(ISchemaInfo info) {
			return "";
		}
	}
	
	public static class ParamPort implements IGenericValue {
		private DefaultPort port;
		public IGenericValue copyCtor() {
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}
		public void deserialize(String code) {
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}
		public String serialize() {
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}
	}
	
	
	
	/* static fields */
	private static final int WIDTH = Constants.GRID_SIZE * 4;
	private static final int HEIGHT_PER_PORT = Constants.GRID_SIZE;
	private static final int EDGE_OFFSET = Constants.GRID_SIZE * 2;
	
	
	/* private fields */
	private PortRelation portrel;
	private List<SchemaPort> schemaports;
	private IParameterCollection parameters;
	private IComponentDrawer drawer;
	private int width, height;
	
	
	
	/* ctors */
	
	public InOutSchemaComponent(Port modelledPort) {
		parameters = new SchemaParameterCollection();
		portrel = new PortRelation(modelledPort);
		schemaports = new ArrayList<SchemaPort>();
		width = WIDTH;
		
		initDefaultParameters();
		buildSchemaPorts();
	}

	private void buildSchemaPorts() {
		Type tp = portrel.port.getType();
		Direction dir = portrel.port.getDirection();
		if (dir.equals(Direction.IN)) {
			if (tp.isScalar()) {
				buildInStdLogic();
			} else {
				buildStdLogicVector(width);
			}
		} else if (dir.equals(Direction.OUT)) {
			if (tp.isScalar()) {
				buildOutStdLogic();
			} else {
				buildStdLogicVector(0);
			}
		} else {
			throw new NotImplementedException("Direction '" + dir.toString() + "' not implemented.");
		}
	}
	
	private void buildInStdLogic() {
		SchemaPort sp = new SchemaPort(width, EDGE_OFFSET + HEIGHT_PER_PORT / 2,
				new Caseless(portrel.port.getName()));
		
		sp.setPortindex(0);
		portrel.relatedTo.add(sp);
		schemaports.add(sp);
		
		height = (EDGE_OFFSET + HEIGHT_PER_PORT) * 2;
	}
	
	private void buildOutStdLogic() {
		SchemaPort sp = new SchemaPort(0, EDGE_OFFSET + HEIGHT_PER_PORT / 2,
				new Caseless(portrel.port.getName()));
		
		sp.setPortindex(0);
		portrel.relatedTo.add(sp);
		schemaports.add(sp);
		
		height = (EDGE_OFFSET + HEIGHT_PER_PORT) * 2;
	}
	
	private void buildStdLogicVector(int xpos) {
		Type tp = portrel.port.getType();
		int from = tp.getRangeFrom(), to = tp.getRangeTo();
		
		int j;
		SchemaPort schport = null;
		if (tp.hasVectorDirectionTO()) {
			j = 0;
			for (int i = from; i <= to; i++, j++) {
				schport = new SchemaPort(xpos, EDGE_OFFSET + HEIGHT_PER_PORT * j + HEIGHT_PER_PORT / 2,
						new Caseless(portrel.port.getName() + "_" + i));
				
				schport.setPortindex(0);
				portrel.relatedTo.add(schport);
				schemaports.add(schport);
			}
		} else {
			j = 0;
			for (int i = to; i <= from; i++, j++) {
				schport = new SchemaPort(xpos, EDGE_OFFSET + HEIGHT_PER_PORT * j + HEIGHT_PER_PORT / 2,
						new Caseless(portrel.port.getName() + "_" + i));
				
				schport.setPortindex(0);
				portrel.relatedTo.add(schport);
				schemaports.add(schport);
			}
		}
		
		height = EDGE_OFFSET * 2 + HEIGHT_PER_PORT * (j + 1);
	}
	
	private void initDefaultParameters() {
		// default parameter - name
		CaselessParameter cslpar =
			new CaselessParameter(ISchemaComponent.KEY_NAME, false, new Caseless(portrel.port.getName())); // TODO: add event
		parameters.addParameter(cslpar);
		
		// default parameter - component orientation
		GenericParameter<Orientation> orientpar =
			new GenericParameter<Orientation>(ISchemaComponent.KEY_ORIENTATION, false,
					new Orientation());
		parameters.addParameter(orientpar);
		
		// default parameter - component port
	}
	
	private void initDrawer(String drawerName) {
		try {
			Class cls = Class.forName(drawerName);
			Class[] partypes = new Class[1];
			partypes[0] = ISchemaComponent.class;
			Constructor<IComponentDrawer> ct = cls.getConstructor(partypes);
			Object[] params = new Object[1];
			params[0] = this;
			drawer = ct.newInstance(params);
		} catch (Exception e) {
			drawer = new DefaultComponentDrawer(this);
		}
	}

	/**
	 * Za deserijalizaciju.
	 * 
	 * @param wrapper
	 */
	public InOutSchemaComponent(ComponentWrapper wrapper) {
		schemaports = new ArrayList<SchemaPort>();
		parameters = new SchemaParameterCollection();
		
		deserialize(wrapper);
	}
	
	/**
	 * Za copyCtor().
	 *
	 */
	private InOutSchemaComponent() {
		parameters = new SchemaParameterCollection();
		schemaports = new ArrayList<SchemaPort>();
	}
	
	
	
	
	
	
	
	/* methods */

	public ISchemaComponent copyCtor() {
		InOutSchemaComponent inout = new InOutSchemaComponent();

		// copy parameters
		for (IParameter param : this.parameters) {
			inout.parameters.addParameter(param.copyCtor());
		}
		
		// copy schema ports
		for (SchemaPort sp : this.schemaports) {
			inout.schemaports.add(new SchemaPort(sp));
		}
		
		// copy port relation
		inout.portrel = new PortRelation(this.portrel.port);
		for (SchemaPort sp : inout.schemaports) {
			inout.portrel.relatedTo.add(sp);
		}
		
		// init drawer
		if (this.drawer != null) inout.initDrawer(this.drawer.getClass().getName());
		
		// other
		inout.width = this.width;
		inout.height = this.height;
		
		return inout;
	}

	public void deserialize(ComponentWrapper compwrap) {
		// handle parameters
		parameters.clear();
		for (ParameterWrapper paramwrap : compwrap.getParamWrappers()) {
			parameters.addParameter(ParameterFactory.createParameter(paramwrap));
		}
		
		// handle port
		List<PortWrapper> ports = compwrap.getPortWrappers();
		if (ports.size() < 1) throw new IllegalStateException("At least one port needed for in-out component.");
		Port p = PortFactory.createPort(ports.get(0));
		portrel = new PortRelation(p);
		
		// create schema ports
		schemaports.clear();
		for (SchemaPortWrapper spw : compwrap.getSchemaPorts()) {
			SchemaPort sp = new SchemaPort(spw);
			portrel.relatedTo.add(sp);
			schemaports.add(sp);
		}
		
		// init drawer
		initDrawer(compwrap.getDrawerName());
		
		// other
		width = compwrap.getWidth();
		height = compwrap.getHeight();
	}

	public String getCategoryName() {
		return Constants.Categories.INOUT;
	}

	public CircuitInterface getCircuitInterface() {
		DefaultPort port = new DefaultPort(portrel.port.getName(),
				portrel.port.getDirection(), portrel.port.getType());
		
		return new DefaultCircuitInterface(getName().toString(), port);
	}

	public String getCodeFileName() {
		return null;
	}

	public EOrientation getComponentOrientation() {
		throw new NotImplementedException();
	}

	public IComponentDrawer getDrawer() {
		return drawer;
	}

	public int getHeight() {
		return height;
	}

	public Caseless getName() {
		try {
			return (Caseless)(parameters.getValue(ISchemaComponent.KEY_NAME));
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Name parameter not found.");
		}
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public SchemaPort getSchemaPort(Caseless name) {
		for (SchemaPort sp : schemaports) {
			if (sp.getName().equals(name)) return sp;
		}
		return null;
	}

	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		int ind = SMath.calcClosestPort(xoffset, yoffset, dist, schemaports);
		if (ind == -1) return null;
		return schemaports.get(ind);
	}

	public SchemaPort getSchemaPort(int index) {
		return schemaports.get(index);
	}

	public List<SchemaPort> getSchemaPorts() {
		return schemaports;
	}

	public Caseless getTypeName() {
		return new Caseless(Constants.TypeNames.IN_OUT_COMPONENT);
	}

	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		return new InOutVHDLSegmentProvider();
	}

	public int getWidth() {
		return width;
	}

	public boolean isGeneric() {
		return false;
	}

	public int schemaPortCount() {
		return schemaports.size();
	}

	public Iterator<Port> portIterator() {
		return new Iterator<Port>() {
			private boolean first = true;
			public boolean hasNext() {
				return first;
			}
			public Port next() {
				if (first) {
					first = false;
					return portrel.port;
				} else {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public Iterator<SchemaPort> schemaPortIterator() {
		return schemaports.iterator();
	}

	public void setComponentOrientation(EOrientation orient) {
		throw new NotImplementedException();
	}

	public void setName(Caseless name) {
		try {
			parameters.setValue(ISchemaComponent.KEY_NAME, name);
		} catch (InvalidParameterValueException e) {
			throw new RuntimeException("Name could not be set - invalid value.", e);
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Name could not be set.", e);
		}
	}
	
	public EComponentType getComponentType() {
		return EComponentType.IN_OUT;
	}
	
}
















