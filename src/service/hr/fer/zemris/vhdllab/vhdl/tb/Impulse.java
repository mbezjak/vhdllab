package hr.fer.zemris.vhdllab.vhdl.tb;

/**
 * This interface describes one state change in a cirtain time of
 * {@link Generator inducement}. It consists of time and state.
 * <p>
 * Time must describe when this impulse was generated while state
 * must describe value to which impulse changed.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring an impulse in inducement:
 * <blockquote>
 * (100, 01101)
 * </blockquote>
 * In this example this interface must contain time: <code>100</code>
 * and state: <code>01101</code>.
 * 
 * <h3>Restrictions</h3>
 * 
 * Time must not be negative, state must not be zero-lenght and must
 * contain only known state values (ignore case), which are: '0', '1',
 * 'U', 'Z', 'X', 'W', 'H', 'L' and '-'.
 * 
 * @author Miro Bezjak
 */
public interface Impulse {
	
	/**
	 * Returns a time when impulse changed.
	 * 
	 * @return an impulse time.
	 */
	long getTime();
	
	/**
	 * Returns a value to which impulse changed.
	 * 
	 * @return a value to which impulse changed.
	 */
	String getState();
	
	/**
	 * Compares this impulse to the specified object. Returns <code>true</code>
	 * if and only if the specified object is also a <code>Impulse</code> and if
	 * times and states are the same.
	 * 
	 * @param o the object to compare this <code>Impulse</code> against.
	 * @return <code>true</code> if <code>Impulse</code> are equal; <code>false</code> otherwise.
	 */
	boolean equals(Object o);
}