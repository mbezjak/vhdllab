package hr.fer.zemris.vhdllab.vhdl.tb;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.tb.DefaultImpulse} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultImpulseTest extends TestCase {

	public DefaultImpulseTest(String name) {
		super(name);
	}

	/** Test constructor when time is negative */
	public void testDefaultImpulse() {
		try {
			new DefaultImpulse(-1, "0");
			fail("No exception when time is negative");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when time is zero */
	public void testDefaultImpulse2() {
		try {
			new DefaultImpulse(0, "0");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when status is null */
	public void testDefaultImpulse3() {
		try {
			new DefaultImpulse(1, null);
			fail("No exception when status is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when status is unknown */
	public void testDefaultImpulse4() {
		try {
			new DefaultImpulse(1, "019-");
			fail("No exception when status is unknown");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when status is zero-lenght */
	public void testDefaultImpulse5() {
		try {
			new DefaultImpulse(1, "");
			fail("No exception when status is zero-lenght");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test constructor when status is correct */
	public void testDefaultImpulse6() {
		try {
			new DefaultImpulse(1, DefaultImpulse.knownStateValues);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated Exception: "+e.getMessage());
		}
	}
	
	/** Test method equals(Object) and hashCode() when equals return true */
	public void testEqualsAndHashCode() {
		DefaultImpulse impulse1 = new DefaultImpulse(100, "1z10");
		DefaultImpulse impulse2 = new DefaultImpulse(100, "1z10");
		assertEquals(true, impulse1.equals(impulse2));
		assertEquals(impulse1.hashCode(), impulse2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false */
	public void testEqualsAndHashCode2() {
		DefaultImpulse impulse1 = new DefaultImpulse(100, "1010");
		DefaultImpulse impulse2 = new DefaultImpulse(200, "1010");
		assertEquals(false, impulse1.equals(impulse2));
		assertNotSame(impulse1.hashCode(), impulse2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false */
	public void testEqualsAndHashCode3() {
		DefaultImpulse impulse1 = new DefaultImpulse(100, "1010");
		DefaultImpulse impulse2 = new DefaultImpulse(100, "0110");
		assertEquals(false, impulse1.equals(impulse2));
		assertNotSame(impulse1.hashCode(), impulse2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		DefaultImpulse impulse = new DefaultImpulse(100, "1010");
		boolean val = impulse.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof DefaultType */
	public void testEqualsObject2() {
		DefaultImpulse impulse = new DefaultImpulse(100, "1010");
		boolean val = impulse.equals(new String("1010"));
		assertEquals(false, val);
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 *//*
	public void testToString() {
		DefaultImpulse impulse = new DefaultImpulse(100, "01001011");
		System.out.println("********************");
		System.out.println("DefaultImpulse testing...");
		System.out.println("Testing method toString():");
		System.out.println(impulse);
		System.out.println("********************");
	}*/
}
