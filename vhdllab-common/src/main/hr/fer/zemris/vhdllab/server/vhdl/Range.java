package hr.fer.zemris.vhdllab.server.vhdl;

import java.io.Serializable;

/**
 * Represents a range of a bit-vector or a simple scalar.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class Range implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A scalar range.
	 */
	public static final Range SCALAR = new Range();

	/**
	 * A bit-vector direction.
	 */
	private transient final VectorDirection direction;

	/**
	 * A lower bound of a bit-vector.
	 * 
	 * @serial
	 */
	private final int from;

	/**
	 * An upper bound of a bit-vector.
	 * 
	 * @serial
	 */
	private final int to;

	/**
	 * A flag indicating if this range represents a scalar rather then a
	 * bit-vector.
	 * 
	 * @serial
	 */
	private final boolean scalar;

	/**
	 * Creates a scalar.
	 */
	private Range() {
		/*
		 * sets a scalar range values
		 */
		direction = null;
		from = -1;
		to = -1;
		scalar = true;
	}

	/**
	 * Creates a bit-vector range out of specified parameters.
	 * 
	 * @param direction
	 *            a bit-vector direction
	 * @param from
	 *            a lower bound of a bit-vector
	 * @param to
	 *            an upper bound of a bit-vector
	 * @throws NullPointerException
	 *             if <code>direction</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if one of the following is true:
	 *             <ul>
	 *             <li><code>from < 0</code></li>
	 *             <li><code>to < 0</code></li>
	 *             <li><code>from < to</code> and direction is DOWNTO</li>
	 *             <li><code>from > to</code> and direction is TO</li>
	 *             </ul>
	 * @see #SCALAR
	 */
	public Range(int from, VectorDirection direction, int to) {
		super();
		if (direction == null) {
			throw new NullPointerException("Vector direction cant be null");
		}
		if (from < 0) {
			throw new IllegalArgumentException(
					"From bound must be >= 0, but was: " + from);
		}
		if (to < 0) {
			throw new IllegalArgumentException(
					"To bound must be >= 0, but was: " + to);
		}
		switch (direction) {
		case DOWNTO:
			if (from < to) {
				throw new IllegalArgumentException("from: " + from + " < to: "
						+ to + " direction: " + direction);
			}
			break;
		case TO:
			if (to < from) {
				throw new IllegalArgumentException("from: " + from + " > to: "
						+ to + " direction: " + direction);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown vector direction: "
					+ direction);
		}
		this.direction = direction;
		this.from = from;
		this.to = to;
		this.scalar = false;
	}

	/**
	 * Returns a bit-vector direction.
	 * 
	 * @return a bit-vector direction
	 * @throws IllegalStateException
	 *             if this range is a scalar
	 */
	public VectorDirection getDirection() {
		if (isScalar()) {
			throw new IllegalStateException(
					"Scalars dont have vector direction");
		}
		return direction;
	}

	/**
	 * Returns a lower bound of a bit-vector.
	 * 
	 * @return a lower bound of a bit-vector
	 * @throws IllegalStateException
	 *             if this range is a scalar
	 */
	public int getFrom() {
		if (isScalar()) {
			throw new IllegalStateException("Scalars dont have lower bound");
		}
		return from;
	}

	/**
	 * Returns an upper bound of a bit-vector.
	 * 
	 * @return an upper bound of a bit-vector
	 * @throws IllegalStateException
	 *             if this range is a scalar
	 */
	public int getTo() {
		if (isScalar()) {
			throw new IllegalStateException("Scalars dont have upper bound");
		}
		return to;
	}

	/**
	 * Returns a <code>true</code> if this range is actually a scalar or
	 * <code>false</code> otherwise.
	 * 
	 * @return a <code>true</code> if this range is actually a scalar or
	 *         <code>false</code> otherwise
	 * @see #isVector()
	 */
	public boolean isScalar() {
		return scalar;
	}

	/**
	 * Returns a <code>true</code> if this range is a vector or
	 * <code>false</code> otherwise.
	 * 
	 * @return a <code>true</code> if this range is a vector or
	 *         <code>false</code> otherwise
	 * @see #isScalar()
	 */
	public boolean isVector() {
		return !isScalar();
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
		if (isScalar()) {
			result = 1337;
		} else {
			result = prime * result + from;
			result = prime * result + to;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // takes care of scalars
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Range))
			return false;
		final Range other = (Range) obj;
		if (from != other.from)
			return false;
		if (to != other.to)
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
		StringBuilder sb = new StringBuilder(10);
		sb.append(from).append(" ");
		sb.append(direction).append(" ");
		sb.append(to);
		return sb.toString();
	}
	
	/**
	 * Make a defensive copy.
	 */
	private Object readResolve() {
		VectorDirection dir;
		if(isScalar()) {
			// singleton instance control
			return SCALAR;
		}
		if(from < to) {
			dir = VectorDirection.TO;
		} else {
			dir = VectorDirection.DOWNTO;
		}
		return new Range(from, dir, to);
	}

}
