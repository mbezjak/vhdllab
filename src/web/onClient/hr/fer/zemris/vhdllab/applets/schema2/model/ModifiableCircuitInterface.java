package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



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
	private List<Port> ro_ports;
	private Map<String, Port> portmap;
	
	
	
	public ModifiableCircuitInterface(String name) {
		if (name == null) throw new NullPointerException("Name cannot be null");
		
		createCircuitInterface(name);
	}
	
	
	
	private void createCircuitInterface(String name) {
		entityName = name;
		
		ports = new LinkedList<Port>();
		ro_ports = Collections.unmodifiableList(ports);
		portmap = new HashMap<String, Port>();
	}



	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String name) {
		entityName = name;
	}

	public Port getPort(String portName) {
		return portmap.get(portName);
	}

	public List<Port> getPorts() {
		return ro_ports;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if( !(obj instanceof CircuitInterface) ) return false;
		CircuitInterface other = (CircuitInterface)obj;
		
		return other.getEntityName().equalsIgnoreCase(this.entityName) 
			&& other.getPorts().size() == this.ports.size()
			&& other.getPorts().equals(this.ports);
	}

	@Override
	public int hashCode() {
		return entityName.toLowerCase().hashCode() ^ ports.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 50 + ports.toString().length() );
		retval.append("CIRCUIT INTERFACE, ENTITY NAME: ").append(entityName)
			.append(", CONTAINS ").append(ports.size()).append(" PORTS:\n");
		for(Port p : ports) {
			retval.append(p.toString()).append("\n");
		}
		return retval.toString();
	}
	
	

}















