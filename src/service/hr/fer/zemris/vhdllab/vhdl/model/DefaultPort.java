package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.vhdl.tb.StringUtil;

/**
 * This class describes port part in ENTITY block of VHDL code.
 * It consists of port name, {@linkplain Direction} and {@linkplain Type}.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of port part in ENTITY block:
 * <blockquote>
 * a : IN std_logic
 * </blockquote>
 * In this example this class will contain port name: <code>a</code>,
 * direction: <code>IN</code> and a type: scalar; <code>std_logic</code>.
 * <p>
 * Another example, but with declaring a vector:
 * <blockquote>
 * 	b : OUT std_logic_vector(2 DOWNTO 0)
 * </blockquote>
 * In this example this class will contain port name: <code>b</code>,
 * direction: <code>OUT</code> and a <code>type</code>: vector; rangeFrom: 2;
 * rangeTo: 0; vectorDirection: DOWNTO; <code>std_logic</code>. For more
 * information see {@linkplain Type}.
 * 
 * <h3>Restrictions</h3>
 * 
 * Port name will have the following format:
 * <ul>
 * <li>it will contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
 * <li>it will not start with a non-alpha character
 * <li>it will not end with an underline character
 * <li>it will not contain an underline character after an underline character
 * </ul>
 * 
 * @author Miro Bezjak
 * @see Type
 * @see Direction
 */
public class DefaultPort implements Port {

	/** A port name. */
	private String portName;
	
	/** A direction of a port. */
	private Direction direction;
	
	/** A type of a port. */
	private Type type;
	
	/**
	 * Create an instance of this class using name, direction and type to describe a port.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * Port name must be of the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underline character
	 * <li>it must not contain an underline character after an underline character
	 * </ul>
	 * 
	 * @param name a name of a port.
	 * @param direction a direction of a port.
	 * @param type a type of a port.
	 * @throws NullPointerException if <code>name</code>, <code>direction</code> or <code>type</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>name</code> is not of correct format.
	 * @see Direction
	 * @see Type
	 */
	public DefaultPort(String name, Direction direction, Type type) {
		if( name == null ) throw new NullPointerException("Port name can not be null.");
		if( direction == null ) throw new NullPointerException("Direction can not be null.");
		if( type == null ) throw new NullPointerException("Type can not be null.");
		
		if( !StringUtil.isCorrectName(name) ) throw new IllegalArgumentException("Port name is not of correct format.");
		
		this.portName = name;
		this.direction = direction;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#getName()
	 */
	public String getName() {
		return portName;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#getDirection()
	 */
	public Direction getDirection() {
		return direction;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#getType()
	 */
	public Type getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port#equals(hr.fer.zemris.vhdllab.vhdl.model.Port)
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Port) ) return false;
		Port other = (Port) o;
		
		return other.getName().toLowerCase().equalsIgnoreCase(this.portName)
			&& other.getDirection().equals(this.direction)
			&& other.getType().equals(this.type);
	}

	/**
	 * Returns a hash code value for this <code>DefaultPort</code> instance.
	 * The hash code of <code>DefaultPort</code> instance is hash code of 
	 * port name (ignore case) XOR with hash code of direction XOR with hash
	 * code of type (class implemeting <code>Type</code> interface).
	 * <p>
	 * This ensures that <code>dp1.equals(dp2)</code> implies that 
	 * <code>dp1.hashCode() == dp2.hashCode()</code> for any two DefaultPorts, 
	 * <code>dp1</code> and <code>dp2</code>, as required by the general
	 * contract of <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>DefaultPort</code> instance.
	 * @see java.lang.String#hashCode()
	 * @see Direction#hashCode()
	 * @see DefaultType#hashCode()
	 */
	@Override
	public int hashCode() {
		return portName.toLowerCase().hashCode() ^ direction.hashCode() ^ type.hashCode();
	}
	
	/**
	 * Returns a string representing detailed description of this
	 * <code>DefaultPort</code> instance. Returned string will have
	 * the following format:
	 * <p>
	 * PORT NAME: --port name here--, DIRECTION: --string representing direction here--, 
	 * 			--string representing type here--
	 * 
	 * @return a string representing detailed description of this <code>DefaultPort</code> instance.
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 20 + portName.length() + direction.toString().length() + type.toString().length() );
		retval.append("PORT NAME: ").append(portName).append(", DIRECTION: ")
			.append(direction.toString()).append(", ").append(type.toString());
		return retval.toString();
	}
}
