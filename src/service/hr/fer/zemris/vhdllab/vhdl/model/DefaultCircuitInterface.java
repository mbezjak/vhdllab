package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.vhdl.tb.Generator;
import hr.fer.zemris.vhdllab.vhdl.tb.Signal;
import hr.fer.zemris.vhdllab.vhdl.tb.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class describes ENTITY block in VHDL code. It consists of
 * entity name and a {@link java.util.List List} of {@link Port ports}.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of ENTITY block:
 * <blockquote><pre>
 * ENTITY circuit IS PORT(
 * 	a : IN std_logic;
 * 	b : OUT std_logic);
 * END circuit;
 * </pre></blockquote>
 * In this example this class will contain entity name: <code>circuit</code>
 * and a list of ports that describes this particular circuit. Therefor one
 * port in a port collection will contain information about the following line:
 * <i>a : IN std_logic</i>! This is true for every other port in port list.
 * 
 * <h3>Restrictions</h3>
 * 
 * A <code>List</code> of <code>Ports</code> will not contain two or more 
 * ports that share the same name (ignore case)! Also entity name will have the
 * following format:
 * <ul>
 * <li>it will contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
 * <li>it will not start with a non-alpha character
 * <li>it will not end with an underline character
 * <li>it will not contain an underline character after an underline character
 * </ul>
 * 
 * @author Miro Bezjak
 * @see Port
 */
public class DefaultCircuitInterface implements CircuitInterface {

	/** A name of entity block. */
	private String entityName;
	
	/** A list of ports that describes circuit interface. */
	private List<Port> portList = new ArrayList<Port>();
	
	/** A read-only list of ports that is used for return. */
	private List<Port> portList_ro = Collections.unmodifiableList(portList);
	
	/** A map of ports used for searching for a particular port. */
	private Map<String, Port> portMap = new HashMap<String, Port>();
	
	/**
	 * Create an instance of this class using only entity name. Port
	 * list will remain empty.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * A <code>name</code> must be of the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underline character
	 * <li>it must not contain an underline character after an underline character
	 * </ul>
	 * 
	 * @param name an entity name.
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>name</code> is not of the correct format.
	 */
	public DefaultCircuitInterface(String name) {
		createDefaultCircuitInterface(name, null);
	}
	
	/**
	 * Create an instance of this class using an entity name and port list
	 * <code>ports</code> that will be added to this port list.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * A <code>name</code> must be of the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underline character
	 * <li>it must not contain an underline character after an underline character
	 * </ul>
	 * 
	 * @param name an entity name.
	 * @param ports a port list that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>ports</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>name</code> is not of the correct format.
	 */
	public DefaultCircuitInterface(String name, List<Port> ports) {
		if( ports == null ) throw new NullPointerException("Port list can not be null.");
		createDefaultCircuitInterface(name, ports);
	}
	
	/**
	 * Create an instance of this class using an entity name and a
	 * <code>port</code> that will be added to this port list.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * A <code>name</code> must be of the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underline character
	 * <li>it must not contain an underline character after an underline character
	 * </ul>
	 * 
	 * @param name an entity name.
	 * @param port a port that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>port</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>name</code> is not of the correct format.
	 */
	public DefaultCircuitInterface(String name, Port port) {
		if( port == null ) throw new NullPointerException("Port can not be null.");
		List<Port> ports = new ArrayList<Port>();
		ports.add(port);
		createDefaultCircuitInterface(name, ports);
	}
	
	/**
	 * A private constructor.
	 * <p>
	 * All of the above constructors are redirected to this private method because 
	 * they are doing similar jobs. For details see class constructors above.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * A <code>name</code> must be of the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underline character
	 * <li>it must not contain an underline character after an underline character
	 * </ul>
	 * 
	 * @param name an entity name.
	 * @param ports a port list that will be added.
	 * @throws NullPointerException if either <code>name</code> or <code>ports</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>name</code> is not of the correct format.
	 */
	private void createDefaultCircuitInterface(String name, List<Port> ports) {
		if( name == null ) throw new NullPointerException("Name can not be null.");
		if( !StringUtil.isCorrectName(name) ) throw new IllegalArgumentException("Name is not of correct format.");
		
		this.entityName = name;
		if( ports != null ) addPortList(ports);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#getPort(java.lang.String)
	 */
	public Port getPort(String portName) {
		if( portName == null ) throw new NullPointerException("Port name can not be null.");
		return portMap.get(portName.toLowerCase());
	}
	
	/**
	 * Returns a list of ports. List is sorted according to the way
	 * ports were added (declared). A returned list of ports is read-only!
	 * 
	 * @return a list of ports.
	 * @see Port
	 */
	public List<Port> getPorts() {
		return portList_ro;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#getEntityName()
	 */
	public String getEntityName() {
		return entityName;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#equals(hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface)
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof CircuitInterface) ) return false;
		CircuitInterface other = (CircuitInterface) o;
		
		return other.getEntityName().toLowerCase().equals(this.entityName) 
			&& other.getPorts().size() == this.portList.size()
			&& other.getPorts().equals(this.portList);
	}
	
	/**
	 * Returns a hash code value for this <code>DefaultCircuitInterface</code>
	 * instance. The hash code of <code>DefaultCircuitInterface</code> instance
	 * is hash code of entity name (ignore case) XOR with hash code of port list.
	 * <p>
	 * This ensures that <code>dci1.equals(dci2)</code> implies that 
	 * <code>dci1.hashCode() == dci2.hashCode()</code> for any two 
	 * DefaultCircuitInterfaces, <code>dci1</code> and <code>dci2</code>, as
	 * required by the general contract of  <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>DefaultCircuitInterface</code> instance.
	 * @see java.lang.String#hashCode()
	 * @see java.util.List#hashCode()
	 */
	@Override
	public int hashCode() {
		return entityName.toLowerCase().hashCode() ^ portList.hashCode();
	}
	
	/**
	 * Returns a string representing detailed description of this 
	 * <code>DefaultCircuitInterface</code> instance. Returned string 
	 * will have the following format:
	 * <p>
	 * CIRCUIT INTERFACE, ENTITY NAME: --entity name here--, CONTAINS --port list size here-- PORTS:<br>
	 * --string representing every port in port list here--
	 * 
	 * @return a string representing detailed description of this <code>DefaultCircuitInterface</code> instance.
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 50 + portList.toString().length() );
		retval.append("CIRCUIT INTERFACE, ENTITY NAME: ").append(entityName)
			.append(", CONTAINS ").append(portList.size()).append(" PORTS:\n");
		for(Port p : portList) {
			retval.append(p.toString()).append("\n");
		}
		return retval.toString();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#isCompatible(hr.fer.zemris.vhdllab.vhdl.model.tb.Generator)
	 */
	public boolean isCompatible(Generator generator) {
		if( generator == null ) throw new NullPointerException("Generator can not be null.");
		if( portList.size() != generator.getSignals().size() ) return false;
		for(Signal signal : generator.getSignals()) {
			Port port = getPort(signal.getName()); //O(1)
			if( port == null ) return false;
			
			if( signal.isScalar() && port.getType().isScalar() ) continue;
			if( signal.isVector() && port.getType().isVector()
				&& signal.getRangeFrom() == port.getType().getRangeFrom()
				&& signal.getRangeTo() == port.getType().getRangeTo() ) continue;
			return false;
		}
		return true;
	}
	
	/**
	 * Appends the specified port to the end of this list.
	 * 
	 * @param p a port that will be added.
	 * @return <code>true</code> if <code>p</code> has been added; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>p</code> is <code>null</code>.
	 * @see #addPortList(List)
	 */
	public boolean addPort(Port p) {
		if( p == null ) throw new NullPointerException("Port can not be null.");
		if( getPort(p.getName()) != null ) return false;
		portList.add(p);
		portMap.put(p.getName().toLowerCase(), p);
		return true;
	}
		
	/**
	 * Appends all of the elements of <code>ports</code> to this list.
	 * 
	 * @param ports a port list that will to be added.
	 * @return <code>true</code> if at least one port in <code>ports</code> has been added; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>ports</code> is <code>null</code>.
	 * @see #addPort(Port)
	 */
	public boolean addPortList(List<Port> ports) {
		if( ports == null ) throw new NullPointerException("Port list can not be null.");
		boolean retval = false;
		for(Port p : ports) {
			if( addPort(p) ) retval = true;
		}
		return retval;
	}
}