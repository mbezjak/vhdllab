package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ComponentWrapper;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;







public class DummyOR implements ISchemaComponent {
	public class DummyVHDLProvider implements IVHDLSegmentProvider {
		public String getInstantiation(ISchemaInfo info) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			
			sb.append(entityName).append(": COMPONENT ");
			sb.append(getTypeName()).append(" PORT MAP(");
			for (SchemaPort port : ports) {
				if (first) first = false; else sb.append(", ");
				Caseless wire = port.getMapping();
				sb.append(port.getName()).append(" => ").append((wire == null) ? "open" : wire.toString());
			}
			sb.append(");\n");
			
			return sb.toString();
		}
		public String getSignalDefinitions(ISchemaInfo info) {
			return "signal sig_" + entityName + "_1: std_logic;\n";
		}
	}
	
	private final static int WIDTH = 50;
	private final static int HEIGHT_PER_PORT = 20;
	private final static Caseless TYPENAME = new Caseless("DummyOR");
	
	private Caseless entityName;
	private EOrientation orient;
	private IParameterCollection parameters;
	private List<SchemaPort> ports;
	
	
	public DummyOR(String name) {
		create_dor(name, 2);
	}
	
	public DummyOR(String name, int numOfPorts) {
		create_dor(name, numOfPorts);
	}
	
	private void create_dor(String name, int numOfPorts) {
		// set ports
		entityName = new Caseless(name);
		
		// set orientation
		orient = EOrientation.EAST;
		
		// set parameters
		parameters = new SchemaParameterCollection();
		
		// set ports
		ports = new LinkedList<SchemaPort>();
		SchemaPort port;
		for (int i = 0; i < numOfPorts; i++) { // ulazi
			port = new SchemaPort(0, HEIGHT_PER_PORT, new Caseless("in_or" + i));
			ports.add(port);
		}
		port = new SchemaPort(WIDTH, (numOfPorts + 1) * HEIGHT_PER_PORT, new Caseless("out_or"));
		ports.add(port);
	}
	
	
	
	
	
	public SchemaPort getSchemaPort(Caseless name) {
		// TODO Auto-generated method stub
		return null;
	}

	public ISchemaComponent copyCtor() {
		throw new NotImplementedException();
	}
	public String getCategoryName() {
		return "Basic";
	}
	public CircuitInterface getCircuitInterface() {
		throw new NotImplementedException("Must create circuit interface.");
	}
	public EOrientation getComponentOrientation() {
		return orient;
	}
	public Iterator<Port> portIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public IComponentDrawer getDrawer() {
		throw new NotImplementedException();
	}
	public int getHeight() {
		return HEIGHT_PER_PORT * (ports.size() - 1);
	}
	public Caseless getName() {
		return entityName;
	}
	public IParameterCollection getParameters() {
		return parameters;
	}
	public List<SchemaPort> getSchemaPorts() {
		return ports;
	}
	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		int index = SMath.calcClosestPort(xoffset, yoffset, dist, ports);
		
		if (index == -1) return null;
		else return ports.get(index);
	}
	public SchemaPort getSchemaPort(int index){
		try {
			return ports.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Iterator<SchemaPort> schemaPortIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public int portCount() {
		return ports.size();
	}
	public Caseless getTypeName() {
		return TYPENAME;
	}
	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		return new DummyVHDLProvider();
	}
	public int getWidth() {
		return WIDTH;
	}
	public void setComponentOrientation(EOrientation orientation) {
		orient = orientation;
	}
	public void setName(Caseless name) {
		entityName = name;
	}

	public void deserialize(ComponentWrapper compwrap) {
		// TODO Auto-generated method stub
		
	}

	public String getCodeFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isGeneric() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<SchemaPort> getPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
	


	
	
	
	
	
	
	
	
	
