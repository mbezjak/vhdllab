package hr.fer.zemris.vhdllab.vhdl.model;

/**
 * This interface describes type of a port in ENTITY block. It consists
 * of type name, range and vector direction.
 * <p>
 * Type name must contain information about a type of a port. Usualy
 * std_logic or std_logic_vector. Range must contain information about bounds
 * of a vector or that port is a scalar, if that is the case. Vector direction 
 * must contain information about a key word that describes the way vector is
 * declared (eg. DOWNTO) or that port is a scalar, if that is the case.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring a type of port in ENTITY block:
 * <blockquote>
 * std_logic;
 * </blockquote>
 * In this example this interface must contain type name: <code>std_logic</code>,  
 * range and vector direction that describes port as a scalar.
 * <p>
 * Another example, but with declaring a vector:
 * <blockquote>
 * std_logic_vector(2 DOWNTO 0)
 * </blockquote>
 * In this example this interface must contain type name: <code>std_logic_vector</code>,   
 * range that describes vector bounds: 2, 0 and vector direction: DOWNTO.
 * 
 * <h3>Restrictions</h3>
 * 
 * Type name must be of the following format:
 * <ul>
 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
 * <li>it must not start with a non-alpha character
 * <li>it must not end with an underline character
 * <li>it must not contain an underline character after an underline character
 * </ul>
 * Range must uniformly describe port as a scalar or bounds of a vector, whichever is the case.
 * Depending on the declaration of range, it can be <code>null</code>. In that case a <code>null</code>
 * value uniformly describes port as a scalar. When describing a vector it must contain exactly two
 * elemets: index of lower bound of a vector (rangeFrom) and index of upper bound of a vector (rangeTo)
 * both of which must not be negative numbers.
 * <p>
 * Vector direction can also describe port as a scalar with <code>null</code> value. If describing a
 * vector then it can only be one of the following:
 * <ul>
 * <li>DOWNTO
 * <li>TO
 * </ul>
 * Note that range and vector direction must not be exclusive between themselfs, meaning that
 * if range describes port as a scalar then vector direction must also contain the same information.
 * <p>
 * When vector direction is DOWNTO then rangeFrom must not be less then rangeTo and vice versa.
 * Also when vector direction is TO then rangeFrom must not be greater then rangeTo and vice versa.
 * 
 * @author Miro Bezjak
 */
public interface Type {
	
	/**
	 * Get a type name. Usualy std_logic or std_logic_vector.
	 *  
	 * @return a type name.
	 */
	String getTypeName();
	
	/**
	 * Returns an index of lower bound of a vector.
	 * 
	 * @return index of lower bound of a vector.
	 * @throws IllegalStateException if port is not a vector.
	 */
	int getRangeFrom();
	
	/**
	 * Returns an index of upper bound of a vector.
	 * 
	 * @return index of upper bound of a vector.
	 * @throws IllegalStateException if port is not a vector.
	 */
	int getRangeTo();
	
	/**
	 * Returns a vector direction representing a key word that describes the
	 * way vector is declared (and should be accessed) in VHDL. Vector direction
	 * can be:
	 * <ul>
	 * <li>DOWNTO
	 * <li>TO
	 * </ul>
	 * 
	 * @return a vector direction.
	 * @throws IllegalStateException if port is not a vector.
	 */
	String getVectorDirection();
	
	/**
	 * Test to see if port is a scalar by type.
	 * 
	 * @return <code>true</code> if port is a scalar; <code>false</code> otherwise.
	 * @see Type#isVector()
	 */
	boolean isScalar();
	
	/**
	 * Test to see if port is a vector by type.
	 * 
	 * @return <code>true</code> if port is a vector; <code>false</code> otherwise.
	 * @see Type#isScalar()
	 */
	boolean isVector();
	
	/**
	 * Test to see if vector direction is DOWNTO.
	 * 
	 * @return <code>true</code> if vector direction is DOWNTO; <code>false</code> otherwise.
	 * @see #hasVectorDirectionTO()
	 */
	boolean hasVectorDirectionDOWNTO();
	
	/**
	 * Test to see if vector direction is TO.
	 * 
	 * @return <code>true</code> if vector direction is TO; <code>false</code> otherwise.
	 * @see #hasVectorDirectionDOWNTO()
	 */
	boolean hasVectorDirectionTO();
	
	/**
	 * Compares this type to the specified object. Returns <code>true</code>
	 * if and only if the specified object is also a <code>Type</code> and if type names
	 * (ignore case), ranges and vector directions (ignore case) are the same.
	 * 
	 * @param o the object to compare this <code>Type</code> against.
	 * @return <code>true</code> if <code>Type</code> are equal; <code>false</code> otherwise.
	 */
	boolean equals(Object o);
}
