package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaParameterCollection;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;







public class DummyOR implements ISchemaComponent {
	public class DummyVHDLProvider implements IVHDLSegmentProvider {
		public String getInstantiation() {
			return "dummy_or_1: COMPONENT DummyOR PORT MAP(traalala);\n";
		}
		public String getSignalDefinitions() {
			return "signal s1: std_logic;\n";
		}
	}
	
	private final static int WIDTH = 50;
	private final static int HEIGHT_PER_PORT = 20;
	private final static Caseless TYPENAME = new Caseless("DummyOR");
	
	private CircuitInterface circint;
	private EOrientation orient;
	private IParameterCollection parameters;
	private List<SchemaPort> ports;
	
	public DummyOR(String instanceName) {
		circint = new DefaultCircuitInterface(instanceName);
		orient = EOrientation.EAST;
		parameters = new SchemaParameterCollection();
		ports = new LinkedList<SchemaPort>();
	}
	
	
	
	public ISchemaComponent copyCtor() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getCategoryName() {
		return "Basic";
	}
	public CircuitInterface getCircuitInterface() {
		return circint;
	}
	public EOrientation getComponentOrientation() {
		return orient;
	}
	public IComponentDrawer getDrawer() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getHeight() {
		return HEIGHT_PER_PORT * (ports.size() - 1);
	}
	public Caseless getName() {
		return new Caseless(circint.getEntityName());
	}
	public IParameterCollection getParameters() {
		return parameters;
	}
	public List<SchemaPort> getPorts() {
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
		// TODO hm, ovo bi mogao biti problem s obzirom na CircuitInterface
	}
	public void deserialize(String code) {
		throw new NotImplementedException();
	}
	public String serialize() {
		throw new NotImplementedException();
	}
	
	
	
	
}
	


	
	
	
	
	
	
	
	
	
