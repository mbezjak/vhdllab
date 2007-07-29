package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.SchemaPortWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.AutoRenamer;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.IntList;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PortRelation;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.GenericParameter;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.events.NameChanger;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.generic.Orientation;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.PortFactory;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;







public class DefaultSchemaComponent implements ISchemaComponent {
	
	private class PortIterator implements Iterator<Port> {
		private Iterator<PortRelation> prit = portrelations.iterator();
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
	
	private class DefaultVHDLSegmentProvider implements IVHDLSegmentProvider {
		
		public String getInstantiation(ISchemaInfo info) {
			StringBuilder sb = new StringBuilder();
			
			// bind to helper signals
			int i = 0;
			for (PortRelation portrel : portrelations) {
				Type tp = portrel.port.getType();
				String signame = signames.get(i);
				if (tp.isVector()) {
					int vecpos = 0;
					for (SchemaPort related : portrel.relatedTo) {
						if (!Caseless.isNullOrEmpty(related.getMapping())) {
							sb.append(signame).append('(').append(vecpos).append(')');
							sb.append(" <= ").append(related.getMapping().toString());
							sb.append(";\n");
						}
						vecpos++;
					}
					
				}
				i++;
			}
			
			
			// instantiate
			
			sb.append(getName()).append(": entity work.").append(getTypeName());
			
			// handle generic map
			boolean first = true;
			if (isGeneric()) for (IParameter param : parameters) {
				if (!param.isGeneric()) continue;
				if (first) {
					sb.append(" GENERIC MAP(");
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(param.getName()).append(" => ").append(param.getVHDLGenericEntry());
			}
			if (!first) sb.append(")");
			
			// handle port map
			if (portrelations.size() > 0) {
				sb.append(" PORT MAP(");
				
				first = true;
				i = 0;
				for (PortRelation portrel : portrelations) {
					if (first) first = false; else sb.append(", ");
					sb.append(portrel.port.getName()).append(" => ");
					
					Type tp = portrel.port.getType();
					if (tp.isScalar()) {
						Caseless mappedto = portrel.relatedTo.get(0).getMapping();
						sb.append((!Caseless.isNullOrEmpty(mappedto)) ? (mappedto) : ("open"));
					} else {
						sb.append(signames.get(i));
					}
				}
				i++;
				
				sb.append(')');
			}
			
			sb.append(";\n");
			
			return sb.toString();
		}

		public String getSignalDefinitions(ISchemaInfo info) {
			signames.clear();
			
			StringBuilder sb = new StringBuilder();
			
			int i = 0;
			String hlpsigname = null;
			Set<Caseless> wirenames = info.getWires().getWireNames();
			for (PortRelation portrel : portrelations) {
				Type tp = portrel.port.getType();
				if (tp.isVector()) {
					hlpsigname = AutoRenamer.generateHelpSignalName(getName(), wirenames, i).toString();
					sb.append("SIGNAL ").append(hlpsigname).append(": std_logic_vector(");
					if (tp.hasVectorDirectionTO()) {
						sb.append(tp.getRangeFrom()).append(" TO ").append(tp.getRangeTo());
					} else {
						sb.append(tp.getRangeFrom()).append(" DOWNTO ").append(tp.getRangeTo());
					}
					sb.append(");\n");
				} else hlpsigname = null;
				signames.add(hlpsigname);
				i++;
			}
			
			return sb.toString();
		}
		
	}
	

	/* static fields */
	private static final int WIDTH_PER_PORT = Constants.GRID_SIZE * 2;
	private static final int HEIGHT_PER_PORT = Constants.GRID_SIZE * 2;
	private static final int EDGE_OFFSET = Constants.GRID_SIZE * 4;
	
	

	/* private fields */
	private List<String> signames = new ArrayList<String>();
	private String categoryName;
	private Caseless componentName;
	private String codeFileName;
	private boolean generic;
	private IComponentDrawer drawer;
	private IParameterCollection parameters;
	private List<SchemaPort> schemaports;
	private List<PortRelation> portrelations;
	private int width, height;

	/* protected fields */
	/* none */
	
	

	/* ctors */
	
	/**
	 * Protected ctor, koristi se kod copyCtor-a,
	 * eventualno za nasljedivanje.
	 * 
	 */
	protected DefaultSchemaComponent() {
		parameters = new SchemaParameterCollection();
		schemaports = new ArrayList<SchemaPort>();
		portrelations = new ArrayList<PortRelation>();
	}
	
	/**
	 * Ovaj se konstruktor koristi pri deserijalizaciji.
	 */
	public DefaultSchemaComponent(ComponentWrapper compwrap) {
		deserialize(compwrap);
	}

	/**
	 * @param name
	 *            Jedinstveno ime ove instance komponente.
	 * @param predefComp
	 *            Wrapper za predefinirane komponente.
	 */
	public DefaultSchemaComponent(PredefinedComponent predefComp) {
		// basic properties
		componentName = new Caseless(predefComp.getComponentName());
		codeFileName = predefComp.getCodeFileName();
		categoryName = predefComp.getCategoryName();
		generic = predefComp.isGenericComponent();

		// drawer
		initDrawer(predefComp.getDrawerName());

		// parameters
		initParameters(predefComp.getParameters());

		// ports
		initPorts(predefComp.getPorts());
		
		// add default parameters
		initDefaultParameters(predefComp.getPreferredName());
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

	/**
	 * Stvara portove na temelju PortWrapper-a, te automatski
	 * stvara odgovarajuce pinove za njih.
	 * Pretpostavlja da je uz svaki port navedena odgovarajuca
	 * orijentacija (PortWrapper.getOrientation() ne vraca null ili
	 * prazan string).
	 * 
	 * @param portwrappers
	 */
	private void initPorts(List<PortWrapper> portwrappers) {
		schemaports = new ArrayList<SchemaPort>();
		portrelations = new ArrayList<PortRelation>();
		if (portwrappers == null) return;
		
		SchemaPort schport;
		Type tp;
		Direction dir;
		Port port;
		PortRelation pr;
		int nc = 0, sc = 0, wc = 0, ec = 0, pos = 0, portindex = 0;
		IntList toBeMoved = new IntList();
		for (PortWrapper pw : portwrappers) {
			if (pw.getType().equals(PortWrapper.STD_LOGIC_VECTOR)) {
				int[] range = new int[2];
				range[0] = Integer.parseInt(pw.getLowerBound());
				range[1] = Integer.parseInt(pw.getUpperBound());
				
				String vecdir = toVecDir(pw.getVectorAscension());
				
				tp = new DefaultType(PortWrapper.STD_LOGIC_VECTOR, range, vecdir);
				if (pw.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = Direction.IN;
				else if (pw.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = Direction.OUT;
				else throw new NotImplementedException("Direction '" + pw.getDirection() + "' unknown.");
				
				port = new DefaultPort(pw.getName(), dir, tp);
				pr = new PortRelation(port);
				portrelations.add(pr);
				
				int increment;
				if (pw.getOrientation().equals(PortWrapper.ORIENTATION_NORTH)) {
					increment = createSchPortsFor(tp, pr, EOrientation.NORTH, toBeMoved, nc, pos, portindex);
					nc += increment;
				} else if (pw.getOrientation().equals(PortWrapper.ORIENTATION_SOUTH)) {
					increment = createSchPortsFor(tp, pr, EOrientation.SOUTH, toBeMoved, sc, pos, portindex);
					sc += increment;
				} else if (pw.getOrientation().equals(PortWrapper.ORIENTATION_WEST)) {
					increment = createSchPortsFor(tp, pr, EOrientation.WEST, toBeMoved, wc, pos, portindex);
					wc += increment;
				} else if (pw.getOrientation().equals(PortWrapper.ORIENTATION_EAST)) {
					increment = createSchPortsFor(tp, pr, EOrientation.EAST, toBeMoved, ec, pos, portindex);
					ec += increment;
				} else throw new NotImplementedException("Orientation '" + pw.getOrientation() + "' unknown.");
				
				pos += increment;
				
			} else if (pw.getType().equals(PortWrapper.STD_LOGIC)) {
				tp = new DefaultType(PortWrapper.STD_LOGIC, DefaultType.SCALAR_RANGE,
						DefaultType.SCALAR_VECTOR_DIRECTION);
				if (pw.getDirection().equals(PortWrapper.DIRECTION_IN)) dir = Direction.IN;
				else if (pw.getDirection().equals(PortWrapper.DIRECTION_OUT)) dir = Direction.OUT;
				else throw new NotImplementedException("Direction '" + pw.getDirection() + "' unknown.");
				
				port = new DefaultPort(pw.getName(), dir, tp);
				pr = new PortRelation(port);
				portrelations.add(pr);
				
				if (pw.getOrientation().equals(PortWrapper.ORIENTATION_NORTH)) {
					schport = new SchemaPort(EDGE_OFFSET + nc * WIDTH_PER_PORT, 0,
							new Caseless(port.getName()));
					nc++;
				} else if (pw.getOrientation().equals(PortWrapper.ORIENTATION_SOUTH)) {
					schport = new SchemaPort(EDGE_OFFSET + sc * WIDTH_PER_PORT, 0,
							new Caseless(port.getName()));
					sc++;
					toBeMoved.add(pos);
				} else if (pw.getOrientation().equals(PortWrapper.ORIENTATION_WEST)) {
					schport = new SchemaPort(0, EDGE_OFFSET + wc * HEIGHT_PER_PORT,
							new Caseless(port.getName()));
					wc++;
				} else if (pw.getOrientation().equals(PortWrapper.ORIENTATION_EAST)) {
					schport = new SchemaPort(0, EDGE_OFFSET + ec * HEIGHT_PER_PORT,
							new Caseless(port.getName()));
					ec++;
					toBeMoved.add(pos);
				} else throw new NotImplementedException("Orientation '" + pw.getOrientation() + "' unknown.");
				
				schport.setPortindex(portindex);
				pr.relatedTo.add(schport);
				schemaports.add(schport);
				pos++;
			} else {
				throw new NotImplementedException("Port type '" + pw.getType() + "' is unknown.");
			}
			
			portindex++;
		}
		
		// calculate width and height and set ports appropriately
		width = (((nc > sc) ? (nc) : (sc)) - 1) * WIDTH_PER_PORT + EDGE_OFFSET * 2;
		height = (((wc > ec) ? (wc) : (ec)) - 1) * HEIGHT_PER_PORT + EDGE_OFFSET * 2;
		
		// translate those schema ports (pins) that could be on width or height right away
		pos = toBeMoved.size();
		for (int i = 0; i < pos; i++) {
			schport = schemaports.get(toBeMoved.get(i));
			if (schport.getOffset().x == 0) schport.setXOffset(width);
			else schport.setYOffset(height);
		}
		
		alignPortsToCenterOnEachSide();
	}
	
	private void alignPortsToCenterOnEachSide() {
		// browse through all pins and map pins on different sides
		List<SchemaPort> north = new ArrayList<SchemaPort>(), south = new ArrayList<SchemaPort>();
		List<SchemaPort> west = new ArrayList<SchemaPort>(), east = new ArrayList<SchemaPort>();
		
		for (SchemaPort sp : schemaports) {
			XYLocation offset = sp.getOffset();
			if (offset.x == 0) west.add(sp);
			else if (offset.x == width) east.add(sp);
			else if (offset.y == 0) north.add(sp);
			else if (offset.y == height) south.add(sp);
			else throw new IllegalStateException("A schema port that's not on the border exists.");
		}
		
		
		// center the opposite side with less pins - check if even or odd num of pins
		int sz = west.size(), szopp = east.size(), s;
		List<SchemaPort> tomove = null;
		
		if (sz < szopp) { tomove = west; }
		else if (sz > szopp) { tomove = east; s = szopp; szopp = sz; sz = s; }
		if (tomove != null) {
			// sz = size of the shorter side; szopp = size of the opposite
			if (((sz % 2) == 0 && (szopp % 2) == 0) || (((sz % 2) == 1) && ((szopp % 2) == 1))) {
				// if both are even or odd, translate by half their difference times the port size
				int transl = (szopp - sz) / 2 * HEIGHT_PER_PORT;
				for (SchemaPort sp : tomove)  {
					sp.getOffset().y += transl;
				}
			} else {
				// if one is even and the other one odd, translate by --||-- plus half the port size
				int transl = (szopp - sz) / 2 * HEIGHT_PER_PORT + HEIGHT_PER_PORT / 2;
				for (SchemaPort sp : tomove)  {
					sp.getOffset().y += transl;
				}
			}
		}
		
		tomove = null;
		sz = north.size();
		szopp = south.size();
		if (sz < szopp) { tomove = north; }
		else if (sz > szopp) { tomove = south; s = szopp; szopp = sz; sz = s; }
		if (tomove != null) {
			// sz = size of the shorter side; szopp = size of the opposite
			if (((sz % 2) == 0 && (szopp % 2) == 0) || (((sz % 2) == 1) && ((szopp % 2) == 1))) {
				// if both are even or odd, translate by half their difference times the port size
				int transl = (szopp - sz) / 2 * WIDTH_PER_PORT;
				for (SchemaPort sp : tomove)  {
					sp.getOffset().x += transl;
				}
			} else {
				// if one is even and the other one odd, translate by --||-- plus half the port size
				int transl = (szopp - sz) / 2 * WIDTH_PER_PORT + WIDTH_PER_PORT / 2;
				for (SchemaPort sp : tomove)  {
					sp.getOffset().x += transl;
				}
			}
		}
	}
	
	private final int createSchPortsFor(Type tp, PortRelation pr, EOrientation ori, IntList toBeMoved,
			int stor, int stpos, int portindex)
	{
		int from = tp.getRangeFrom(), to = tp.getRangeTo();
		
		SchemaPort schport = null;
		if (tp.hasVectorDirectionTO()) {
			for (int i = from; i <= to; i++) {
				if (ori == EOrientation.NORTH || ori == EOrientation.SOUTH) {
					schport = new SchemaPort(EDGE_OFFSET + stor * WIDTH_PER_PORT, 0,
							new Caseless(pr.port.getName() + "_" + i));
				}
				if (ori == EOrientation.EAST || ori == EOrientation.WEST) {
					schport = new SchemaPort(0, EDGE_OFFSET + stor * HEIGHT_PER_PORT,
							new Caseless(pr.port.getName() + "_" + i));
				}
				if (ori == EOrientation.EAST || ori == EOrientation.SOUTH) toBeMoved.add(stpos);
				stpos++;
				stor++;
				schport.setPortindex(portindex);
				schemaports.add(schport);
				pr.relatedTo.add(schport);
			}
		} else {
			for (int i = to; i <= from; i++) {
				if (ori == EOrientation.NORTH || ori == EOrientation.SOUTH) {
					schport = new SchemaPort(EDGE_OFFSET + stor * WIDTH_PER_PORT, 0,
							new Caseless(pr.port.getName() + "_" + i));
				}
				if (ori == EOrientation.EAST || ori == EOrientation.WEST) {
					schport = new SchemaPort(0, EDGE_OFFSET + stor * HEIGHT_PER_PORT,
							new Caseless(pr.port.getName() + "_" + i));
				}
				if (ori == EOrientation.EAST || ori == EOrientation.SOUTH) toBeMoved.add(stpos);
				stpos++;
				stor++;
				schport.setPortindex(portindex);
				schemaports.add(schport);
				pr.relatedTo.add(schport);
			}
		}
		
		return Math.abs(to - from) + 1;
	}

	private final String toVecDir(String ascend) {
		if (ascend.equals(PortWrapper.ASCEND)) return DefaultType.VECTOR_DIRECTION_TO;
		else if (ascend.equals(PortWrapper.DESCEND)) return DefaultType.VECTOR_DIRECTION_DOWNTO;
		else throw new NotImplementedException("Ascension '" + ascend + "' unknown.");
	}

	
	/**
	 * Stvara drawer na temelju imena.
	 * 
	 * @param drawerName
	 */
	protected void initDrawer(String drawerName) {
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
	 * Na temelju liste ParameterWrapper-a, stvara parametre.
	 * 
	 * @param params
	 */
	protected void initParameters(List<ParameterWrapper> params) {
		parameters = new SchemaParameterCollection();
		if (params != null) {
			IParameter par;
			for (ParameterWrapper parwrap : params) {
				par = ParameterFactory.createParameter(parwrap);
				parameters.addParameter(par);
			}
		}
	}
	
	
	
	
	
	/* methods */

	public ISchemaComponent copyCtor() {
		DefaultSchemaComponent dsc = new DefaultSchemaComponent();
		
		dsc.componentName = this.componentName;
		dsc.codeFileName = this.codeFileName;
		dsc.categoryName = this.categoryName;
		dsc.generic = this.generic;
		dsc.width = this.width;
		dsc.height = this.height;
		dsc.initDrawer(this.drawer.getClass().getName());
		
		// copy schema ports and port relations
		PortRelation npr;
		SchemaPort nsp;
		for (PortRelation portrel : this.portrelations) {
			npr = new PortRelation(new DefaultPort(portrel.port.getName(),
					portrel.port.getDirection(), portrel.port.getType()));
			for (SchemaPort sp : portrel.relatedTo) {
				nsp = new SchemaPort(sp);
				nsp.setMapping(null);
				npr.relatedTo.add(nsp);
				dsc.schemaports.add(nsp);
			}
			dsc.portrelations.add(npr);
		}
		
		// copy parameters
		for (IParameter param : this.parameters) {
			dsc.parameters.addParameter(param.copyCtor());
		}
		
		return dsc;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public CircuitInterface getCircuitInterface() {
		List<Port> ports = new ArrayList<Port>();
		for (PortRelation pr : portrelations) {
			ports.add(new DefaultPort(pr.port.getName(), pr.port.getDirection(), pr.port.getType()));
		}
		return new DefaultCircuitInterface(componentName.toString(), ports);
	}

	public EOrientation getComponentOrientation() {
		try {
			return ((Orientation)(parameters.getValue(ISchemaComponent.KEY_ORIENTATION))).orientation;
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Orientation parameter not found.");
		}
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

	public List<SchemaPort> getSchemaPorts() {
		return schemaports;
	}

	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		int ind = SMath.calcClosestPort(xoffset, yoffset, dist, schemaports);
		if (ind == SMath.ERROR) return null;
		return schemaports.get(ind);
	}

	public SchemaPort getSchemaPort(int index) {
		if (index < 0 || index >= schemaports.size()) return null;
		return schemaports.get(index);
	}

	public Caseless getTypeName() {
		return componentName;
	}

	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		return new DefaultVHDLSegmentProvider();
	}

	public int getWidth() {
		return width;
	}

	public int schemaPortCount() {
		return schemaports.size();
	}

	public void setComponentOrientation(EOrientation orient) {
		try {
			parameters.setValue(ISchemaComponent.KEY_ORIENTATION, new Orientation(orient));
		} catch (InvalidParameterValueException e) {
			throw new RuntimeException("Orientation could not be set - invalid value.", e);
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Orientation could not be set.", e);
		}
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

	public SchemaPort getSchemaPort(Caseless name) {
		for (SchemaPort sp : schemaports) {
			if (sp.getName().equals(name)) return sp;
		}
		return null;
	}

	public void deserialize(ComponentWrapper compwrap) {
		// basic
		componentName = new Caseless(compwrap.getComponentName());
		codeFileName = compwrap.getCodeFileName();
		categoryName = compwrap.getCategoryName();
		generic = compwrap.getGenericComponent();
		width = compwrap.getWidth();
		height = compwrap.getHeight();
		
		// draw
		initDrawer(compwrap.getDrawerName());
		
		// parameters
		initParameters(compwrap.getParamWrappers());
		
		// ports
		initPortsOnly(compwrap.getPortWrappers());
		
		// physical schema ports
		initSchemaPortsOnly(compwrap.getSchemaPorts());
	}
	
	private void initPortsOnly(List<PortWrapper> portwrappers) {
		portrelations = new ArrayList<PortRelation>();
		
		Port port;
		PortRelation portrel;
		for (PortWrapper portwrap : portwrappers) {
			port = createPortFromWrapper(portwrap);
			portrel = new PortRelation(port);
			portrelations.add(portrel);
		}
	}
	
	private void initSchemaPortsOnly(List<SchemaPortWrapper> portwrappers) {
		schemaports = new ArrayList<SchemaPort>();
		for (SchemaPortWrapper spw : portwrappers) {
			SchemaPort sp = new SchemaPort(spw);
			sp.setPortindex(spw.getPortindex());
			schemaports.add(sp);
		}
		for (int i = 0, sz = schemaports.size(); i < sz; i++) {
			SchemaPort sp = schemaports.get(i);
			if (sp.getPortindex() != SchemaPort.NO_PORT) portrelations.get(sp.getPortindex()).relatedTo.add(sp);
		}
	}
	
	private Port createPortFromWrapper(PortWrapper portwrap) {
		return PortFactory.createPort(portwrap);
	}
	
	public String getCodeFileName() {
		return codeFileName;
	}

	public Iterator<Port> portIterator() {
		return new PortIterator();
	}

	public Iterator<SchemaPort> schemaPortIterator() {
		return schemaports.iterator();
	}

	public int portCount() {
		return portrelations.size();
	}

	public Port getPort(int index) {
		return portrelations.get(index).port;
	}

	public void setPort(int index, Port nport) {
		if (index < 0 || index >= portrelations.size()) throw new IndexOutOfBoundsException();
		
		// cache all SchemaPorts because of their mappings
		Map<Caseless, SchemaPort> cached = new HashMap<Caseless, SchemaPort>();
		for (SchemaPort sp : schemaports) {
			cached.put(sp.getName(), sp);
		}
		
		// put the new port at desired index
		portrelations.get(index).port = nport;
		
		// create a list of PortWrappers
		List<PortWrapper> portwrappers = new ArrayList<PortWrapper>();
		for (PortRelation pr : portrelations) {
			PortWrapper pw = new PortWrapper(pr.port);
			portwrappers.add(pw);
		}
		
		// init ports once more
		initPorts(portwrappers);
		
		// search for new SchemaPorts with same old names and append mappings
		for (SchemaPort sp : schemaports) {
			SchemaPort oldsp = cached.get(sp.getName());
			if (oldsp == null) continue;
			sp.setMapping(oldsp.getMapping());
		}
	}

	public boolean isGeneric() {
		return generic;
	}

	public EComponentType getComponentType() {
		return EComponentType.PREDEFINED;
	}

	
	

}






















