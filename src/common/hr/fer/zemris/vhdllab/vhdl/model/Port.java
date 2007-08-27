package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.utilities.StringFormat;

import java.io.Serializable;

/**
 * This interface describes port part in ENTITY block of VHDL code. It consists
 * of port name, {@link Direction} and {@link Type}.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of port part in ENTITY block:
 * 
 * <blockquote>
 * 
 * <pre>
 * a : IN std_logic
 * </pre>
 * 
 * </blockquote>
 * 
 * In this example a port contains port name: <code>a</code>, direction:
 * <code>IN</code> and a type: scalar; <code>std_logic</code>.
 * 
 * <p>
 * Another example, but with declaring a vector:
 * 
 * <blockquote>
 * 
 * <pre>
 * b : OUT std_logic_vector(2 DOWNTO 0)
 * </pre>
 * 
 * </blockquote>
 * 
 * In this example a port contains port name: <code>b</code>, direction:
 * <code>OUT</code> and a <code>type</code>: vector; rangeFrom: 2; rangeTo:
 * 0; vectorDirection: DOWNTO; <code>std_logic</code>. For more information
 * regarding port type see {@link Type}.
 * </p>
 * 
 * <h3>Restrictions</h3>
 * 
 * A port name must have a format as described by
 * {@link StringFormat#isCorrectPortName(String)}.
 * 
 * @author Miro Bezjak
 * @see Type
 * @see Direction
 */
public interface Port extends Serializable {

	/**
	 * Returns a name of port.
	 * 
	 * @return a port name
	 */
	String getName();

	/**
	 * Returns a direction of a port.
	 * 
	 * @return an instance of <code>Direction</code>
	 */
	Direction getDirection();

	/**
	 * Returns a type of a port.
	 * 
	 * @return a port type
	 */
	Type getType();

	/**
	 * Returns a hash code for this port.
	 * 
	 * @return a hash code
	 */
	int hashCode();
	
	/**
	 * Compares this port to the specified object. Returns <code>true</code>
	 * if and only if the specified object is also a <code>Port</code> and if
	 * port names (case ignored), directions and types are the same.
	 * 
	 * @param o
	 *            the object to compare this <code>Port</code> against.
	 * @return <code>true</code> if <code>Port</code> are equal;
	 *         <code>false</code> otherwise.
	 * @see Direction#equals(Object)
	 * @see Type#equals(Object)
	 */
	boolean equals(Object o);
}
