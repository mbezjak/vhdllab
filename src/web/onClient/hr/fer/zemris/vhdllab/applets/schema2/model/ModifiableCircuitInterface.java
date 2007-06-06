package hr.fer.zemris.vhdllab.applets.schema2.model;

import java.util.List;
import java.util.Map;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;



/**
 * CircuitInterface koji ima dodatnu mogucnost postavljanja
 * imena komponente i postavljanja portova.
 * 
 * @author Axel
 *
 */
public class ModifiableCircuitInterface implements CircuitInterface {
	private String entityName;
	private List<Port> ports;
	private Map<String, Port> portmap;
	
	
	public ModifiableCircuitInterface(String name) {
		if (name == null) throw new NullPointerException("Name cannot be null");
		
	}
	
	
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String name) {
		entityName = name;
	}

	public Port getPort(String portName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Port> getPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	

}















