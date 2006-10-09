package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;

/**
 * This interface describes testbench inducement. It consists of measure unit,
 * duration and a {@linkplain java.util.Collection} of {@linkplain Signal}.
 * <p>
 * Testbech inducement must contain information regarding signal changes depending
 * on time. Measure unit must contain a unit of measurement for signal change times.
 * A duration must contain master duration for all signal changes and a collection
 * of signal must contain information on all signals.
 * 
 * <h3>Testbench inducement format</h3>
 * 
 * Inducement is a case insensitive and it must abide by the following format:<br>
 * <ul>
 * <li>it must contain a tag <code>&lt;measureUnit&gt;</code> and inside it a measure
 * 	   unit
 * <li>it must contain a tag <code>&lt;duration&gt;</code> and inside it a duration of
 * 	   all signals. Each {@link Signal signal} may contain {@link Impulse impulses}
 *     after duration, however Generator must not contain them.
 * <li>it may contain <code>&lt;signal&gt;</code> tag and inside it must contain at
 *     least one impulse
 * </ul>
 * <p>
 * Tags may be separated with line feed or whitespace characters. Inducement must not
 * contain two signals that share the same name (ignore case). Duration must contain
 * a non-negative long integer number and measure unit must only contain following
 * values:
 * <ul>
 * <li>fs
 * <li>ps
 * <li>ns
 * <li>us
 * <li>ms
 * <li>s
 * </ul>
 * <p>
 * A <code>&lt;signal&gt;</code> tag must contain attribute <code>name</code> and
 * <code>type</code>. Also if type of a signal is a vector then it must contain
 * attributes <code>rangeFrom</code> and <code>rangeTo</code>. Attributes must be
 * separated by either line feed or whitespace characters. Order of attributes is
 * not important.
 * <p>
 * Attribute <code>name</code> must have the following format:
 * <ul>
 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underscore (_) characters
 * <li>it must not start with a non-alpha character
 * <li>it must not end with an underscore character
 * <li>it must not contain an underscore character after an underscore character
 * </ul>
 * <p>
 * Attribute <code>type</code> must be one of following:
 * <ul>
 * <li>scalar
 * <li>vector
 * </ul>
 * <p>
 * Both attributes <code>rangeFrom</code> and <code>rangeTo</code> must contain only
 * non-negative integer numbers. If <code>type</code> is a scalar then there is no
 * need for a signal to contain attributes <code>rangeFrom</code> and <code>rangeTo</code>,
 * however if it does then both attributes must contain the same number. Also if
 * <code>type</code> is a vector then signal must contain attributes <code>rangeFrom</code>
 * and <code>rangeTo</code> and they may or may not be equal.
 * <p>
 * Furthermore, signal must contain at least one {@linkplain Impulse} which contains
 * a time and a state. If, however, signal contains more then one impulse then they
 * must be written in a tandem. A signal must not contain two or more equal (same
 * time and state) impulses and must be sorted ascending by time. Two or more
 * neighborly impulses must not share same state. Also state lenght (number of bits)
 * must match range. For example:
 * <ul>
 * <li>if signal is a scalar then each impulse must contain state that has only one bit
 * <li>if signal is a vector then each impulse must contain state that has exactly
 *     <code>Math.abs(rangeFrom - rangeTo + 1)</code> bits
 * </ul>
 * <p>
 * Also between two impulses there can be only line feed or whitespace characters.
 * Each impulse must have the following format:
 * <ul>
 * <li>it must start with a left bracket '('
 * <li>it must end with a right bracket ')'
 * <li>within a bracket it must contain a time when impulse changed and a value to
 *     which impulse changed (state)
 * <li>a time and a state must be separated with a comma ','
 * <li>a time must be placed between left bracket and a comma while a state must be
 *     placed between a comma and a right bracket.
 * </ul>
 * <p>
 * A time must be a non-negative long integer number while a state must not be
 * zero-lenght and must only contain known state values (ignore case), which are: '0',
 * '1', 'U', 'Z', 'X', 'W', 'H', 'L' and '-'.
 * 
 * Two or more neighborly
 * impulses in exciter must not share same state. 
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of testbench inducement:
 * <blockquote>
 * &lt;measureUnit&gt;ns&lt;/measureUnit&gt;<br>
 * &lt;duration>1000&lt;/duration&gt;<br>
 * &lt;signal name="A" type="scalar"&gt;(0,0)(100,1)(150,0)(300,1)&lt;/signal&gt;<br>
 * &lt;signal name="b" type="scalar"&gt;(0,0)(200,1)(300,z)(440,U)&lt;/signal&gt;<br>
 * &lt;signal name="c" type="scalar" rangeFrom="0" rangeTo="0"&gt;(0,0)(50,1)(300,0)(400,1)&lt;/signal&gt;<br>
 * &lt;signal name="d" type="vector" rangeFrom="0" rangeTo="0"&gt;(100,1)(200,0)(300,1)(400,z)&lt;/signal&gt;<br>
 * &lt;signal name="e" type="vector" rangeFrom="0" rangeTo="2"&gt;(0,000)(100,100)(400,101)(500,111)(600,010)&lt;/signal&gt;<br>
 * &lt;signal name="f" type="vector" rangeFrom="1" rangeTo="4"&gt;(0,0001)(100,1000)(200,0110)(300,U101)(400,1001)(500,110Z)(600,0110)&lt;/signal&gt;<br>
 * </blockquote>
 * In this example this interface must contain measure unit: <code>ns</code>, duration:
 * <code>1000</code> and a collection of signal that describes signals in testbench
 * inducement. Therefor one signal in a signal collection must contain information
 * about the following line:
 * <i>&lt;signal name="A" type="scalar"&gt;(0,0)(100,1)(150,0)(300,1)&lt;/signal&gt;</i>!
 * This is true for every other signal in signal collection.
 * 
 * <h3>Restrictions</h3>
 * 
 * A <code>Collection</code> of <code>Signal</code> must not contain two or more 
 * signals that share the same name (ignore case)! Duration must not be negative
 * and measure unit must only contain following values:
 * <ul>
 * <li>fs
 * <li>ps
 * <li>ns
 * <li>us
 * <li>ms
 * <li>s
 * </ul>
 * 
 * @author Miro Bezjak
 * @see Signal
 * @see Impulse
 */
public interface Generator {
	
	/**
	 * Returns a list of signals. Your collection must now be returned as a
	 * {@link java.util.List list}. Returned list must be sorted according
	 * to the way signals were added (declared).
	 * <p>
	 * A returned list of signals must be read-only to ensure consistency!
	 * 
	 * @return a list of signals.
	 * @see Signal
	 */
	List<Signal> getSignals();
	
	/**
	 * Returns a master duration for all signal changes. In other words, every
	 * signal change after master duration was ignored.
	 * 
	 * @return a master duration.
	 */
	long getDuration();
	
	/**
	 * Returns a unit of measurement for signal change times. Measure unit can be one
	 * of the following:
	 * <ul>
	 * <li>fs
	 * <li>ps
	 * <li>ns
	 * <li>us
	 * <li>ms
	 * <li>s
	 * </ul>
	 * 
	 * @return a measure unit.
	 */
	String getMeasureUnit();
	
	/**
	 * Search a collection of signals and find a signal whose name equals <code>signalName</code>
	 * (ignore case). 
	 * 
	 * @param signalName a name of a signal.
	 * @return a signal that contains <code>signalName</code>; <code>null</code> if that signal does not exist.
	 * @throws NullPointerException if <code>signalName</code> is <code>null</code>.
	 * @see Signal
	 */
	Signal getSignal(String signalName);
	
	/**
	 * Compares this generator to the specified object. Returns
	 * <code>true</code> if and only if the specified object is also a
	 * <code>Generator</code> and if measure units, durations and signal
	 * lists are the same.
	 * 
	 * @param o the object to compare this <code>Generator</code> against.
	 * @return <code>true</code> if <code>Generator</code> are equal; <code>false</code> otherwise.
	 * @see Signal#equals(Object)
	 */
	boolean equals(Object o);
	
	/**
	 * Determine compatibility with {@linkplain CircuitInterface}. This method is used to
	 * determine if <code>Generator's Signals</code> contain the same information as
	 * <code>CircuitInterface's Ports</code>.
	 * <p>
	 * This <code>Generator</code> and <code>CircuitInterface</code> are compatible 
	 * if following is true:
	 * <ul>
	 * <li>Generator's signal collection is the same size as CircuitInterface's port collection 
	 *     (they have the same number of elements)
	 * <li>for every signal in signal collection there is exactly one "equal" port in port collection
	 * </ul>
	 * <p>
	 * A signal and a port are "equal" if following is true:
	 * <ul>
	 * <li>a signal name is equal (ignore case) to port name
	 * <li>if signal is a scalar then port must be a scalar aswell or if signal is a vector
	 *     then port must be a vector aswell and
	 * 		<ul>
	 * 		<li>{@link Signal#getRangeFrom() signal's rangeFrom} must be equal to {@link hr.fer.zemris.vhdllab.vhdl.model.Type#getRangeFrom() port's rangeFrom}
	 * 		<li>{@link Signal#getRangeTo() signal's rangeTo} must be equal to {@link hr.fer.zemris.vhdllab.vhdl.model.Type#getRangeTo() port's rangeTo}
	 * 		</ul>
	 * </ul>
	 * 
	 * @param ci a circuit interface to detemine compatibility with.
	 * @return <code>true</code> if this interface and <code>ci</code> are compatible; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>ci</code> is <code>null</code>.
	 * @see CircuitInterface#isCompatible(Generator)
	 * @see Signal
	 * @see hr.fer.zemris.vhdllab.vhdl.model.Port
	 */
	boolean isCompatible(CircuitInterface ci);
}
