package hr.fer.zemris.vhdllab.vhdl.tb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DefaultGeneratorTest {

	private static String inducement;
	private static Generator gen;
	
	@BeforeClass
	public static void init() throws ParseException {
		inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		gen = new DefaultGenerator(inducement);
	}
	
	/**
	 * Inducement is <code>null</code>.
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void constructor() throws ParseException {
		new DefaultGenerator(null);
	}
	
	/**
	 * Measure unit is missing.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor2() throws ParseException {
		String inducement = new String("\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Measure unit is not closed.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor3() throws ParseException {
		String inducement = new String("<measureUnit>ns\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Duration is missing.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor4() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Duration is not closed.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor5() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000<duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Signal is missing.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor6() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Signal is not closed.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor7() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Inducement has whitespaces.
	 * @throws ParseException 
	 */
	@Test
	public void constructor8() throws ParseException {
		String inducement = new String("   <measureUnit>ns</measureUnit>\n" +
				"  <duration>1000</duration>\n" +
				"<signal   name = \"A\"  type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\"   type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,  0)(50,1)  (300,0)(400,1)  </signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Inducement has line feeds.
	 * @throws ParseException 
	 */
	@Test
	public void constructor9() throws ParseException {
		String inducement = new String("\n<measureUnit>ns</measureUnit>\n" +
				"\n<duration>1000</duration>\n" +
				"<signal \nname = \"A\" type=\"scalar\">(0,\n0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)\n(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)\n</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Inducement has underslashes.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor10() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)_(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Inducement is correct.
	 * @throws ParseException 
	 */
	@Test
	public void constructor11() throws ParseException {
		new DefaultGenerator(inducement);
	}
	
	/**
	 * rangeFrom in signal 'e' is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor12() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"-1\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * type in signal 'e' is missing.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor13() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * type in signal 'a' is missing.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor14() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" >(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Impulse in signal 'a' is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor15() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100: 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Impulse in signal 'a' is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor16() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, g)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Impulse in signal 'a' is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor17() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * signal 'a' is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor18() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"_A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Duration is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor19() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>-1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Measure unit is incorrect.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor20() throws ParseException {
		String inducement = new String("<measureUnit></measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * signal 'e' has no impulses.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor21() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\"></signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * signal 'a' has no impulses.
	 * @throws ParseException 
	 */
	@Test(expected=ParseException.class)
	public void constructor22() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\"></signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		new DefaultGenerator(inducement);
	}
	
	/**
	 * Generators are equal.
	 * @throws ParseException 
	 */
	@Test
	public void equalsAndHashCode() throws ParseException {
		String inducement1 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g1 = new DefaultGenerator(inducement1);
		
		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		Generator g2 = new DefaultGenerator(inducement2);
		
		assertEquals(true, g1.equals(g2));
		assertEquals(g1.hashCode(), g2.hashCode());
	}
	
	/**
	 * Generators are not equal.
	 * @throws ParseException 
	 */
	@Test
	public void equalsAndHashCode2() throws ParseException {
		String inducement1 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g1 = new DefaultGenerator(inducement1);

		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, 0)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		Generator g2 = new DefaultGenerator(inducement2);
		
		assertEquals(false, g1.equals(g2));
		assertNotSame(g1.hashCode(), g2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		assertEquals(false, gen.equals(null));
	}
	
	/**
	 * Object is not instance of CircuitInterface.
	 */
	@Test
	public void equalsObject2() {
		assertEquals(false, gen.equals(new String("signals")));
	}
	
	@Test
	public void getSignal() {
		DefaultSignal s = new DefaultSignal("e", new int[] {0,2});
		s.addImpulse(new DefaultImpulse(0, "000"));
		s.addImpulse(new DefaultImpulse(100, "100"));
		s.addImpulse(new DefaultImpulse(400, "101"));
		s.addImpulse(new DefaultImpulse(500, "111"));
		s.addImpulse(new DefaultImpulse(600, "010"));
		
		assertEquals(s,	gen.getSignal("e"));
	}

	/**
	 * Try to add a signal after method getSignals().
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void getSignals() {
		gen.getSignals().add(new DefaultSignal("R", new int[] {0,2}, new DefaultImpulse(100, "010")));
	}

	/**
	 * Duration is negative.
	 * @throws ParseException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void setDuration() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.setDuration(-1);
	}
	
	/**
	 * Duration is zero.
	 * @throws ParseException 
	 */
	@Test
	public void setDuration2() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.setDuration(0);
	}
	
	/**
	 * Duration is correct.
	 * @throws ParseException 
	 */
	@Test
	public void setDuration3() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.setDuration(1);
	}

	/**
	 * Measure unit is <code>null</code>.
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void setMeasureUnit() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.setMeasureUnit(null);
	}
	
	/**
	 * Measure unit is not correct.
	 * @throws ParseException 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void setMeasureUnit2() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.setMeasureUnit("as");
	}
	
	/**
	 * Measure unit is correct.
	 * @throws ParseException 
	 */
	@Test
	public void setMeasureUnit3() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.setMeasureUnit("fs");
	}

	/**
	 * Circuit interface is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void isCompatible() {
		gen.isCompatible(null);
	}
	
	/**
	 * Circuit interface is correct.
	 * @throws ParseException 
	 */
	@Test
	public void isCompatible2() throws ParseException {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" + 
				"<duration>1000</duration>" + 
				"<signal name=\"a\" type=\"scalar\">(0, 0)(100, Z)(500, U)(600, 1)(700, 0)</signal>" + 
				"<signal name=\"B\" type=\"scalar\">(0, 1)(100, Z)(400, U)(600, 0)(700, 1)</signal>" + 
				"<signal name=\"c\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0, 000)(100, Z00)(500, U01)(550, 111)(700, 100)</signal>" + 
				"<signal name=\"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"3\">(0, 0010)(1010, Z000)(500, U011)(550, 11z1)(700, 1010)</signal>" + 
				"<signal name=\"e\" type=\"scalar\">(0, 1)(100, 1)(200, 0)(350, 1)(500, z)</signal>" + 
				"<signal name=\"F\" type=\"vector\" rangeFrom=\"7\" rangeTo=\"0\">(0, 0010110)(1010, Z001100)(500, U0zz011)(550, 11011z1)(700, 1011010)</signal>");
		Generator g = new DefaultGenerator(inducement);
		
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		ports.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		assertEquals(true, g.isCompatible(ci));		
	}

	/**
	 * Signal is null.
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void addSignal() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.addSignal(null);
	}
	
	/**
	 * Signal is correct.
	 * @throws ParseException 
	 */
	@Test
	public void addSignal2() throws ParseException {
		DefaultGenerator g1 = new DefaultGenerator(inducement);
		g1.addSignal(new DefaultSignal("Z", DefaultSignal.SCALAR_RANGE, new DefaultImpulse(0, "0")));
		
		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>"+
				"<signal name = \"z\" type= \"scalar\">(0 , 0)</signal>");
		Generator g2 = new DefaultGenerator(inducement2);
		
		assertEquals(g2, g1);
	}

	/**
	 * Signal list is <code>null</code>.
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void addSignalList() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		g.addSignalList(null);
	}
	
	/**
	 * Signal list is correct.
	 * @throws ParseException 
	 */
	@Test
	public void addSignalList2() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		
		List<Signal> signalsAdd = new ArrayList<Signal>();
		signalsAdd.add(new DefaultSignal("j", new DefaultImpulse(0, "0")));
		signalsAdd.add(new DefaultSignal("k", new int[] {2,0}, new DefaultImpulse(50, "000")));
		signalsAdd.add(new DefaultSignal("l", new int[] {0,3}, new DefaultImpulse(200, "1010")));
		g.addSignalList(signalsAdd);
		
		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>"+
				"<signal name = \"j\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)</signal>"+
				"<signal name = \"k\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(50,000)</signal>"+
				"<signal name = \"l\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"3\">(200,1010)</signal>");
		DefaultGenerator g2 = new DefaultGenerator(inducement2);

		assertEquals(g, g2);
	}

	@Test
	public void writeInducement() throws ParseException {
		DefaultGenerator g = new DefaultGenerator(inducement);
		Generator g2 = new DefaultGenerator(g.writeInducement());
		assertEquals(g, g2);
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
		System.out.println("********************");
		System.out.println("DefaultGenerator testing...");
		System.out.println("Testing method toString():");
		System.out.println(gen);
		System.out.println("********************");
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DefaultGeneratorTest.class);
	}
	
}