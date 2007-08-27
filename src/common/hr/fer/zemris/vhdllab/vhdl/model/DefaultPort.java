package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.utilities.StringFormat;

/**
 * This is a default implementation of {@link Port}.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @see Type
 * @see Direction
 */
public final class DefaultPort implements Port {

	private static final long serialVersionUID = -8925566308988401573L;

	/**
	 * A port name.
	 * 
	 * @serial
	 */
	private String portName;

	/**
	 * A direction of a port.
	 * 
	 * @serial
	 */
	private Direction direction;

	/**
	 * A type of a port.
	 * 
	 * @serial
	 */
	private Type type;

	/**
	 * Create an instance of this class using name, direction and type to
	 * describe a port. A port name must have a format as described by
	 * {@link StringFormat#isCorrectPortName(String)}.
	 * 
	 * @param name
	 *            a name of a port
	 * @param direction
	 *            a direction of a port
	 * @param type
	 *            a type of a port
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is not of correct format
	 * @see Direction
	 * @see Type
	 */
	public DefaultPort(String name, Direction direction, Type type) {
		if (name == null) {
			throw new NullPointerException("Port name can not be null.");
		}
		if (direction == null) {
			throw new NullPointerException("Direction can not be null.");
		}
		if (type == null) {
			throw new NullPointerException("Type can not be null.");
		}

		if (!StringFormat.isCorrectPortName(name))
			throw new IllegalArgumentException(
					"Port name is not of correct format.");

		this.portName = name;
		this.direction = direction;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#getName()
	 */
	public String getName() {
		return portName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#getDirection()
	 */
	public Direction getDirection() {
		return direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#getType()
	 */
	public Type getType() {
		return type;
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
		result = prime * result + direction.hashCode();
		result = prime * result + portName.toLowerCase().hashCode();
		result = prime * result + type.hashCode();
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
		if (getClass() != obj.getClass())
			return false;
		final DefaultPort other = (DefaultPort) obj;
		if (!direction.equals(other.direction))
			return false;
		if (!portName.equalsIgnoreCase(other.portName))
			return false;
		if (!type.equals(other.type))
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
		StringBuilder retval = new StringBuilder(20 + portName.length()
				+ direction.toString().length() + type.toString().length());
		retval.append("PORT NAME: ").append(portName).append(", DIRECTION: ")
				.append(direction.toString()).append(", ").append(
						type.toString());
		return retval.toString();
	}
	
}
