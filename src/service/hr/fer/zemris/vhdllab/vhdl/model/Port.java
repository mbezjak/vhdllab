package hr.fer.zemris.vhdllab.vhdl.model;


/**
 * This interface describes port part in ENTITY block of VHDL code.
 * It consists of port name, {@linkplain Direction} and {@linkplain Type}.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of port part in ENTITY block:
 * <blockquote>
 * a : IN std_logic
 * </blockquote>
 * In this example this interface must contain port name: <code>a</code>,
 * direction: <code>IN</code> and a type: scalar; <code>std_logic</code>.
 * <p>
 * Another example, but with declaring a vector:
 * <blockquote>
 * 	b : OUT std_logic_vector(2 DOWNTO 0)
 * </blockquote>
 * In this example this interface must contain port name: <code>b</code>,
 * direction: <code>OUT</code> and a <code>type</code>: vector; rangeFrom: 2;
 * rangeTo: 0; vectorDirection: DOWNTO; <code>std_logic</code>. For more
 * information see {@linkplain Type}.
 * 
 * <h3>Restrictions</h3>
 * 
 * Port name must have the following format:
 * <ul>
 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
 * <li>it must not start with a non-alpha character
 * <li>it must not end with an underline character
 * <li>it must not contain an underline character after an underline character
 * </ul>
 * 
 * @author Miro Bezjak
 * @see Type
 * @see Direction
 */
public interface Port {
	
	/**
	 * Returns a name of port.
	 * 
	 * @return a port name.
	 */
	String getName();
	
	/**
	 * Returns a direction of a port. Direction can be:
	 * <ul>
	 * <li>IN
	 * <li>OUT
	 * <li>INOUT
	 * <li>BUFFER
	 *</ul>
	 *  
	 * @return an instance of <code>Direction</code>.
	 * @see Direction
	 */
	Direction getDirection();
	
	/**
	 * Returns a type of a port.
	 * <p>
	 * Type contains information regarding a type of a port: type name, if port
	 * is a scalar or a vector, if a vector then also a bounds of a vector and a
	 * vector direction. For more information see {@linkplain Type}.
	 * 
	 * @return a type of a port.
	 * @see Type
	 */
	Type getType();
	
	/**
	 * Compares this port to the specified object. Returns <code>true</code>
	 * if and only if the specified object is also a <code>Port</code> and if
	 * port names (ignore case), directions and types are the same.
	 * 
	 * @param o the object to compare this <code>Port</code> against.
	 * @return <code>true</code> if <code>Port</code> are equal; <code>false</code> otherwise.
	 * @see Direction#equals(Object)
	 * @see Type#equals(Object)
	 */
	boolean equals(Object o);
}
