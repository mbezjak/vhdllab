package hr.fer.zemris.vhdllab.api.vhdl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes an <code>ENTITY</code> block of VHDL code.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class CircuitInterface implements Serializable, Iterable<Port> {

	private static final long serialVersionUID = 1L;

	/**
	 * A name of an entity.
	 */
	private final String name;

	/**
	 * A ports of a circuit.
	 */
	private final Map<String, Port> ports;

	/**
	 * Constructs a circuit interface out of specified parameters.
	 * 
	 * @param name
	 *            a name of an entity
	 * @param ports
	 *            all ports of a circuit
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if port list contains duplicate ports
	 */
	public CircuitInterface(String name, List<Port> ports) {
		if (name == null) {
			throw new NullPointerException("Entity name cant be null");
		}
		if (ports == null) {
			throw new NullPointerException("Ports cant be null");
		}
		this.name = name;
		this.ports = new LinkedHashMap<String, Port>(ports.size());
		for (Port p : ports) {
			Port previousPort = this.ports.put(p.getName().toLowerCase(), p);
			if (previousPort != null) {
				throw new IllegalArgumentException("Duplicate port: "
						+ previousPort);
			}
		}
	}

	/**
	 * Returns an entity name.
	 * 
	 * @return an entity name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a port for specified port name or <code>null</code> if such
	 * port doesn't exist.
	 * 
	 * @param portName
	 *            a name of a port
	 * @return a port for specified port name
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public Port getPort(String portName) {
		if (portName == null) {
			throw new NullPointerException("Port name cant be null");
		}
		return ports.get(portName.toLowerCase());
	}

	/**
	 * Returns an unmodifiable list of ports. Return value will never be
	 * <code>null</code> although it can be empty list. Beware of performance
	 * penalties when using this method. If you simply wish to iterate over a
	 * ports then used {@link #iterator()} method instead.
	 * 
	 * @return an unmodifiable list of ports
	 */
	public List<Port> getPorts() {
		Collection<Port> values = ports.values();
		return Collections.unmodifiableList(new ArrayList<Port>(values));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Port> iterator() {
		return getPorts().iterator();
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
		result = prime * result + name.toLowerCase().hashCode();
		result = prime * result + ports.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CircuitInterface))
			return false;
		final CircuitInterface other = (CircuitInterface) obj;
		if (!name.equalsIgnoreCase(other.name))
			return false;
		if (!ports.equals(other.ports))
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
		StringBuilder sb = new StringBuilder(10 + ports.size() * 50);
		sb.append("ENTITY ").append(name).append(" IS PORT(\n");
		for (Port p : this) {
			sb.append("\t").append(p).append(";\n");
		}
		if (!ports.isEmpty()) {
			// remove last semi-colon
			sb.delete(sb.length() - 2, sb.length() - 1);
		}
		sb.append(");\nEND ").append(name).append(";");
		return sb.toString();
	}

	/**
	 * Make a defensive copy.
	 */
	private Object readResolve() {
		List<Port> portList = new ArrayList<Port>(ports.values());
		return new CircuitInterface(name, portList);
	}

}
