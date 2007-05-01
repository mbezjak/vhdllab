package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;







public class DummyOR implements ISchemaComponent {
	private final static int WIDTH = 50;
	private final static int HEIGHT_PER_PORT = 20;
	
	private Map<Caseless, SchemaPort> ports;
	private CircuitInterface circinterf;
	private EOrientation orient;
	
	
	
	public DummyOR(String name) {
		ports = new HashMap<Caseless, SchemaPort>();
		circinterf = new DefaultCircuitInterface(name);
		orient = EOrientation.NORTH;
	}
	
	
	
	
	

	
	public ISchemaComponent copyCtor() {
		DummyOR dork = new DummyOR(circinterf.getEntityName());
		
		return dork;
	}

	
	public Set<Caseless> getAllPorts() {
		return ports.keySet(); 
	}

	
	
	
	public CircuitInterface getCircuitInterface() {
		return circinterf;
	}



	public EOrientation getComponentOrientation() {
		return orient;
	}




	public int getHeight() {
		return ports.size() * HEIGHT_PER_PORT;
	}



	public Caseless getName() {
		return new Caseless(circinterf.getEntityName());
	}




	public IParameterCollection getParameters() {
		// TODO
		throw new NotImplementedException();
	}





	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		// TODO
		throw new NotImplementedException();		
	}




	public SchemaPort getSchemaPort(Caseless name) {
		return ports.get(name);
	}





	public Caseless getTypeName() {
		return new Caseless("DummyOR");
	}






	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		// TODO
		throw new NotImplementedException();
	}





	public int getWidth() {
		return WIDTH;
	}




	public void setComponentOrientation(EOrientation orient) {
		this.orient = orient;		
	}




	public void deserialize(String code) {
		// TODO
		throw new NotImplementedException();		
	}




	public String serialize() {
		// TODO
		throw new NotImplementedException(); 
	}



	public Caseless getMappingForPort(Caseless name) {
		// TODO Auto-generated method stub
		return null;
	}







	public String getCategoryName() {
		return "BasicGates";
	}







	public void setName(Caseless name) {
	}







	public IComponentDrawer getDrawer() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	
	
	
}
	


	
	
	
	
	
	
	
	
	
