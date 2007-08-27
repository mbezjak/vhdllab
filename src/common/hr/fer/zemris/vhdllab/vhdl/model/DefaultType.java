package hr.fer.zemris.vhdllab.vhdl.model;

import java.io.ObjectStreamException;
import java.util.Arrays;

import hr.fer.zemris.vhdllab.utilities.StringFormat;

/**
 * This is a default implementation of {@link Type}.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 */
public final class DefaultType implements Type {

	private static final long serialVersionUID = -3236698761706994334L;

	/**
	 * Static field that uniformly describes port as a scalar by setting
	 * <code>range</code> to <code>null</code>.
	 */
	public static final int[] SCALAR_RANGE = null;

	/**
	 * Scalars do not have vector direction so vector direction of a scalar is
	 * <code>null</code>.
	 */
	public static final String SCALAR_VECTOR_DIRECTION = null;

	/** Set vector direction to DOWNTO. */
	public static final String VECTOR_DIRECTION_DOWNTO = "DOWNTO";

	/** Set vector direction to TO. */
	public static final String VECTOR_DIRECTION_TO = "TO";

	/**
	 * A type name. Usually: std_logic or std_logic_vector.
	 * 
	 * @serial
	 */
	private String typeName;

	/**
	 * A vector direction of a Type.
	 * 
	 * @serial
	 */
	private String vectorDirection;

	/**
	 * Type range. It contains either information that port is a scalar (by
	 * containing <code>null</code> value) or vector bounds. First element of
	 * this array contains index of lower bound of a vector (rangeFrom) while
	 * the second element contains index of upper bound of a vector (rangeTo).
	 * 
	 * @serial
	 */
	private int[] range;

	/**
	 * Create an instance of this class using name, range and vector direction
	 * to describe type of a port. A type name must have a format as described
	 * by {@link StringFormat#isCorrectVHDLType(String)}. Range must contain
	 * information about bounds of a vector or that port is a scalar, if that is
	 * the case. Vector direction must contain information about a key word that
	 * describes the way vector is declared (e.g. <code>DOWNTO</code>) or
	 * that port is a scalar, if that is the case.
	 * <p>
	 * Note that range and vector direction must not be exclusive between
	 * themselves, meaning that if range describes port as a scalar then vector
	 * direction must also contain the same information.
	 * </p>
	 * <p>
	 * When vector direction is <code>DOWNTO</code> then
	 * <code>rangeFrom</code> must not be less then <code>rangeTo</code>.
	 * Also when vector direction is <code>TO</code> then
	 * <code>rangeFrom</code> must not be greater then <code>rangeTo</code>.
	 * 
	 * @param typeName
	 *            a type name
	 * @param range
	 *            bounds of a vector or <code>null</code> if port is a scalar
	 * @param vectorDirection
	 *            a vector direction (<code>DOWNTO</code> or <code>TO</code>)
	 * @throws NullPointerException
	 *             if <code>typeName</code> is <code>null</code> or
	 *             <code>range</code> and <code>vectorDirection</code> are
	 *             exclusive between themselves
	 * @throws IllegalArgumentException
	 *             if one of the following is true:
	 *             <ul>
	 *             <li><code>typeName</code> is not of correct format</li>
	 *             <li><code>vectorDirection</code> is not
	 *             <code>DOWNTO</code> nor <code>TO</code></li>
	 *             <li><code>range</code> does not have exactly two elements</li>
	 *             <li>at least one element of <code>range</code> is negative</li>
	 *             <li>Second element of <code>range</code> is greater then
	 *             first one while <code>vectorDirection</code> is DOWNTO</li>
	 *             <li>First element of <code>range</code> is greater then
	 *             second one while <code>vectorDirection</code> is TO</li>
	 *             </ul>
	 */
	public DefaultType(String typeName, int[] range, String vectorDirection) {
		if (typeName == null) {
			throw new NullPointerException("Type name can not be null.");
		}
		if (range == null && vectorDirection != null) {
			throw new NullPointerException(
					"Conflict: vector direction can not be null while range is not.");
		}
		if (range != null && vectorDirection == null) {
			throw new NullPointerException(
					"Conflict: range can not be null while vector direction is not.");
		}
		if (!StringFormat.isCorrectVHDLType(typeName)) {
			throw new IllegalArgumentException(
					"Type name is not of correct format.");
		}
		if (range != null
				&& (range.length != 2 || range[0] < 0 || range[1] < 0))
			throw new IllegalArgumentException(
					"Range does not have two elements or at least one of them is negative.");
		if (vectorDirection != null) {
			if (!isVectorDirection(vectorDirection)) {
				throw new IllegalArgumentException(
						"Vector direction is incorrect.");
			}
			if (vectorDirection
					.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_DOWNTO)
					&& range[0] < range[1]) {
				throw new IllegalArgumentException(
						"First element of range must be greater then second one when vector direction is DOWNTO");
			}
			if (vectorDirection
					.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_TO)
					&& range[0] > range[1]) {
				throw new IllegalArgumentException(
						"Second element of range must be greater then first one when vector direction is TO");
			}
		}

		this.typeName = typeName;
		this.vectorDirection = vectorDirection;
		if (range == null) {
			this.range = DefaultType.SCALAR_RANGE;
		} else {
			this.range = new int[] { range[0], range[1] };
		}
	}

	/**
	 * Ignore case and check if <code>s</code> is vector direction. Vector
	 * direction is one of the following:
	 * <ul>
	 * <li>DOWNTO</li>
	 * <li>TO</li>
	 * </ul>
	 * <p>
	 * 
	 * @param s
	 *            a string that will be checked
	 * @return <code>true</code> if <code>s</code> is vector direction;
	 *         <code>false</code> otherwise
	 */
	private boolean isVectorDirection(String s) {
		return s.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_DOWNTO)
				|| s.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_TO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getTypeName()
	 */
	public String getTypeName() {
		return typeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getRangeFrom()
	 */
	public int getRangeFrom() {
		if (!isVector()) {
			throw new IllegalStateException("Scalars do not have a range.");
		}
		return range[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getRangeTo()
	 */
	public int getRangeTo() {
		if (!isVector()) {
			throw new IllegalStateException("Scalars do not have a range.");
		}
		return range[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getVectorDirection()
	 */
	public String getVectorDirection() {
		if (!isVector()) {
			throw new IllegalStateException(
					"Scalars do not have vector direction.");
		}
		return vectorDirection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#isScalar()
	 */
	public boolean isScalar() {
		return range == DefaultType.SCALAR_RANGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#isVector()
	 */
	public boolean isVector() {
		return !isScalar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#hasVectorDirectionDOWNTO()
	 */
	public boolean hasVectorDirectionDOWNTO() {
		if (isScalar()) {
			return false;
		}
		return vectorDirection
				.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_DOWNTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#hasVectorDirectionTO()
	 */
	public boolean hasVectorDirectionTO() {
		if (isScalar()) {
			return false;
		}
		return vectorDirection
				.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_TO);
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
		result = prime * result + Arrays.hashCode(range);
		result = prime * result + typeName.toLowerCase().hashCode();
		result = prime
				* result
				+ ((vectorDirection == null) ? 0 : vectorDirection
						.toUpperCase().hashCode());
		;
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
		final DefaultType other = (DefaultType) obj;
		if (!Arrays.equals(range, other.range))
			return false;
		if (!typeName.equalsIgnoreCase(other.typeName))
			return false;
		if (vectorDirection == null) {
			if (other.vectorDirection != null)
				return false;
		} else if (!vectorDirection.equalsIgnoreCase(other.vectorDirection))
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
		StringBuilder retval = new StringBuilder(20 + typeName.length());
		retval.append("TYPE NAME: ").append(typeName).append(", RANGE: ");
		if (isScalar())
			retval.append("scalar (no range)");
		else
			retval.append(getRangeFrom()).append(" ").append(vectorDirection)
					.append(" ").append(getRangeTo());
		return retval.toString();
	}

	/**
	 * Makes a defensive copy of default type.
	 */
	private Object readResolve() throws ObjectStreamException {
		return new DefaultType(typeName, range, vectorDirection);
	}

}
