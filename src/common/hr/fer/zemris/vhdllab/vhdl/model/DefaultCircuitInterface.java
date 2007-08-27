package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.utilities.StringFormat;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a default implementation of {@link CircuitInterface}.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @see Port
 */
public final class DefaultCircuitInterface implements CircuitInterface {

	private static final long serialVersionUID = 3566491383856046473L;

	/**
	 * A name of entity block.
	 * 
	 * @serial
	 */
	private String entityName;

	/**
	 * A list of ports that describes circuit interface.
	 * 
	 * @serial
	 */
	private List<Port> portList;

	/** A read-only list of ports to return in getter. */
	private transient List<Port> portList_ro;

	/** A map of ports used for searching for a particular port. */
	private transient Map<String, Port> portMap;

	/**
	 * Create an instance of this class using only entity name with an empty
	 * port list. An entity name must have a format as described by
	 * {@link StringFormat#isCorrectEntityName(String)}.
	 * 
	 * @param name
	 *            an entity name
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is not of the correct format
	 */
	public DefaultCircuitInterface(String name) {
		this(name, new ArrayList<Port>());
	}

	/**
	 * Create an instance of this class using an entity name and a
	 * <code>port</code> that circuit consists of. An entity name must have a
	 * format as described by {@link StringFormat#isCorrectEntityName(String)}.
	 * 
	 * @param name
	 *            an entity name
	 * @param port
	 *            a port that circuit consists of
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is not of the correct format
	 */
	public DefaultCircuitInterface(String name, Port port) {
		this(name, createPortList(port));
	}

	/**
	 * Create an instance of this class using an entity name and port list that
	 * circuit consists of. An entity name must have a format as described by
	 * {@link StringFormat#isCorrectEntityName(String)}.
	 * 
	 * @param name
	 *            an entity name
	 * @param ports
	 *            a port list that circuit consists of
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is not of the correct format
	 */
	public DefaultCircuitInterface(String name, List<Port> ports) {
		if (name == null) {
			throw new NullPointerException("Name can not be null.");
		}
		if (ports == null) {
			throw new NullPointerException("Port list can not be null.");
		}
		if (!StringFormat.isCorrectEntityName(name)) {
			throw new IllegalArgumentException("Name is not of correct format.");
		}
		this.entityName = name;
		portList = new ArrayList<Port>(ports.size());
		portMap = new HashMap<String, Port>(ports.size());
		addPortList(ports);
		portList_ro = Collections.unmodifiableList(portList);
	}

	/**
	 * Appends all of the elements of <code>ports</code> to intern list.
	 * 
	 * @param ports
	 *            a port list to all
	 * @see #addPort(Port)
	 */
	private void addPortList(List<Port> ports) {
		for (Port p : ports) {
			addPort(p);
		}
	}

	/**
	 * Appends the specified port to the end of intern list.
	 * 
	 * @param p
	 *            a port that will be added.
	 * @see #addPortList(List)
	 */
	private void addPort(Port p) {
		if (getPort(p.getName()) != null) {
			// do not allow multiple ports with same name
			return;
		}
		portList.add(p);
		portMap.put(p.getName().toLowerCase(), p);
	}

	/**
	 * Creates a list out of specified <code>port</code>.
	 * 
	 * @param port
	 *            a port for whom to create a list
	 * @return a list containing specified <code>port</code>
	 */
	private static List<Port> createPortList(Port port) {
		if (port == null) {
			throw new NullPointerException("Port can not be null.");
		}
		List<Port> ports = new ArrayList<Port>(1);
		ports.add(port);
		return ports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#getPort(java.lang.String)
	 */
	@Override
	public Port getPort(String portName) {
		if (portName == null) {
			throw new NullPointerException("Port name can not be null.");
		}
		return portMap.get(portName.toLowerCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#getPorts()
	 */
	@Override
	public List<Port> getPorts() {
		return portList_ro;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return entityName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + entityName.toLowerCase().hashCode();
		result = prime * result + portList.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DefaultCircuitInterface other = (DefaultCircuitInterface) obj;
		if (!entityName.equalsIgnoreCase(other.entityName))
			return false;
		if (portList.size() != other.portList.size())
			return false;
		if (!portList.equals(other.portList))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder(50 + portList.toString()
				.length());
		retval.append("CIRCUIT INTERFACE, ENTITY NAME: ").append(entityName)
				.append(", CONTAINS ").append(portList.size()).append(
						" PORTS:\n");
		for (Port p : portList) {
			retval.append(p.toString()).append("\n");
		}
		return retval.toString();
	}

	/**
	 * Makes a defensive copy of default circuit interface.
	 */
	private Object readResolve() throws ObjectStreamException {
		return new DefaultCircuitInterface(entityName, portList);
	}

}