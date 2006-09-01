package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.vhdl.tb.StringUtil;

/**
 * This class describes type of a port in ENTITY block. It consists
 * of type name, range and vector direction.
 * <p>
 * Type name will contain information about a type of a port. Usualy
 * std_logic or std_logic_vector. Range will contain information about bounds
 * of a vector or that port is a scalar, if that is the case. Vector direction 
 * will contain information about a key word that describes the way vector is
 * declared (eg. DOWNTO) or that port is a scalar, if that is the case.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring a type of port in ENTITY block:
 * <blockquote>
 * std_logic;
 * </blockquote>
 * In this example this class will contain type name: <code>std_logic</code>,  
 * range and vector direction that describes port as a scalar (<code>null</code>
 * value).
 * <p>
 * Another example, but with declaring a vector:
 * <blockquote>
 * std_logic_vector(2 DOWNTO 0)
 * </blockquote>
 * In this example this class will contain type name: <code>std_logic_vector</code>,   
 * range that describes vector bounds: 2, 0 and vector direction: DOWNTO.
 * 
 * <h3>Restrictions</h3>
 * 
 * Type name will be of the following format:
 * <ul>
 * <li>it will contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
 * <li>it will not start with a non-alpha character
 * <li>it will not end with an underline character
 * <li>it will not contain an underline character after an underline character
 * </ul>
 * When describing a scalar, range it will contain a <code>null</code> value thus uniformly
 * describing port as a scalar and when describing a vector it, will contain exactly two elemets:
 * index of lower bound of a vector (rangeFrom) and index of upper bound of a vector (rangeTo)
 * both of which will not be negative numbers thus uniformly describing bounds of a vector.
 * <p>
 * Vector direction will also describe port as a scalar with <code>null</code> value. If describing a
 * vector then it will only be one of the following:
 * <ul>
 * <li>DOWNTO
 * <li>TO
 * </ul>
 * Note that range and vector direction will not be exclusive between themselfs, meaning that
 * if range describes port as a scalar then vector direction will also contain the same information.
 * <p>
 * When vector direction is DOWNTO then rangeFrom will not be less then rangeTo and vice versa.
 * Also when vector direction is TO then rangeFrom will not be greater then rangeTo and vice versa.
 * 
 * @author Miro Bezjak
 */
public class DefaultType implements Type {
	
	/**
	 * Static field that uniformly describes port as a scalar by setting <code>range</code>
	 * to <code>null</code>.
	 */
	public static final int[] SCALAR_RANGE = null;
	
	/** Scalars do not have vector direction so vector direction of a scalar is <code>null</code>. */
	public static final String SCALAR_VECTOR_DIRECTION = null;
	
	/** Set vector direction to DOWNTO. */
	public static final String VECTOR_DIRECTION_DOWNTO = "DOWNTO";
	
	/** Set vector direction to TO. */
	public static final String VECTOR_DIRECTION_TO = "TO";

	/** A type name. Usualy: std_logic or std_logic_vector. */
	private String typeName;
	
	/** A vector direction of a Type. */
	private String vectorDirection;
	
	/** 
	 * Type range. It contains either information that port is a scalar (by containing
	 * <code>null</code> value) or vector bounds. First element of this array contains
	 * index of lower bound of a vector (rangeFrom) while the second element contains
	 * index of upper bound of a vector (rangeTo).
	 */
	private int[] range;

	/**
	 * Create an instance of this class using name, range and vector direction
	 * to describe type of a port.
	 * <p>
	 * Type name must contain information about a type of a port. Usualy
	 * std_logic or std_logic_vector. Range must contain information about bounds
	 * of a vector or that port is a scalar, if that is the case. Vector direction 
	 * must contain information about a key word that describes the way vector is
	 * declared (eg. DOWNTO) or that port is a scalar, if that is the case.
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
	 * <p>
	 * Range must uniformly describe port as a scalar or bounds of a vector, whichever is the case.
	 * When describing a scalar it must contain a <code>null</code> value and when describing a
	 * vector it must contain exactly two elemets: index of lower bound of a vector (rangeFrom)
	 * and index of upper bound of a vector (rangeTo) both of which must not be negative numbers.
	 * <p>
	 * Vector direction must also describe port as a scalar with <code>null</code> value. If describing
	 * a vector then it must only be one of the following:
	 * <ul>
	 * <li>DOWNTO
	 * <li>TO
	 * </ul>
	 * <p>
	 * Note that range and vector direction must not be exclusive between themselfs, meaning that
	 * if range describes port as a scalar then vector direction must also contain the same information.
	 * <p>
	 * When vector direction is DOWNTO then rangeFrom must not be less then rangeTo and vice versa.
	 * Also when vector direction is TO then rangeFrom must not be greater then rangeTo and vice versa.
	 * 
	 * @param typeName a type name.
	 * @param range bounds of a vector or <code>null</code> if port is a scalar.
	 * @param vectorDirection a vector direction (DOWNTO or TO).
	 * @throws NullPointerException if <code>typeName</code> is <code>null</code> or <code>range</code> and <code>vectorDirection</code> are exclusive between themselfs.
	 * @throws IllegalArgumentException if one of the following is true:
	 * 			<ul>
	 * 			<li><code>typeName</code> is not of correct format
	 * 			<li><code>vectorDirection</code> is not <code>DOWNTO</code> nor <code>TO</code>
	 * 			<li><code>range</code> does not have exactly two elements.
	 * 			<li>at least one element of <code>range</code> is negative.
	 * 			<li>Second element of <code>range</code> is greater then first one
	 * 				while <code>vectorDirection</code> is DOWNTO
	 * 			<li>First element of <code>range</code> is greater then second one
	 * 				while <code>vectorDirection</code> is TO
	 * 			</ul>
	 */
	public DefaultType(String typeName, int[] range, String vectorDirection) {
		if( typeName == null ) throw new NullPointerException("Type name can not be null.");
		if( range == null && vectorDirection != null ) throw new NullPointerException("Conflict: vector direction can not be null while range is not.");
		if( range != null && vectorDirection == null ) throw new NullPointerException("Conflict: range can not be null while vector direction is not.");
		
		if( !StringUtil.isCorrectName(typeName) ) throw new IllegalArgumentException("Type name is not of correct format.");
		if( range != null && (range.length != 2 || range[0] < 0 || range[1] < 0) ) throw new IllegalArgumentException("Range does not have two elements or at least one of them is negative.");
		if( vectorDirection != null ) {
			if( !StringUtil.isVectorDirection(vectorDirection) ) throw new IllegalArgumentException("Vector direction is incorrect.");
			if( vectorDirection.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_DOWNTO) 
				&& range[0] < range[1] ) throw new IllegalArgumentException("First element of range must be greater then second one when vector direction is DOWNTO");
			if( vectorDirection.equalsIgnoreCase(DefaultType.VECTOR_DIRECTION_TO)
				&& range[0] > range[1] ) throw new IllegalArgumentException("Second element of range must be greater then first one when vector direction is TO");
		}

		this.typeName = typeName;
		this.vectorDirection = vectorDirection;
		if( range == null ) this.range = DefaultType.SCALAR_RANGE;
		else this.range = new int[] {range[0], range[1]};
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getTypeName()
	 */
	public String getTypeName() {
		return typeName;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getRangeFrom()
	 */
	public int getRangeFrom() {
		if( !isVector() ) throw new IllegalStateException("Scalars do not have a range.");
		return range[0];
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getRangeTo()
	 */
	public int getRangeTo() {
		if( !isVector() ) throw new IllegalStateException("Scalars do not have a range.");
		return range[1];
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#getVectorDirection()
	 */
	public String getVectorDirection() {
		if( !isVector() ) throw new IllegalStateException("Scalars do not have vector direction.");
		return vectorDirection;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#isScalar()
	 */
	public boolean isScalar() {
		return range == DefaultType.SCALAR_RANGE;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#isVector()
	 */
	public boolean isVector() {
		return range != DefaultType.SCALAR_RANGE;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#hasVectorDirectionDOWNTO()
	 */
	public boolean hasVectorDirectionDOWNTO() {
		if( vectorDirection == DefaultType.SCALAR_VECTOR_DIRECTION ) return false;
		return vectorDirection.equals("DOWNTO");
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Type#hasVectorDirectionTO()
	 */
	public boolean hasVectorDirectionTO() {
		if( vectorDirection == DefaultType.SCALAR_VECTOR_DIRECTION ) return false;
		return vectorDirection.equals("TO");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Type) ) return false;
		Type other = (Type) o;
		
		if( !(other.getTypeName().equalsIgnoreCase(this.typeName)) ) return false;
		if( other.isScalar() && this.isScalar() ) return true;
		return other.isVector() && this.isVector()
				&& other.getRangeFrom() == this.getRangeFrom()
				&& other.getRangeTo() == this.getRangeTo()
				&& other.getVectorDirection().equalsIgnoreCase(this.getVectorDirection());
	}
	
	/**
	 * Returns a hash code value for this <code>DefaultType</code> instance.
	 * The hash code of <code>DefaultType</code> instance is hash code of 
	 * type name (ignore case) XOR with hash code of rangeFrom XOR with hash
	 * code of rangeTo XOR with hash code of vector direction (ignore case)
	 * if port is a vector. If port is a scalar then hash code of
	 * <code>DefaultType</code> instance is hash code of type name (ignore case).
	 * <p>
	 * This ensures that <code>dt1.equals(dt2)</code> implies that 
	 * <code>dt1.hashCode() == dt2.hashCode()</code> for any two DefaultTypes, 
	 * <code>dt1</code> and <code>dt2</code>, as required by the general 
	 * contract of <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>DefaultType</code> instance.
	 * @see java.lang.String#hashCode()
	 */
	@Override
	public int hashCode() {
		if( isVector() ) return typeName.toLowerCase().hashCode() ^ Integer.valueOf(getRangeFrom()).hashCode()
							^ Integer.valueOf(getRangeTo()).hashCode() ^ vectorDirection.toUpperCase().hashCode();
		else return typeName.toLowerCase().hashCode();
	}

	/**
	 * Returns a string representing detailed description of this 
	 * <code>DefaultType</code> instance. Returned string will have
	 * the following format:
	 * <p>
	 * TYPE NAME: --type name here--, RANGE: --string representing range and vector direction here--
	 * 
	 * @return a string representing detailed description of this <code>DefaultType</code> instance.
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 20 + typeName.length() );
		retval.append("TYPE NAME: ").append(typeName).append(", RANGE: ");
		if( isScalar() ) retval.append("scalar (no range)");
		else retval.append(getRangeFrom()).append(" ").append(vectorDirection).append(" ").append(getRangeTo());
		return retval.toString();
	}
}
