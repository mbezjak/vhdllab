package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class describes testbench inducement. It consists of measure unit,
 * duration and a {@linkplain java.util.List} of {@linkplain Signal}.
 * <p>
 * Testbech inducement will contain information regarding signal changes depending
 * on time. Measure unit will contain a unit of measurement for signal change times.
 * A duration will contain master duration for all signal changes and a list
 * of signals will contain information on all signals.
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
 * In this example this class will contain measure unit: <code>ns</code>, duration:
 * <code>1000</code> and a list of signals that describes signals in testbench
 * inducement. Therefor one signal in a signal list must contain information about
 * the following line:
 * <i>&lt;signal name="A" type="scalar"&gt;(0,0)(100,1)(150,0)(300,1)&lt;/signal&gt;</i>!
 * This is true for every other signal in signal list.
 * 
 * <h3>Restrictions</h3>
 * 
 * A <code>List</code> of <code>Signal</code> will not contain two or more 
 * signals that share the same name (ignore case)! Duration will not be negative
 * and measure unit will only contain following values:
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
public class DefaultGenerator implements Generator {

	/** A unit of measurement for signal change times. */
	private String measureUnit;
	
	/** 
	 * A master duration for all signal changes. In other words, every signal
	 * change after master duration will be ignored.
	 */
	private long duration;
	
	/** A list of signals that describes inducement. */
	private List<Signal> signalList = new ArrayList<Signal>();
	
	/** A read-only list of signals that is used for return. */
	private List<Signal> signalList_ro = Collections.unmodifiableList(signalList);
	
	/** A map of signals used for searching for a particular signal. */
	private Map<String, Signal> signalMap = new HashMap<String, Signal>();
	
	/**
	 * Construct a Generator out of testbench inducement. Testbech inducement must
	 * contain information regarding signal changes depending on time.
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
	 * In this example this class will contain measure unit: <code>ns</code>, duration:
	 * <code>1000</code> and a list of signals that describes signals in testbench
	 * inducement. Therefor one signal in a signal list will contain information about
	 * the following line:
	 * <i>&lt;signal name="A" type="scalar"&gt;(0,0)(100,1)(150,0)(300,1)&lt;/signal&gt;</i>!
	 * This is true for every other signal in signal list.
	 * 
	 * @param inducement an inducement to populate this instance with.
	 * @throws ParseException if inducement is not of correct format.
	 */
	public DefaultGenerator(String inducement) throws ParseException {
		parseInducement(inducement);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Generator#getSignal(java.lang.String)
	 */
	public Signal getSignal(String signalName) {
		if( signalName == null ) throw new NullPointerException("Signal name can not be null.");
		return signalMap.get(signalName.toLowerCase());
	}
	
	/**
	 * Returns a list of signals. Returned list is sorted according
	 * to the way signals were added. A returned list of signals is
	 * read-only to ensure consistency!
	 * 
	 * @return a list of signals.
	 * @see Signal
	 */
	public List<Signal> getSignals() {
		return signalList_ro;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Generator#getMeasureUnit()
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Generator#getDuration()
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Set a master duration for all signal changes. In other words, every signal
	 * change after master duration will be ignored.
	 * 
	 * @param duration a master duration.
	 * @throws IllegalArgumentException if <code>duration</code> is <code>negative</code>.
	 */
	public void setDuration(long duration) {
		if( duration < 0 ) throw new IllegalArgumentException("Duration can not be negative.");
		this.duration = duration;
	}

	/**
	 * Set a unit of measurement for signal change times. Measure unit must be one
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
	 * @param measureUnit a measure unit.
	 * @throws NullPointerException is <code>measureUnit</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>measureUnit</code> is incorrect.
	 */
	public void setMeasureUnit(String measureUnit) {
		if( measureUnit == null ) throw new NullPointerException("Measure unit can not be null.");
		if( !StringUtil.isMeasureUnit( measureUnit ) ) throw new IllegalArgumentException("Measure unit is incorrect.");
		this.measureUnit = measureUnit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Generator) ) return false;
		Generator other = (Generator) o;
		
		return other.getMeasureUnit().equals(this.measureUnit)
			&& other.getDuration() == this.duration
			&& other.getSignals().size() == this.signalList.size()
			&& other.getSignals().equals(this.signalList);
	}
	
	/**
	 * Returns a hash code value for this <code>DefaultGenerator</code>
	 * instance. The hash code of <code>DefaultGenerator</code> instance
	 * is hash code of measure unit XOR with hash code of duration XOR
	 * with hash code of signal list.
	 * <p>
	 * This ensures that <code>dg1.equals(dg2)</code> implies that 
	 * <code>dg1.hashCode() == dg2.hashCode()</code> for any two 
	 * DefaultGenerators, <code>dg1</code> and <code>dg2</code>, as
	 * required by the general contract of  <code>Object.hashCode</code>.
	 * 
	 * @return a hash code value for this <code>DefaultGenerator</code> instance.
	 * @see java.lang.String#hashCode()
	 * @see java.lang.Long#hashCode()
	 * @see java.util.List#hashCode()
	 */
	@Override
	public int hashCode() {
		return measureUnit.hashCode() ^ Long.valueOf(duration).hashCode() ^ signalList.hashCode();
	}
	
	/**
	 * Returns a string representing detailed description of this 
	 * <code>DefaultGenerator</code> instance. Returned string 
	 * will have the following format:
	 * <p>
	 * GENERATOR DURATION: --duration here--, MEASURE UNIT: --measure unit here--, CONTAINS --signal list size here-- SIGNALS:<br>
	 * --string representing every signal in signal list here--
	 * 
	 * @return a string representing detailed description of this <code>DefaultGenerator</code> instance.
	 */
	@Override
	public String toString() {
		StringBuilder retval = new StringBuilder( 50 + signalList.toString().length() );
		retval.append("GENERATOR DURATION: ").append(duration)
			.append(", MEASURE UNIT: ").append(measureUnit)
			.append(", CONTAINS ").append(signalList.size()).append(" SIGNALS:\n");
		for(Signal signal : signalList) {
			retval.append(signal.toString()).append("\n");
		}
		return retval.toString();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.vhdl.model.tb.Generator#isCompatible(hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface)
	 */
	public boolean isCompatible(CircuitInterface ci) {
		if( ci == null ) throw new NullPointerException("Circuit interface can not be null.");
		if( signalList.size() != ci.getPorts().size() ) return false;
		for(Port port : ci.getPorts()) {
			Signal signal = getSignal(port.getName()); //O(1)
			if( signal == null ) return false;
			
			if( signal.isScalar() && port.getType().isScalar() ) continue;
			if( signal.isVector() && port.getType().isVector()
				&& signal.getRangeFrom() == port.getType().getRangeFrom()
				&& signal.getRangeTo() == port.getType().getRangeTo() ) continue;
			return false;
		}
		return true;
	}
	
	/**
	 * Appends the specified signal to the end of this list.
	 * 
	 * @param s a signal that will be added.
	 * @return <code>true</code> if <code>s</code> has been added; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>s</code> is <code>null</code>.
	 * @see #addSignalList(List)
	 */
	public boolean addSignal(Signal s) {
		if( s == null ) throw new NullPointerException("Signal can not be null.");
		if( getSignal(s.getName()) != null ) return false;
		signalList.add(s);
		signalMap.put(s.getName().toLowerCase(), s);
		return true;
	}
	
	/**
	 * Appends all of the elements of <code>signals</code> to this list.
	 * 
	 * @param signals a signals list that will to be added.
	 * @return <code>true</code> if at least one signal in <code>signals</code> has been added; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>signals</code> is <code>null</code>.
	 * @see #addSignal(Signal)
	 */
	public boolean addSignalList(List<Signal> signals) {
		if( signals == null ) throw new NullPointerException("Signal list can not be null.");
		boolean retval = false;
		for(Signal s : signals) {
			if( addSignal(s) ) retval = true;
		}
		return retval;
	}
	
	/**
	 * Returns an inducement that describes this instance. Returned inducement will have
	 * exactly the same format as <code>Generator</code> instance is prescribing.
	 * 
	 * @return an inducement that describes this instance.
	 */
	public String writeInducement() {
		StringBuilder inducement = new StringBuilder( this.toString().length() );
		
		inducement.append("<measureUnit>").append(measureUnit).append("</measureUnit>\n");
		inducement.append("<duration>").append(duration).append("</duration>\n");
		
		for(Signal signal : signalList) {
			inducement.append("<signal")
				.append(" name=\"").append(signal.getName()).append("\"");
			if( signal.isVector() ) {
				inducement.append(" type=\"vector\"")
					.append(" rangeFrom=\"").append(signal.getRangeFrom()).append("\"")
					.append(" rangeTo=\"").append(signal.getRangeTo()).append("\"");
			}
			else inducement.append(" type=\"scalar\"");
			inducement.append(">");
			
			for(Impulse i : signal.getExciter()) {
				inducement.append("(").append(i.getTime()).append(",")
					.append(i.getState()).append(")");
			}
			inducement.append("</signal>\n");
		}
		return inducement.toString();
	}
	
	/**
	 * Parse inducement and populate this instance with results of parsing.
	 * 
	 * @param inducement an inducement to populate this instance with.
	 * @throws ParseException if inducement is not of correct format.
	 */
	private void parseInducement(String inducement) throws ParseException {
		if( inducement == null ) throw new NullPointerException("Inducement can not be null.");

		inducement = inducement.toLowerCase();
		inducement = inducement.replaceAll("[\n]+", " ");
		inducement = inducement.replaceAll("[ ]+", " ");
		inducement = inducement.trim();
		try {
			setMeasureUnit( parseMeasureUnit(inducement) );
			setDuration( parseDuration(inducement) );

			int offset = 0;
			while(true) {
				String code = parseCode(inducement, offset);
				if( code == null ) break;
				offset = inducement.indexOf(code) + code.length();
				String tag = parseTag(code);
				String paragraph = parseParagraph(code);
				
				String name = parseName(tag);
				int[] range = parseRange(tag);
				List<Impulse> exciter = parseExciter(paragraph, duration);
				addSignal(new DefaultSignal(name, range, exciter));
			}
		} catch (RuntimeException e) {
			throw new ParseException(e.getMessage(), 0);
		}
		if(getSignals().size()==0) throw new ParseException("Must have at least one signal.", 0);
	}
	
	/**
	 * Extract paragraph of measure unit tag.
	 * 
	 * @param inducement an inducement to populate this instance with.
	 * @return a measure unit.
	 * @throws ParseException if measure unit tag does not exists or is not closed.
	 */
	private String parseMeasureUnit(String inducement) throws ParseException {
		int start = inducement.indexOf("<measureunit>");
		if( start == -1 ) throw new ParseException("Tag measureUnit does not exist.", 0);
		int end = inducement.indexOf("</measureunit>");
		if( end == -1 ) throw new ParseException("Tag measureUnit is never closed.", 0);
		return inducement.substring(start + "<measureunit>".length(), end);
	}
	
	/**
	 * Extract paragraph of duration tag.
	 * 
	 * @param inducement an inducement to populate this instance with.
	 * @return a master duration for all signals.
	 * @throws ParseException if duration tag does not exists, is not closed or it is unable to parse a duration.
	 */
	private long parseDuration(String inducement) throws ParseException {
		int start = inducement.indexOf("<duration>");
		if( start == -1 ) throw new ParseException("Tag duration does not exist.", 0);
		int end = inducement.indexOf("</duration>");
		if( end == -1 ) throw new ParseException("Tag duration is never closed.", 0);
		long duration = -1;
		try {
			duration = Long.parseLong(inducement.substring(start + "<duration>".length(), end));
		} catch(NumberFormatException e) {
			throw new ParseException("Unable to parse duration.", 0);
		}
		return duration;
	}
	
	/**
	 * Extract a declaration of a signal from inducement and start the search from 
	 * <code>offset</code> position.
	 * 
	 * @param inducement an inducement to populate this instance with.
	 * @param offset from where to start the search for a code.
	 * @return a declaration of a signal; <code>null</code> if there is no more declared signals.
	 * @throws ParseException if signal tag is never closed.
	 */
	private String parseCode(String inducement, int offset) throws ParseException {
		int start = inducement.indexOf("<signal ", offset);
		if( start == -1 ) return null;
		int end = inducement.indexOf("</signal>", offset);
		if( end == -1 ) throw new ParseException("Tag signal is never closed.", 0);
		return inducement.substring(start, end + "</signal>".length());
	}
	
	/**
	 * Extract a tag from a code.
	 * 
	 * @param code a declaration of a signal from where to extract a tag.
	 * @return a tag of a code.
	 */
	private String parseTag(String code) {
		int end = code.indexOf('>');
		return code.substring(0, end + 1);
	}
	
	/**
	 * Extract a paragraph from a code.
	 * 
	 * @param code a declaration of a signal from where to extract a paragraph.
	 * @return a paragraph of a code.
	 */
	private String parseParagraph(String code) {
		int start = code.indexOf('>');
		int end = code.indexOf('<', start);
		return code.substring(start + 1, end);
	}

	/**
	 * Extract an attribute name from a tag.
	 * 
	 * @param tag a tag from where to extract an attribute name.
	 * @return a name of a signal.
	 * @throws ParseException if attribute name is missing.
	 */
	private String parseName(String tag) throws ParseException {
		int start = tag.indexOf(" name");
		if( start == -1 ) throw new ParseException("Missing attribute: name.", 0);
		start = tag.indexOf('\"', start);
		int end = tag.indexOf('\"', start + 1);
		return tag.substring(start + 1, end);
	}
	
	/**
	 * Extract an attribute type from a tag. Type can be:
	 * <ul>
	 * <li>scalar
	 * <li>vector
	 * </ul>
	 * 
	 * @param tag a tag from where to extract an attribute type.
	 * @return a type of a signal.
	 * @throws ParseException if attribute type is missing or if extracted type is not scalar nor vector.
	 */
	private String parseType(String tag) throws ParseException {
		int start = tag.indexOf(" type");
		if( start == -1 ) throw new ParseException("Missing attribute: type.", 0);
		start = tag.indexOf('\"', start);
		int end = tag.indexOf('\"', start + 1);
		String type = tag.substring(start + 1, end);
		if( !(type.equals("scalar") || type.equals("vector")) ) throw new ParseException("Type is not scalar nor vector.", 0);
		return type;
	}

	/**
	 * Create an integer array from a rangeFrom and rangeTo.
	 * 
	 * @param tag a tag from where to create a rangeFrom.
	 * @return a bounds of a vector; <code>null</code> if signal is a scalar.
	 * @throws ParseException if attribute rangeFrom or rangeTo is missing while signal is declared
	 * 		   as s vector or rangeFrom if different from rangeTo while signal is declared as a scalar. 
	 */
	private int[] parseRange(String tag) throws ParseException {
		String type = parseType(tag);
		int rangeFrom = parseRangeFrom(tag);
		int rangeTo = parseRangeTo(tag);

		if( type.equals("scalar") ) {
			if( rangeFrom != rangeTo ) throw new ParseException("Incorrect declaration of a scalar.", 0);
			else return null;
		} else { // type.equals("vector")
			if( rangeFrom == -1 || rangeTo == -1 ) throw new ParseException("Missing attribute: rangeFrom or rangeTo.", 0);
			else return new int[] {rangeFrom, rangeTo};
		}
	}
	
	/**
	 * Extract an attribute rangeFrom from a tag.
	 * 
	 * @param tag a tag from where to extract an attribute rangeFrom.
	 * @return a rangeFrom of a signal; <code>-1</code> if that attribute does not exist.
	 * @throws ParseException if it is unable to extract rangeFrom.
	 */
	private int parseRangeFrom(String tag) throws ParseException {
		int start = tag.indexOf(" rangefrom");
		if( start == -1 ) return -1;
		start = tag.indexOf('\"', start);
		int end = tag.indexOf('\"', start + 1);
		int retval = -1;
		try {
			retval = Integer.parseInt(tag.substring(start + 1, end));
		} catch (NumberFormatException e) {
			throw new ParseException("Unable to parse rangeFrom.", 0);
		}
		return retval;
	}
	
	/**
	 * Extract an attribute rangeTo from a tag.
	 * 
	 * @param tag a tag from where to extract an attribute rangeTo.
	 * @return a rangeTo of a signal; <code>-1</code> if that attribute does not exist.
	 * @throws ParseException if it is unable to extract rangeTo.
	 */
	private int parseRangeTo(String tag) throws ParseException {
		int start = tag.indexOf(" rangeto");
		if( start == -1 ) return -1;
		start = tag.indexOf('\"', start);
		int end = tag.indexOf('\"', start + 1);
		int retval = -1;
		try {
			retval = Integer.parseInt(tag.substring(start + 1, end));
		} catch (NumberFormatException e) {
			throw new ParseException("Unable to parse rangeTo.", 0);
		}
		return retval;
	}

	/**
	 * Extract a list of impulses and create an exciter. Every signal
	 * change after master duration will be ignored.
	 * 
	 * @param paragraph a paragraph from where to extract a list of impulses
	 * @param dur a master duration.
	 * @return an exciter.
	 * @throws ParseException if impulse if not of correct format.
	 */
	private List<Impulse> parseExciter(String paragraph, long dur) throws ParseException {
		List<Impulse> exciter = new ArrayList<Impulse>();
		paragraph = paragraph.trim();
		paragraph = paragraph.replaceAll("^[(]", ""); //remove left bracket
		paragraph = paragraph.replaceAll("[)]$", ""); //remove right bracket
		String[] impulse = paragraph.split("[)][ ]?[(]");
		for( int i = 0; i < impulse.length; i++ ) {
			try {
				impulse[i] = impulse[i].trim();
				String[] value = impulse[i].split(",");
				value[0] = value[0].trim();
				value[1] = value[1].trim();
				
				if( Long.parseLong(value[0]) <= dur ) {
					exciter.add(new DefaultImpulse(Long.parseLong(value[0]), value[1]));
				} else break;
			} catch(RuntimeException e) {
				throw new ParseException("Error while parsing impulse "+(i+1), 0);
			}
		}
		return exciter;
	}
}