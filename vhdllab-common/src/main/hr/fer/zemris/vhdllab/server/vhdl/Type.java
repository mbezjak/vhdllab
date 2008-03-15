package hr.fer.zemris.vhdllab.server.vhdl;

import java.io.Serializable;

/**
 * Describes a port type in <code>ENTITY</code> block of VHDL code.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 * @see TypeName
 * @see Range
 */
public final class Type implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A name of a port type.
	 */
	private transient final TypeName typeName;

	/**
	 * A range of a port.
	 */
	private final Range range;

	/**
	 * Constructs a type out of specified parameters.
	 * 
	 * @param typeName
	 *            a name of a port type
	 * @param range
	 *            a range of a port
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>typeName</code> is {@link TypeName#STD_LOGIC}
	 *             while <code>range</code> represents a vector and vice versa
	 */
	public Type(TypeName typeName, Range range) {
		if (typeName == null) {
			throw new NullPointerException("Type name cant be null");
		}
		if (range == null) {
			throw new NullPointerException("Range cant be null");
		}
		switch (typeName) {
		case STD_LOGIC:
			if (!range.isScalar()) {
				throw new IllegalArgumentException(typeName + " while range: "
						+ range);
			}
			break;
		case STD_LOGIC_VECTOR:
			if (!range.isVector()) {
				throw new IllegalArgumentException(typeName + " while range: "
						+ range);
			}
			break;
		default:
			throw new IllegalStateException("Unrecognized type name: "
					+ typeName);
		}
		this.typeName = typeName;
		this.range = range;
	}

	/**
	 * Returns a name of a port type.
	 * 
	 * @return a name of a port type
	 */
	public TypeName getTypeName() {
		return typeName;
	}

	/**
	 * Returns a range of a port.
	 * 
	 * @return a range of a port
	 */
	public Range getRange() {
		return range;
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
		result = prime * result + range.hashCode();
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
		if (!(obj instanceof Type))
			return false;
		final Type other = (Type) obj;
		return range.equals(other.range);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (range.isScalar()) {
			return typeName.toString();
		}
		StringBuilder sb = new StringBuilder(20);
		sb.append(typeName).append("(").append(range).append(")");
		return sb.toString();
	}
	
	/**
	 * Make a defensive copy.
	 */
	private Object readResolve() {
		TypeName name;
		if(range.isScalar()) {
			name = TypeName.STD_LOGIC;
		} else {
			name = TypeName.STD_LOGIC_VECTOR;
		}
		return new Type(name, range);
	}

}
