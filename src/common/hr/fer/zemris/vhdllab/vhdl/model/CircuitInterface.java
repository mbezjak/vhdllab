package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.utilities.StringFormat;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * This interface describes ENTITY block in VHDL code which consists of entity
 * name and a {@link Collection} of {@link Port}s.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of ENTITY block:
 * 
 * <blockquote>
 * 
 * <pre>
 *   ENTITY circuit IS PORT(
 *   	a : IN std_logic;
 *   	b : OUT std_logic);
 *   END circuit;
 * </pre>
 * 
 * </blockquote>
 * 
 * In this example a circuit interface contains entity name: <code>circuit</code>
 * and a collection of ports that describes this particular circuit. Therefor
 * one port in a port collection contains information about the following line:
 * <i>a : IN std_logic</i>! This is true for every other port in port
 * collection.
 * 
 * <h3>Restrictions</h3>
 * 
 * A circuit interface must not contain two or more ports that share the same
 * name (case ignored)! Also entity name must have a format as described by
 * {@link StringFormat#isCorrectEntityName(String)}.
 * 
 * @author Miro Bezjak
 * @see Port
 */
public interface CircuitInterface extends Serializable {

	/**
	 * Returns a list of ports. Your collection must now be returned as a
	 * {@link List list}. Returned list must be sorted according to the way
	 * ports were added (declared).
	 * <p>
	 * A returned list of ports must be read-only to ensure consistency!
	 * </p>
	 * 
	 * @return a list of ports
	 * @see Port
	 */
	List<Port> getPorts();

	/**
	 * Returns a name of described entity block.
	 * 
	 * @return an entity name
	 */
	String getEntityName();

	/**
	 * Returns a port that has the same name as specified <code>portName</code>
	 * (case ignored).
	 * 
	 * @param portName
	 *            a name of a port
	 * @return a port that has the same name as <code>portName</code>;
	 *         <code>null</code> if such port does not exist
	 * @throws NullPointerException
	 *             if <code>portName</code> is <code>null</code>
	 * @see Port
	 */
	Port getPort(String portName);

	/**
	 * Returns a hash code for this circuit interface.
	 * 
	 * @return a hash code
	 */
	int hashCode();
	
	/**
	 * Compares this circuit interface to the specified object. Returns
	 * <code>true</code> if and only if the specified object is also a
	 * <code>CircuitInterface</code> and if entity names (ignore case) and
	 * port lists are the same.
	 * 
	 * @param o
	 *            the object to compare this <code>CircuitInterface</code>
	 *            against
	 * @return <code>true</code> if <code>CircuitInterface</code> are equal;
	 *         <code>false</code> otherwise
	 * @see Port#equals(Object)
	 */
	boolean equals(Object o);
}