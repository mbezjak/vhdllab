package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.vhdl.tb.Generator;

import java.util.List;

/**
 * This interface describes ENTITY block in VHDL code. It consists of
 * entity name and a {@linkplain java.util.Collection} of {@linkplain Port}.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of ENTITY block:
 * <blockquote><pre>
 * ENTITY circuit IS PORT(
 * 	a : IN std_logic;
 * 	b : OUT std_logic);
 * END circuit;
 * </pre></blockquote>
 * In this example this interface must contain entity name: <code>circuit</code>
 * and a collection of ports that describes this particular circuit. Therefor one
 * port in a port collection must contain information about the following line:
 * <i>a : IN std_logic</i>! This is true for every other port in port collection.
 * 
 * <h3>Restrictions</h3>
 * 
 * A <code>Collection</code> of <code>Port</code> must not contain two or more 
 * ports that share the same name (ignore case)! Also entity name must have the
 * following format:
 * <ul>
 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
 * <li>it must not start with a non-alpha character
 * <li>it must not end with an underline character
 * <li>it must not contain an underline character after an underline character
 * </ul>
 * 
 * @author Miro Bezjak
 * @see Port
 */
public interface CircuitInterface {
	
	/**
	 * Returns a list of ports. Your collection must now be returned as a
	 * {@link java.util.List list}. Returned list must be sorted according
	 * to the way ports were added (declared).
	 * <p>
	 * A returned list of ports must be read-only to ensure consistency!
	 * 
	 * @return a list of ports.
	 * @see Port
	 */
	List<Port> getPorts();
	
	/**
	 * Returns a name of described entity block.
	 * 
	 * @return an entity name.
	 */
	String getEntityName();
	
	/**
	 * Search a collection of ports and find a port whose name equals <code>portName</code>
	 * (ignore case). 
	 * 
	 * @param portName a name of a port.
	 * @return a port that contains <code>portName</code>; <code>null</code> if that port does not exist.
	 * @throws NullPointerException if <code>portName</code> is <code>null</code>.
	 * @see Port
	 */
	Port getPort(String portName);
	
	/**
	 * Compares this circuit interface to the specified object. Returns
	 * <code>true</code> if and only if the specified object is also a
	 * <code>CircuitInterface</code> and if entity names (ignore case)
	 * and port lists are the same.
	 * 
	 * @param o the object to compare this <code>CircuitInterface</code> against.
	 * @return <code>true</code> if <code>CircuitInterface</code> are equal; <code>false</code> otherwise.
	 * @see Port#equals(Object)
	 */
	boolean equals(Object o);
	
	/**
	 * Determine compatibility with {@linkplain Generator}. This method is used to
	 * determine if <code>CircuitInterface's Ports</code> contain the same information 
	 * as <code>Generator's Signals</code>.
	 * <p>
	 * This <code>CircuitInterface</code> and <code>Generator</code> are compatible 
	 * if following is true:
	 * <ul>
	 * <li>CircuitInterface's port collection is the same size as Generator's signal collection 
	 * (they have the same number of elements)
	 * <li>for every port in port collection there is exactly one "equal" signal in signal
	 * collection
	 * </ul>
	 * <p>
	 * A port and a signal are "equal" if following is true:
	 * <ul>
	 * <li>a port name is equal (ignore case) to signal name
	 * <li>if port is a scalar then signal must be a scalar aswell or if port is a vector
	 * then signal must be a vector aswell and
	 * 	<ul>
	 * 	<li>{@link Type#getRangeFrom() port's rangeFrom} must be equal to {@link hr.fer.zemris.vhdllab.vhdl.tb.Signal#getRangeFrom() signal's rangeFrom}
	 * 	<li>{@link Type#getRangeTo() port's rangeTo} must be equal to {@link hr.fer.zemris.vhdllab.vhdl.tb.Signal#getRangeTo() signal's rangeTo}
	 * 	</ul>
	 * </ul>
	 * 
	 * @param generator a generator to detemine compatibility with.
	 * @return <code>true</code> if this interface and <code>generator</code> are compatible; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>generator</code> is <code>null</code>.
	 * @see Generator#isCompatible(CircuitInterface)
	 * @see Port
	 * @see hr.fer.zemris.vhdllab.vhdl.tb.Signal
	 */
	boolean isCompatible(Generator generator);
}