package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.tb.DefaultGenerator;
import hr.fer.zemris.vhdllab.vhdl.tb.DefaultImpulse;
import hr.fer.zemris.vhdllab.vhdl.tb.DefaultSignal;
import hr.fer.zemris.vhdllab.vhdl.tb.Generator;
import hr.fer.zemris.vhdllab.vhdl.tb.Signal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.tb.DefaultGenerator} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultGeneratorTest extends TestCase {

	public DefaultGeneratorTest(String name) {
		super(name);
	}

	/** Test constructor when inducement is null */
	public void testDefaultGenerator() {
		try {
			new DefaultGenerator(null);
			fail("No exception when inducement is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when measure unit is missing */
	public void testDefaultGenerator2() {
		String inducement = new String("\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when measure unit is missing.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when measure unit is not closed */
	public void testDefaultGenerator3() {
		String inducement = new String("<measureUnit>ns\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when measure unit is not closed.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when duration is missing */
	public void testDefaultGenerator4() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when duration is missing.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when duration is not closed */
	public void testDefaultGenerator5() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000<duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when duration is not closed.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when signal is missing */
	public void testDefaultGenerator6() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n");
		try {
			new DefaultGenerator(inducement);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when signal is not closed */
	public void testDefaultGenerator7() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when signal is not closed.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when whitespaces are one some places */
	public void testDefaultGenerator8() {
		String inducement = new String("   <measureUnit>ns</measureUnit>\n" +
				"  <duration>1000</duration>\n" +
				"<signal   name = \"A\"  type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\"   type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,  0)(50,1)  (300,0)(400,1)  </signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when line feeds are one some places */
	public void testDefaultGenerator9() {
		String inducement = new String("\n<measureUnit>ns</measureUnit>\n" +
				"\n<duration>1000</duration>\n" +
				"<signal \nname = \"A\" type=\"scalar\">(0,\n0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)\n(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)\n</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when undeslash is on some places */
	public void testDefaultGenerator10() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)_(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when underslash is on some places.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when inducement is correct */
	public void testDefaultGenerator11() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when rangeFrom in signal e is incorrect */
	public void testDefaultGenerator12() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"-1\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when rangeFrom in signal e is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when type in signal e is missing */
	public void testDefaultGenerator13() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when type in signal e is missing.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when type in signal a is missing */
	public void testDefaultGenerator14() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" >(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when type in signal a is missing.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when impulse in signal a is incorrect */
	public void testDefaultGenerator15() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100: 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when impulse in signal a is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when impulse in signal a is incorrect */
	public void testDefaultGenerator16() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, g)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when impulse in signal a is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when impulse in signal a is incorrect */
	public void testDefaultGenerator17() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when impulse in signal a is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when name in signal a is incorrect */
	public void testDefaultGenerator18() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"_A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when name in signal a is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when duration is incorrect */
	public void testDefaultGenerator19() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>-1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when duration is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when measure unit is incorrect */
	public void testDefaultGenerator20() {
		String inducement = new String("<measureUnit></measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		try {
			new DefaultGenerator(inducement);
			fail("No exception when measure unit is incorrect.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when signal e has no impulses */
	public void testDefaultGenerator21() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\"></signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when signal e has no impulses.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when signal a has no impulses */
	public void testDefaultGenerator22() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\"></signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		try {
			new DefaultGenerator(inducement);
			fail("No exception when signal a has no impulses.");
		} catch (ParseException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method equals(Object) and hashCode() when equals return true */
	public void testEqualsAndHashCode() {
		String inducement1 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g1 = null;
		try {
			g1 = new DefaultGenerator(inducement1);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		Generator g2 = null;
		try {
			g2 = new DefaultGenerator(inducement2);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		
		assertEquals(true, g1.equals(g2));
		assertEquals(g1.hashCode(), g2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false */
	public void testEqualsAndHashCode2() {
		String inducement1 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g1 = null;
		try {
			g1 = new DefaultGenerator(inducement1);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, 0)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");

		Generator g2 = null;
		try {
			g2 = new DefaultGenerator(inducement2);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		
		assertEquals(false, g1.equals(g2));
		assertNotSame(g1.hashCode(), g2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		boolean val = g.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof DefaultCircuitInterface */
	public void testEqualsObject2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		boolean val = g.equals(new String("signals"));
		assertEquals(false, val);
	}

	/** Test method getSignal(String) */
	public void testGetSignal() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		DefaultSignal s = new DefaultSignal("e", new int[] {0,2});
		s.addImpulse(new DefaultImpulse(0, "000"));
		s.addImpulse(new DefaultImpulse(100, "100"));
		s.addImpulse(new DefaultImpulse(400, "101"));
		s.addImpulse(new DefaultImpulse(500, "111"));
		s.addImpulse(new DefaultImpulse(600, "010"));
		
		assertEquals(s,	g.getSignal("e"));
	}

	/** Test method getSignals(). Try to add a signal from the outside */
	public void testGetSignals() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		Generator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.getSignals().add(new DefaultSignal("R", new int[] {0,2}, new DefaultImpulse(100, "010")));
			fail("Outside modification possible");
		} catch (UnsupportedOperationException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}

	/** Test method setDuration when duration is negative */
	public void testSetDuration() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.setDuration(-1);
			fail("No exception when duration is negative.");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method setDuration when duration is zero */
	public void testSetDuration2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.setDuration(0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method setDuration when duration is correct */
	public void testSetDuration3() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.setDuration(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}

	/** Test method setMeasureUnit when measure unit is null */
	public void testSetMeasureUnit() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.setMeasureUnit(null);
			fail("No exception when measure unit null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method setMeasureUnit when measure unit is not correct */
	public void testSetMeasureUnit2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.setMeasureUnit("as");
			fail("No exception when measure unit is not correct.");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method setMeasureUnit when measure unit is correct */
	public void testSetMeasureUnit3() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.setMeasureUnit("fs");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}

	/** Test method isCompatible() when circuit interface is null */
	public void testIsCompatible() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.isCompatible(null);
			fail("No exception on when circuit interface is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method isCompatible() when generator is correct */
	public void testIsCompatible2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" + 
				"<duration>1000</duration>" + 
				"<signal name=\"a\" type=\"scalar\">(0, 0)(100, Z)(500, U)(600, 1)(700, 0)</signal>" + 
				"<signal name=\"B\" type=\"scalar\">(0, 1)(100, Z)(400, U)(600, 0)(700, 1)</signal>" + 
				"<signal name=\"c\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0, 000)(100, Z00)(500, U01)(550, 111)(700, 100)</signal>" + 
				"<signal name=\"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"3\">(0, 0010)(1010, Z000)(500, U011)(550, 11z1)(700, 1010)</signal>" + 
				"<signal name=\"e\" type=\"scalar\">(0, 1)(100, 1)(200, 0)(350, 1)(500, z)</signal>" + 
				"<signal name=\"F\" type=\"vector\" rangeFrom=\"7\" rangeTo=\"0\">(0, 0010110)(1010, Z001100)(500, U0zz011)(550, 11011z1)(700, 1011010)</signal>");
		
		Generator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
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

	/** Test method addSignal(Signal) when signal is null */
	public void testAddSignal() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.addSignal(null);
			fail("No exception on when signal is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addSignal(Signal) when signal is correct */
	public void testAddSignal2() {
		String inducement1 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g1 = null;
		try {
			g1 = new DefaultGenerator(inducement1);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g1.addSignal(new DefaultSignal("Z", DefaultSignal.SCALAR_RANGE, new DefaultImpulse(0, "0")));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
		
		String inducement2 = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>"+
				"<signal name = \"z\" type= \"scalar\">(0 , 0)</signal>");
		DefaultGenerator g2 = null;
		try {
			g2 = new DefaultGenerator(inducement2);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		assertEquals(g2, g1);
	}

	/** Test method addSignalList(ListOfSignal) when signal list is null */
	public void testAddSignalList() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		try {
			g.addSignalList(null);
			fail("No exception on when signal list is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addSignalList(ListOfSignal) when signal list is correct */
	public void testAddSignalList2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		List<Signal> signalsAdd = new ArrayList<Signal>();
		signalsAdd.add(new DefaultSignal("j", new DefaultImpulse(0, "0")));
		signalsAdd.add(new DefaultSignal("k", new int[] {2,0}, new DefaultImpulse(50, "000")));
		signalsAdd.add(new DefaultSignal("l", new int[] {0,3}, new DefaultImpulse(200, "1010")));
		
		try {
			g.addSignalList(signalsAdd);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
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
		DefaultGenerator g2 = null;
		try {
			g2 = new DefaultGenerator(inducement2);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		assertEquals(g, g2);
	}

	/** Test method writeInducement() */
	public void testWriteInducement() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		DefaultGenerator g2 = null;
		try {
			g2 = new DefaultGenerator(g.writeInducement());
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		assertEquals(g, g2);
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	public void testToString() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"2\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator g = null;
		try {
			g = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		System.out.println("********************");
		System.out.println("DefaultGenerator testing...");
		System.out.println("Testing method toString():");
		System.out.println(g);
		System.out.println("********************");
	}
}
