package hr.fer.zemris.vhdllab.vhdl.tb;

/**
 * This class describes one state change in a cirtain time of
 * {@link Generator inducement}. It consists of time and state.
 * <p>
 * Time will describe when this impulse was generated while state
 * will describe value to which impulse changed.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring an impulse in inducement:
 * <blockquote>
 * (100, 01101)
 * </blockquote>
 * In this example this class will contain time: <code>100</code>
 * and state: <code>01101</code>.
 * 
 * <h3>Restrictions</h3>
 * 
 * Time will not be negative, state will not be zero-lenght and will
 * contain only known state values (ignore case), which are: '0', '1', 'U', 'Z', 'X',
 * 'W', 'H', 'L' and '-'.
 * 
 * @author Miro Bezjak
 */
public class DefaultImpulse implements Impulse {

	/**
	 * A time when impulse changed.
	 */
	private long time;
	
	/**
	 * A value to which impulse changed.
	 */
	private String state;
	
	/**
	 * Known state values written in tandem like this: "01UZXWHL-".
	 */
	public static final String knownStateValues = new String("01UZXWHL-");
	
	/**
	 * Create an instance of this class using time and state to describe one
	 * state change in a cirtain time of {@link Generator inducement}.
	 * <p>
	 * Time must describe when this impulse was generated while state
	 * must describe value to which impulse changed.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * Time must not be negative, state must not be zero-lenght and must
	 * contain only known state values (ignore case), which are: '0', '1',
	 * 'U', 'Z', 'X', 'W', 'H', 'L' and '-'.
	 * 
	 * @param time a time when impulse changed.
	 * @param state a value to which impulse changed.
	 * @throws NullPointerException if <code>state</code> is <code>null</code>.
	 * @throws IllegalArgumentException if one of the following is true:
	 * 			<ul>
	 * 			<li><code>time</code> is negative
	 * 			<li><code>state</code> is zero-lenght
	 * 			<li><code>state</code> does not contain known values
	 * 			</ul>
	 */
	public DefaultImpulse(long time, String state) {
		if( state == null ) throw new NullPointerException("State can not be null.");
		if( time < 0 ) throw new IllegalArgumentException("Time can not be negative.");
		if( state.length() == 0 ) throw new IllegalArgumentException("State must not be zero-lenght.");
		
		char[] chs = state.toCharArray();
		for(int i = 0; i < chs.length; i++) {
			if( !(DefaultImpulse.knownStateValues.toLowerCase().contains(String.valueOf(chs[i]))
				|| DefaultImpulse.knownStateValues.contains(String.valueOf(chs[i]))) ) throw new IllegalArgumentException("State does not contain known values.");
		}
		
		this.time = time;
		this.state = state;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Impulse#getTime()
	 */
	public long getTime() {
		return time;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Impulse#getState()
	 */
	public String getState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Impulse) ) return false;
		Impulse other = (Impulse) o;
		
		return other.getTime() == this.time
				&& other.getState().equalsIgnoreCase(this.state);
	}

	/**
	 * Returns a hash code value for this <code>DefaultImpulse</code> instance.
	 * The hash code of <code>DefaultImpulse</code> instance is hash code of 
	 * impulse time XOR with hash code of state (ignore case).
	 * <p>
	 * This ensures that <code>di1.equals(di2)</code> implies that 
	 * <code>di1.hashCode() == di2.hashCode()</code> for any two DefaultImpulses, 
	 * <code>di1</code> and <code>di2</code>, as required by the general 
	 * contract of <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>DefaultImpulse</code> instance.
	 * @see java.lang.Long#hashCode()
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(time).hashCode() ^ state.toLowerCase().hashCode();
	}
	
	/**
	 * Returns a string representing detailed description of this 
	 * <code>DefaultImpulse</code> instance. Returned string will have
	 * the following format:
	 * <p>
	 * IMPULSE NAME: --impulse name here--, STATE: --state here--
	 * 
	 * @return a string representing detailed description of this <code>DefaultImpulse</code> instance.
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 20 + state.length());
		retval.append("IMPULSE TIME: ").append(time)
			.append(", STATE: ").append(state);
		return retval.toString();
	}
}