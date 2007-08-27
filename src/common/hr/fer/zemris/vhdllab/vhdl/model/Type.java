package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.utilities.StringFormat;

import java.io.Serializable;

/**
 * This interface describes type of a port in ENTITY block. It consists of type
 * name, range and vector direction.
 * <p>
 * Type name contains information about a type of a port. Usually std_logic or
 * std_logic_vector. Range contains information about bounds of a vector or that
 * port is a scalar, if that is the case. Vector direction contains information
 * about a keyword that describes the way vector is declared (e.g. DOWNTO) or
 * that port is a scalar, if that is the case.
 * </p>
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring a type of port in ENTITY block:
 * 
 * <blockquote> std_logic; </blockquote>
 * 
 * In this example a type contains a type name: <code>std_logic</code>, range
 * and vector direction that describes port as a scalar.
 * <p>
 * Another example, but with declaring a vector:
 * 
 * <blockquote>
 * 
 * <pre>
 * std_logic_vector(2 DOWNTO 0)
 * </pre>
 * 
 * </blockquote>
 * 
 * In this example a type contains a type name: <code>std_logic_vector</code>,
 * range that describes vector bounds: 2, 0 and vector direction: DOWNTO.
 * 
 * <h3>Restrictions</h3>
 * 
 * A type name must have a format as described by
 * {@link StringFormat#isCorrectVHDLType(String)}. Range must describe port as
 * a scalar or bounds of a vector, whichever is the case. Depending on the
 * declaration of range, it can be <code>null</code>. In that case a
 * <code>null</code> value describes port as a scalar. When describing a
 * vector it must contain exactly two elements: index of lower bound of a vector
 * (rangeFrom) and index of upper bound of a vector (rangeTo) both of which must
 * not be negative numbers.
 * <p>
 * Vector direction can also describe port as a scalar with <code>null</code>
 * value. If describing a vector then it can only be one of the following:
 * <ul>
 * <li>DOWNTO</li>
 * <li>TO</li>
 * </ul>
 * Note that range and vector direction must not be exclusive between
 * themselves, meaning that if range describes port as a scalar then vector
 * direction must also contain the same information.
 * <p>
 * When vector direction is <code>DOWNTO</code> then <code>rangeFrom</code>
 * must not be less then <code>rangeTo</code>. Also when vector direction is
 * <code>TO</code> then <code>rangeFrom</code> must not be greater then
 * <code>rangeTo</code>.
 * 
 * @author Miro Bezjak
 */
public interface Type extends Serializable {

	/**
	 * Get a type name. Usually <code>std_logic</code> or
	 * <code>std_logic_vector</code>.
	 * 
	 * @return a type name
	 */
	String getTypeName();

	/**
	 * Returns an index of lower bound of a vector.
	 * 
	 * @return index of lower bound of a vector
	 * @throws IllegalStateException
	 *             if port is not a vector
	 */
	int getRangeFrom();

	/**
	 * Returns an index of upper bound of a vector.
	 * 
	 * @return index of upper bound of a vector
	 * @throws IllegalStateException
	 *             if port is not a vector
	 */
	int getRangeTo();

	/**
	 * Returns a vector direction representing a keyword that describes the way
	 * vector is declared (and should be accessed) in VHDL. Vector direction can
	 * be:
	 * <ul>
	 * <li>DOWNTO</li>
	 * <li>TO</li>
	 * </ul>
	 * 
	 * @return a vector direction
	 * @throws IllegalStateException
	 *             if port is not a vector
	 */
	String getVectorDirection();

	/**
	 * Returns <code>true</code> if port is a scalar by type.
	 * 
	 * @return <code>true</code> if port is a scalar; <code>false</code>
	 *         otherwise
	 * @see Type#isVector()
	 */
	boolean isScalar();

	/**
	 * Returns <code>true</code> if port is a vector by type.
	 * 
	 * @return <code>true</code> if port is a vector; <code>false</code>
	 *         otherwise
	 * @see Type#isScalar()
	 */
	boolean isVector();

	/**
	 * Returns <code>true</code> if vector direction is <code>DOWNTO</code>.
	 * 
	 * @return <code>true</code> if vector direction is <code>DOWNTO</code>;
	 *         <code>false</code> otherwise
	 * @see #hasVectorDirectionTO()
	 */
	boolean hasVectorDirectionDOWNTO();

	/**
	 * Returns <code>true</code> if vector direction is <code>TO</code>.
	 * 
	 * @return <code>true</code> if vector direction is <code>TO</code>;
	 *         <code>false</code> otherwise
	 * @see #hasVectorDirectionDOWNTO()
	 */
	boolean hasVectorDirectionTO();

	/**
	 * Returns a hash code for this type.
	 * 
	 * @return a hash code
	 */
	int hashCode();

	/**
	 * Compares this type to the specified object. Returns <code>true</code>
	 * if and only if the specified object is also a <code>Type</code> and if
	 * type names (ignore case), ranges and vector directions (case ignored) are
	 * the same.
	 * 
	 * @param o
	 *            the object to compare this <code>Type</code> against
	 * @return <code>true</code> if <code>Type</code>s are equal;
	 *         <code>false</code> otherwise
	 */
	boolean equals(Object o);
}
