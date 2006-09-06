package hr.fer.zemris.vhdllab.vhdl.model;

/**
 * This class describes direction of a port in ENTITY
 * block of VHDL code.
 * <p>
 * Direction can be:
 * <ul>
 * <li>IN
 * <li>OUT
 * <li>INOUT
 * <li>BUFFER
 * </ul>
 * 
 * @author Miro Bezjak
 */
public class Direction {

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
	
	/** byte-code of direction */
	private byte dir;
	
	/**
	 * Private constructor. Instead of storing a string value of 
	 * direction it stores byte-coded value.
	 * <ul>
	 * <li>byte-code 0 reserved for direction IN
	 * <li>byte-code 1 reserved for direction OUT
	 * <li>byte-code 2 reserved for direction INOUT
	 * <li>byte-code 3 reserved for direction BUFFER
	 * </ul>
	 * 
	 * @param direction a byte-coded direction.
	 */
	private Direction (byte dir) {
		this.dir = dir;
	}
	
	/**
	 * Get byte-coded direction.
	 * <ul>
	 * <li>byte-code 0 reserved for direction IN.
	 * <li>byte-code 1 reserved for direction OUT.
	 * <li>byte-code 2 reserved for direction INOUT.
	 * <li>byte-code 3 reserved for direction BUFFER.
	 * </ul>
	 * 
	 * @return a byte-coded direction. 
	 */
	private byte getDirection() {
		return dir;
	}
	
	/**
	 * Test if this direction is IN.
	 * 
	 * @return <code>true</code> if direction is IN; <code>false</code> otherwise.
	 */
	public boolean isIN(){
		return dir == CONST_IN;
	}
	
	/**
	 * Test if this direction is OUT.
	 * 
	 * @return <code>true</code> if direction is OUT; <code>false</code> otherwise.
	 */
	public boolean isOUT(){		
		return dir == CONST_OUT;
	}
	
	/**
	 * Test if this direction is INOUT.
	 * 
	 * @return <code>true</code> if direction is INOUT; <code>false</code> otherwise.
	 */
	public boolean isINOUT(){		
		return dir == CONST_INOUT;
	}
	
	/**
	 * Test if this direction is BUFFER.
	 * 
	 * @return <code>true</code> if direction is BUFFER; <code>false</code> otherwise.
	 */
	public boolean isBUFFER(){		
		return dir == CONST_BUFFER;
	}
	
	/**
	 * Returns a string representing description of this <code>Direction</code>
	 * instance. <code>Direction</code> can be:
	 * <ul>
	 * <li>IN
	 * <li>OUT
	 * <li>INOUT
	 * <li>BUFFER
	 * </ul>
	 * 
	 * @return a string representing description of this <code>Direction</code> instance.
	 */
	@Override
	public String toString() {
		switch (dir) {
		case CONST_IN: return "IN";
		case CONST_OUT: return "OUT";
		case CONST_INOUT: return "INOUT";
		case CONST_BUFFER: return "BUFFER";
		default: return "UNKNOWN";
		}
	}
	
	/**
	 * Compares this direction to the specified object. Returns <code>true</code>
	 * if and only if the specified object is also a <code>Direction</code> and
	 * they have the same byte-coded direction.
	 * <ul>
	 * <li>byte-code 0 reserved for direction IN.
	 * <li>byte-code 1 reserved for direction OUT.
	 * <li>byte-code 2 reserved for direction INOUT.
	 * <li>byte-code 3 reserved for direction BUFFER.
	 * </ul>
	 * 
	 * @param o the object to compare this <code>Direction</code> against.
	 * @return <code>true</code> if <code>Direction</code> are equal; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Direction) ) return false;
		Direction other = (Direction) o;
		return other.getDirection() == this.dir;
	}

	/**
	 * Returns a hash code value for this <code>Direction</code> instance.
	 * The hash code of <code>Direction</code> instance is byte-coded value.
	 * <p>
	 * This ensures that <code>dir1.equals(dir2)</code> implies that 
	 * <code>dir1.hashCode() == dir2.hashCode()</code> for any two Directions, 
	 * <code>dir1</code> and <code>dir2</code>, as required by the general contract of 
	 * <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>Direction</code> instance.
	 */
	@Override
	public int hashCode() {
		return Byte.valueOf(dir).hashCode();
	}
}