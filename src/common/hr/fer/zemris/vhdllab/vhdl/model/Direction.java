package hr.fer.zemris.vhdllab.vhdl.model;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This class describes direction of a port in ENTITY block of VHDL code.
 * Direction can be:
 * <ul>
 * <li>IN</li>
 * <li>OUT</li>
 * <li>INOUT</li>
 * <li>BUFFER</li>
 * </ul>
 * 
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 */
public final class Direction implements Serializable {

	private static final long serialVersionUID = 6514558064471035263L;

	/** byte-coded value of direction IN */
	private static final byte CONST_IN = 0;

	/** byte-coded value of direction OUT */
	private static final byte CONST_OUT = 1;

	/** byte-coded value of direction INOUT */
	private static final byte CONST_INOUT = 2;

	/** byte-coded value of direction BUFFER */
	private static final byte CONST_BUFFER = 3;

	/**
	 * Constructor for direction IN.
	 */
	public static final Direction IN = new Direction(Direction.CONST_IN);

	/**
	 * Constructor for direction OUT.
	 */
	public static final Direction OUT = new Direction(Direction.CONST_OUT);

	/**
	 * Constructor for direction INOUT.
	 */
	public static final Direction INOUT = new Direction(Direction.CONST_INOUT);

	/**
	 * Constructor for direction BUFFER.
	 */
	public static final Direction BUFFER = new Direction(Direction.CONST_BUFFER);

	/**
	 * Byte-code of direction.
	 * 
	 * @serial
	 */
	private byte dir;

	/**
	 * Private constructor. Instead of storing a string value of direction it
	 * stores byte-coded value.
	 * <ul>
	 * <li>byte-code 0 reserved for direction IN</li>
	 * <li>byte-code 1 reserved for direction OUT</li>
	 * <li>byte-code 2 reserved for direction INOUT</li>
	 * <li>byte-code 3 reserved for direction BUFFER</li>
	 * </ul>
	 * 
	 * @param direction
	 *            a byte-coded direction.
	 */
	private Direction(byte dir) {
		this.dir = dir;
	}

	/**
	 * Returns <code>true</code> if this direction is IN.
	 * 
	 * @return <code>true</code> if direction is IN; <code>false</code>
	 *         otherwise
	 */
	public boolean isIN() {
		return dir == CONST_IN;
	}

	/**
	 * Returns <code>true</code> if this direction is OUT.
	 * 
	 * @return <code>true</code> if direction is OUT; <code>false</code>
	 *         otherwise
	 */
	public boolean isOUT() {
		return dir == CONST_OUT;
	}

	/**
	 * Returns <code>true</code> if this direction is INOUT.
	 * 
	 * @return <code>true</code> if direction is INOUT; <code>false</code>
	 *         otherwise
	 */
	public boolean isINOUT() {
		return dir == CONST_INOUT;
	}

	/**
	 * Returns <code>true</code> if this direction is BUFFER.
	 * 
	 * @return <code>true</code> if direction is BUFFER; <code>false</code>
	 *         otherwise
	 */
	public boolean isBUFFER() {
		return dir == CONST_BUFFER;
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
		result = prime * result + dir;
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
		final Direction other = (Direction) obj;
		if (dir != other.dir)
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
		switch (dir) {
		case CONST_IN:
			return "IN";
		case CONST_OUT:
			return "OUT";
		case CONST_INOUT:
			return "INOUT";
		case CONST_BUFFER:
			return "BUFFER";
		default:
			return "UNKNOWN";
		}
	}
	
	/**
	 * Makes a instance control.
	 */
	private Object readResolve() throws ObjectStreamException {
		if(isIN()) {
			return IN;
		} else if (isOUT()) {
			return OUT;
		} else if (isINOUT()) {
			return INOUT;
		} else if (isBUFFER()) {
			return BUFFER;
		} else {
			throw new InvalidObjectException("Unknown direction: " + dir);
		}
	}


}