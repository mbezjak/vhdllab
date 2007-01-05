package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.StringFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class describes one signal of {@link Generator inducement}.
 * It consists of signal name, range (signal type) and a
 * {@linkplain java.util.List} of {@linkplain Impulse} also known
 * as exciter.
 * 
 * <h3>Example</h3>
 * 
 * Here is an example of declaring a signal in inducement:
 * <blockquote>
 * &lt;signal name="a" type="scalar"&gt;(0,0)(100,1)(300,0)(400,1)(800,0)&lt;/signal&gt;
 * </blockquote>
 * In this example this class will contain signal name: <code>a</code>,
 * range that describes signal as a scalar (<code>null</code> value) and a
 * list of impulses that describes this particular exciter. Therefor one
 * impulse in an exciter will contain information about the following impuse:
 * <i>(0,0)</i>! This is true for every other impulse in exciter.
 * <p>
 * Another example, but with declaring a vector:
 * <blockquote>
 * &lt;signal name="b" type="vector" rangeFrom="2" rangeTo="0"&gt;(0,000)(200,001)(300,010)(400,100)(800,101)(900,111)&lt;/signal&gt;
 * </blockquote>
 * In this example this class will contain signal name: <code>b</code>,
 * range that describes vector bounds: 2, 0 and a list of impulses that
 * describes this particular exciter. Therefor one impulse in an exciter will
 * contain information about the following impuse:
 * <i>(0,000)</i>! This is true for every other impulse in exciter.
 * 
 * <h3>Restrictions</h3>
 * 
 * Signal name will have the following format:
 * <ul>
 * <li>it will contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underscore (_) characters
 * <li>it will not start with a non-alpha character
 * <li>it will not end with an underscore character
 * <li>it will not contain an underscore character after an underscore character
 * </ul>
 * When describing a scalar, range will contain <code>null</code> value thus
 * uniformly describing signal as a scalar and when describing a vector, it
 * will contain exactly two elemets: index of lower bound of a vector
 * (rangeFrom) and index of upper bound of a vector (rangeTo) both of which
 * will not be negative numbers thus uniformly describing bounds of a vector.
 * <p>
 * An exciter will not contain two or more {@link Impulse#equals(Object) equal}
 * impulses and it will be sorted ascending by time. Two neighborly impulses in
 * exciter will not share same state. Also state lenght (number of bits) will
 * match range. For example:
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
public class DefaultSignal implements Signal {
	
	/**
	 * Static field that uniformly describes signal as a scalar by setting <code>range</code>
	 * to <code>null</code>.
	 */
	public static final int[] SCALAR_RANGE = null;

	/**
	 * Static field that uniformly describes type of a signal as a scalar.
	 */
	public static final String SCALAR = new String("scalar");
	
	/**
	 * Static field that uniformly describes type of a signal as a vector.
	 */
	public static final String VECTOR = new String("vector");
	
	/**	A name of a signal. */
	private String signalName;
	
	/** 
	 * Signal range. It contains either information that signal is a scalar (by containing
	 * <code>null</code> value) or vector bounds. First element of this array contains
	 * index of lower bound of a vector (rangeFrom) while the second element contains
	 * index of upper bound of a vector (rangeTo).
	 */
	private int[] range;
	
	/** A list of impulses that describes exciter. */
	private List<Impulse> exciter = new ArrayList<Impulse>();
	
	/** A read-only list of impulses that is used for return. */
	private List<Impulse> exciter_ro = Collections.unmodifiableList(exciter);
	
	/** A map of impulses used for searching for a particular impulse. */
	private Map<Long, Impulse> exciterMap = new HashMap<Long, Impulse>();
	
	/**
	 * Create an instance of this class using only signal name to describe
	 * a signal. Exciter will remain empty and range will be set so that
	 * it describes this signal as a scalar (to value {@linkplain #SCALAR_RANGE}).
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * A <code>name</code> must be of the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain an underscore character after an underscore character
	 * </ul>
	 * 
	 * @param name a name of a signal.
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>name</code> is not of the correct format.
	 */
	public DefaultSignal(String name) {
		createDefaultSignal(name, DefaultSignal.SCALAR_RANGE, null);
	}

	/**
	 * Create an instance of this class using signal name and range to describe
	 * a signal. Exciter will remain empty.
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
	 * <p>
	 * Range must uniformly describe signal as a scalar or bounds of a vector,
	 * whichever is the case. Depending on the declaration of range, it can be
	 * <code>null</code>. In that case a <code>null</code> value uniformly
	 * describes signal as a scalar. When describing a vector it must contain
	 * exactly two elemets: index of lower bound of a vector (rangeFrom) and
	 * index of upper bound of a vector (rangeTo) both of which must not be
	 * negative numbers.
	 * 
	 * @param name a name of a signal.
	 * @param range bounds of a vector or <code>null</code> if port is a scalar.
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 * @throws IllegalArgumentException if one of the following is true:
	 * 			<ul>
	 * 			<li><code>name</code> is not of correct format
	 * 			<li><code>range</code> does not have exactly two elements.
	 * 			<li>at least one element of <code>range</code> is negative.
	 * 			</ul>
	 */
	public DefaultSignal(String name, int[] range) {
		createDefaultSignal(name, range, null);
	}
	
	/**
	 * Create an instance of this class using signal name and exciter to describe
	 * a signal. Exciter's contents will be added to intern exciter. Range will be
	 * set so that it describes this signal as a scalar (to value {@linkplain #SCALAR_RANGE}).
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
	 * <p>
	 * Every impulse in exciter that does not abide to following rules will not be added to
	 * internal exciter:
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
	 * @param name a name of a signal.
	 * @param exciter a list of impulses that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>exciter</code> is <code>null</code>.
	 * @see #addExciter(List)
	 */
	public DefaultSignal(String name, List<Impulse> exciter) {
		if( exciter == null ) throw new NullPointerException("Exciter can not be null.");
		createDefaultSignal(name, DefaultSignal.SCALAR_RANGE, exciter);
	}

	/**
	 * Create an instance of this class using signal name, range and exciter to
	 * describe a signal. Exciter's contents will be added to intern exciter.
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
	 * <p>
	 * Range must uniformly describe signal as a scalar or bounds of a vector,
	 * whichever is the case. Depending on the declaration of range, it can be
	 * <code>null</code>. In that case a <code>null</code> value uniformly
	 * describes signal as a scalar. When describing a vector it must contain
	 * exactly two elemets: index of lower bound of a vector (rangeFrom) and
	 * index of upper bound of a vector (rangeTo) both of which must not be
	 * negative numbers.
	 * <p>
	 * Every impulse in exciter that does not abide to following rules will not be
	 * added to internal exciter:
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
	 * @param name a name of a signal.
	 * @param range bounds of a vector or <code>null</code> if port is a scalar.
	 * @param exciter a list of impulses that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>exciter</code> is <code>null</code>.
	 * @throws IllegalArgumentException if one of the following is true:
	 * 			<ul>
	 * 			<li><code>name</code> is not of correct format
	 * 			<li><code>range</code> does not have exactly two elements.
	 * 			<li>at least one element of <code>range</code> is negative.
	 * 			</ul>
	 * @see #addExciter(List)
	 */
	public DefaultSignal(String name, int[] range, List<Impulse> exciter) {
		if( exciter == null ) throw new NullPointerException("Exciter can not be null.");
		createDefaultSignal(name, range, exciter);
	}
	
	/**
	 * Create an instance of this class using signal name and impulse to describe
	 * a signal. Impulse will be added to intern exciter. Range will be set so that
	 * it describes this signal as a scalar (to value {@linkplain #SCALAR_RANGE}).
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
	 * <p>
	 * An impulse will not be added if state lenght of does not match range,
	 * for example:
	 * <ul>
	 * <li>if signal is a scalar then <code>i</code> must contain state that
	 * 	   has only one bit
	 * <li>if signal is a vector then <code>i</code> must contain state that
	 * 	   has exactly <code>Math.abs(rangeFrom - rangeTo + 1)</code> bits
	 * </ul>
	 * 
	 * @param name a name of a signal.
	 * @param impulse an impulse that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>impulse</code> is <code>null</code>.
	 * @see #addImpulse(Impulse)
	 */
	public DefaultSignal(String name, Impulse impulse) {
		if( impulse == null ) throw new NullPointerException("Impulse can not be null.");
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(impulse);
		createDefaultSignal(name, DefaultSignal.SCALAR_RANGE, exciter);
	}
	
	/**
	 * Create an instance of this class using signal name, range and impulse to
	 * describe a signal. Impulse will be added to intern exciter.
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
	 * <p>
	 * Range must uniformly describe signal as a scalar or bounds of a vector,
	 * whichever is the case. Depending on the declaration of range, it can be
	 * <code>null</code>. In that case a <code>null</code> value uniformly
	 * describes signal as a scalar. When describing a vector it must contain
	 * exactly two elemets: index of lower bound of a vector (rangeFrom) and
	 * index of upper bound of a vector (rangeTo) both of which must not be
	 * negative numbers.
	 * <p>
	 * An impulse will not be added if state lenght of does not match range,
	 * for example:
	 * <ul>
	 * <li>if signal is a scalar then <code>i</code> must contain state that
	 * 	   has only one bit
	 * <li>if signal is a vector then <code>i</code> must contain state that
	 * 	   has exactly <code>Math.abs(rangeFrom - rangeTo + 1)</code> bits
	 * </ul>
	 * 
	 * @param name a name of a signal.
	 * @param range bounds of a vector or <code>null</code> if port is a scalar.
	 * @param impulse an impulse that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>impulse</code> is <code>null</code>.
	 * @throws IllegalArgumentException if one of the following is true:
	 * 			<ul>
	 * 			<li><code>name</code> is not of correct format
	 * 			<li><code>range</code> does not have exactly two elements.
	 * 			<li>at least one element of <code>range</code> is negative.
	 * 			</ul>
	 * @see #addImpulse(Impulse)
	 */
	public DefaultSignal(String name, int[] range, Impulse impulse) {
		if( impulse == null ) throw new NullPointerException("Impulse can not be null.");
		List<Impulse> exciter = new ArrayList<Impulse>();
		exciter.add(impulse);
		createDefaultSignal(name, range, exciter);
	}
	
	/**
	 * A private constructor.
	 * <p>
	 * All of the above constructors are redirected to this private method because 
	 * they are doing similar jobs. For details see class constructors above.
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
	 * <p>
	 * Range must uniformly describe signal as a scalar or bounds of a vector,
	 * whichever is the case. Depending on the declaration of range, it can be
	 * <code>null</code>. In that case a <code>null</code> value uniformly
	 * describes signal as a scalar. When describing a vector it must contain
	 * exactly two elemets: index of lower bound of a vector (rangeFrom) and
	 * index of upper bound of a vector (rangeTo) both of which must not be
	 * negative numbers.
	 * 
	 * @param name a name of a signal.
	 * @param range bounds of a vector or <code>null</code> if port is a scalar.
	 * @param exciter a list of impulses that will be added.
	 * @throws NullPointerException if <code>name</code> or <code>exciter</code> is <code>null</code>.
	 * @throws IllegalArgumentException if one of the following is true:
	 * 			<ul>
	 * 			<li><code>name</code> is not of correct format
	 * 			<li><code>range</code> does not have exactly two elements.
	 * 			<li>at least one element of <code>range</code> is negative.
	 * 			</ul>
	 * @see #addExciter(List)
	 */
	private void createDefaultSignal(String name, int[] range, List<Impulse> exciter) {
		if( name == null ) throw new NullPointerException("Name can not be null.");
		if( !StringFormat.isCorrectEntityName(name) ) throw new IllegalArgumentException("Signal name is not of correct format.");
		if( range != null && (range.length != 2 || range[0] < 0 || range[1] < 0) ) throw new IllegalArgumentException("Range does not have two elements or at least one of them is negative.");
		
		this.signalName = name;
		if( range == null ) this.range = DefaultSignal.SCALAR_RANGE;
		else this.range = new int[] {range[0], range[1]};
		if( exciter != null ) this.addExciter(exciter);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Signal#getName()
	 */
	public String getName() {
		return signalName;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Signal#getRangeFrom()
	 */
	public int getRangeFrom() {
		if( !isVector() ) throw new IllegalStateException("Scalars do not have a range.");
		return range[0];
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Signal#getRangeTo()
	 */
	public int getRangeTo() {
		if( !isVector() ) throw new IllegalStateException("Scalars do not have a range.");
		return range[1];
	}

	/**
	 * Returns a list of impulses. Returned list is sorted according
	 * to the way impulses were added. A returned list of impulses is
	 * read-only to ensure consistency!
	 * 
	 * @return a list of impulses.
	 * @see Impulse
	 */
	public List<Impulse> getExciter() {
		return exciter_ro;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Signal#isScalar()
	 */
	public boolean isScalar() {
		return range == null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Signal#isVector()
	 */
	public boolean isVector() {
		return range != null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Signal) ) return false;
		Signal other = (Signal) o;
		
		if( !(other.getName().equalsIgnoreCase(this.signalName)
			&& other.getExciter().size() == this.exciter.size()
			&& other.getExciter().equals(this.exciter)) ) return false;
		
		if( other.isScalar() && this.isScalar() ) return true;
		return other.isVector() && this.isVector()
				&& other.getRangeFrom() == this.getRangeFrom()
				&& other.getRangeTo() == this.getRangeTo();
	}

	/**
	 * Returns a hash code value for this <code>DefaultSignal</code> instance.
	 * The hash code of <code>DefaultSignal</code> instance is hash code of 
	 * signal name (ignore case) XOR with hash code of rangeFrom XOR with hash
	 * code of rangeTo XOR with hash code of exciter if port is a vector. If
	 * port is a scalar then hash code of <code>DefaultSignal</code> instance
	 * is hash code of signal name (ignore case) XOR with hash code of exciter.
	 * <p>
	 * This ensures that <code>ds1.equals(ds2)</code> implies that 
	 * <code>ds1.hashCode() == ds2.hashCode()</code> for any two DefaultSignals, 
	 * <code>ds1</code> and <code>ds2</code>, as required by the general 
	 * contract of <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>DefaultSignal</code> instance.
	 * @see java.lang.String#hashCode()
	 * @see java.lang.List#hashCode()
	 */
	@Override
	public int hashCode() {
		if( isVector() ) return signalName.toLowerCase().hashCode() ^ Integer.valueOf(getRangeFrom()).hashCode()
							^ Integer.valueOf(getRangeTo()).hashCode() ^ exciter.hashCode();
		else return signalName.toLowerCase().hashCode() ^ exciter.hashCode();
	}
	
	/**
	 * Returns a string representing detailed description of this
	 * <code>DefaultSignal</code> instance. Returned string will have
	 * the following format:
	 * <p>
	 * SIGNAL NAME: --signal name here--, RANGE: --string representing range and vector direction here--, CONTAINS --exciter size here-- IMPULSES:<br>
	 * --string representing every impulse in impulse list here--
	 * 
	 * @return a string representing detailed description of this <code>DefaultSignal</code> instance.
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 30 + signalName.length() + exciter.toString().length() );
		retval.append("SIGNAL NAME: ").append(signalName).append(", RANGE: ");
		if( isScalar() ) retval.append("scalar (no range)");
		else {
			retval.append(getRangeFrom());
			if( getRangeFrom() >= getRangeTo() ) retval.append(" DOWNTO ");
			else retval.append(" TO ");
			retval.append(getRangeTo());
		}
		retval.append(", CONTAINS ").append(exciter.size()).append(" IMPULSES:\n");
		for(Impulse i : exciter) {
			retval.append(i.toString()).append("\n");
		}
		return retval.toString();
	}
	
	/**
	 * Appends the specified impulse to the end of this list.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * An impulse <code>i</code> will not be added if one of the following is true:
	 * <ul>
	 * <li>if exciter already contains this impulse
	 * <li>if <code>i</code> has time less then last impulse of exciter
	 * <li>if <code>i</code> shares the same state as last impulse of exciter
	 * <li>if state lenght of <code>i</code> does not match range, for example:
	 * 		<ul>
	 * 		<li>if signal is a scalar then <code>i</code> must contain state that
	 * 			has only one bit
	 * 		<li>if signal is a vector then <code>i</code> must contain state that
	 * 			has exactly <code>Math.abs(rangeFrom - rangeTo + 1)</code> bits
	 * 		</ul>
	 * </ul>
	 * 
	 * @param i a impulse that will be added.
	 * @return <code>true</code> if <code>i</code> has been added; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>i</code> is <code>null</code>.
	 * @see #addExciter(List)
	 */
	public boolean addImpulse(Impulse i) {
		if( i == null ) throw new NullPointerException("Impulse can not be null.");
		if( isScalar() && i.getState().length() != 1 ) return false;
		if( isVector() && (Math.abs(this.getRangeFrom() - this.getRangeTo()) + 1) != i.getState().length() ) return false;

		if( !exciter.isEmpty() ) {
			Impulse lastImpulse = exciter.get(exciter.size()-1);
			if( lastImpulse.getTime() > i.getTime() ) return false;
			if( lastImpulse.getState().equalsIgnoreCase(i.getState()) ) return false;
		}
		exciter.add(i);
		exciterMap.put(Long.valueOf(i.getTime()), i);
		return true;
	}
	
	/**
	 * Appends all of the elements of <code>exciter</code> to this list.
	 * Impulses from <code>exciter</code> will be added one at a time.
	 * Every impulse in exciter that does not abide to following rules will
	 * not be added to internal exciter:
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
	 * @param exciter an exciter that will to be added.
	 * @return <code>true</code> if at least one impulse in <code>exciter</code> has been added; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>exciter</code> is <code>null</code>.
	 * @see #addImpulse(Impulse)
	 */
	public boolean addExciter(List<Impulse> exciter) {
		if( exciter == null ) throw new NullPointerException("Exciter can not be null.");
		boolean retval = false;
		for(Impulse i : exciter) {
			if( addImpulse(i) ) retval = true;
		}
		return retval;
	}
	
	/**
	 * Search exciter and find first impulse whose time is greater then
	 * <code>time</code>. In order to find an impulse at time 0 it is possible
	 * for <code>time</code> to be <code>-1</code>. In that case this method will
	 * find first impulse in exciter (most likely at time 0). (Other then <code>-1</code>,
	 * <code>time</code> must not be negative)
	 * 
	 * @param time a time from where the search begins.
	 * @return an impulse whose time is greater then <code>time</code>; <code>null</code> if that impulse does not exist.
	 * @throws IllegalArgumentException if <code>time</code> is less then <code>-1</code>.
	 * @see Impulse
	 */
	public Impulse getImpulseAfterTime(long time) {
		if( time < -1 ) throw new IllegalArgumentException("Time can not be less then -1.");
		
		if( exciter.size() == 0 ) return null;
		if( time == -1 ) return exciter.get(0);
		int index = exciter.indexOf(getImpulseAtTime(time)) + 1;
		if( index >= exciter.size() ) return null;
		return exciter.get(index);
	}
	
	/**
	 * Search exciter and find an impulse whose time equals <code>time</code>.
	 * 
	 * @param time a time when impulse changed.
	 * @return an impulse that contains <code>time</code>; <code>null</code> if that impulse does not exist.
	 * @throws IllegalArgumentException if <code>time</code> is <code>negative</code>.
	 * @see Impulse
	 */
	public Impulse getImpulseAtTime(long time) {
		if( time < 0 ) throw new IllegalArgumentException("Time can not be negative.");
		return exciterMap.get(Long.valueOf(time));
	}
}