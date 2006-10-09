package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.List;

/**
 * This interface describes one signal of {@link Generator inducement}.
 * It consists of signal name, range (signal type) and a
 * {@linkplain java.util.Collection} of {@linkplain Impulse} also known
 * as exciter.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring a signal in inducement:
 * <blockquote>
 * <signal name="a" type="scalar">(0,0)(100,1)(300,0)(400,1)(800,0)</signal>
 * </blockquote>
 * In this example this interface must contain signal name: <code>a</code>,
 * range that describes signal as a scalar and a collection of impulses that
 * describes this particular exciter. Therefor one impulse in an exciter must
 * contain information about the following impuse:
 * <i>(0,0)</i>! This is true for every other impulse in exciter.
 * <p>
 * Another example, but with declaring a vector:
 * <blockquote>
 * <signal name="b" type="vector" rangeFrom="2" rangeTo="0">(0,000)(200,001)(300,010)(400,100)(800,101)(900,111)</signal>
 * </blockquote>
 * In this example this interface must contain signal name: <code>b</code>,
 * range that describes vector bounds: 2, 0 and a collection of impulses that
 * describes this particular exciter. Therefor one impulse in an exciter must
 * contain information about the following impuse:
 * <i>(0,000)</i>! This is true for every other impulse in exciter.
 * 
 * <h3>Restrictions</h3>
 * 
 * Signal name must have the following format:
 * <ul>
 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underscore (_) characters
 * <li>it must not start with a non-alpha character
 * <li>it must not end with an underscore character
 * <li>it must not contain an underscore character after an underscore character
 * </ul>
 * Range must uniformly describe signal as a scalar or bounds of a vector,
 * whichever is the case. Depending on the declaration of range, it can be
 * <code>null</code>. In that case a <code>null</code> value uniformly
 * describes signal as a scalar. When describing a vector it must contain
 * exactly two elemets: index of lower bound of a vector (rangeFrom) and
 * index of upper bound of a vector (rangeTo) both of which must not be
 * negative numbers.
 * <p>
 * An exciter must not contain two or more {@link Impulse#equals(Object) equal}
 * impulses and it must be sorted ascending by time. Two or more neighborly
 * impulses in exciter must not share same state. Also state lenght (number of bits)
 * must match range. For example:
 * <ul>
 * <li>if signal is a scalar then every impulse in exciter must contain state
 * 	   that has only one bit
 * <li>if signal is a vector then every impulse in exciter must contain state
 * 	   that has exactly <code>Math.abs(rangeFrom - rangeTo + 1)</code> bits
 * </ul>
 * 
 * @author Miro Bezjak
 * @see Impulse
 */
public interface Signal {
	
	/**
	 * Returns a name of a signal.
	 * 
	 * @return a signal name.
	 */
	String getName();
	
	/**
	 * Returns an index of lower bound of a vector.
	 * 
	 * @return index of lower bound of a vector.
	 * @throws IllegalStateException if signal is not a vector.
	 */
	int getRangeFrom();
	
	/**
	 * Returns an index of upper bound of a vector.
	 * 
	 * @return index of upper bound of a vector.
	 * @throws IllegalStateException if signal is not a vector.
	 */
	int getRangeTo();
	
	/**
	 * Returns a list of impulses. Your collection must now be returned as a
	 * {@link java.util.List list}. Returned list must be sorted according
	 * to the way impulses were added.
	 * <p>
	 * A returned list of impulses must be read-only to ensure consistency!
	 * 
	 * @return a list of impulses.
	 * @see Impulse
	 */
	List<Impulse> getExciter();
	
	/**
	 * Test to see if signal is a scalar by type.
	 * 
	 * @return <code>true</code> if signal is a scalar; <code>false</code> otherwise.
	 * @see Signal#isVector()
	 */
	public boolean isScalar();
	
	/**
	 * Test to see if signal is a vector by type.
	 * 
	 * @return <code>true</code> if signal is a vector; <code>false</code> otherwise.
	 * @see Signal#isScalar()
	 */
	public boolean isVector();
	
	/**
	 * Compares this signal to the specified object. Returns
	 * <code>true</code> if and only if the specified object is also a
	 * <code>Signal</code> and if signal names (ignore case), ranges
	 * and exciters are the same.
	 * 
	 * @param o the object to compare this <code>Signal</code> against.
	 * @return <code>true</code> if <code>Signal</code> are equal; <code>false</code> otherwise.
	 * @see Port#equals(Object)
	 */
	boolean equals(Object o);
}