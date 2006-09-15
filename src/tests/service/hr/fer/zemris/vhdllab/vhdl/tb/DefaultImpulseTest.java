package hr.fer.zemris.vhdllab.vhdl.tb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DefaultImpulseTest {
	
	private static Impulse impulse;
	
	@BeforeClass
	public static void init() {
		impulse = new DefaultImpulse(100, "1010");
	}
	
	/**
	 * Time is negative.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor() {
		new DefaultImpulse(-1, "0");
	}
	
	/**
	 * Time is zero.
	 */
	@Test
	public void constructor2() {
		new DefaultImpulse(0, "0");
	}
	
	/**
	 * Status is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor3() {
		new DefaultImpulse(1, null);
	}
	
	/**
	 * Status is unknown.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor4() {
		new DefaultImpulse(1, "019-");
	}
	
	/**
	 * Status us zero-lengh.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor5() {
		new DefaultImpulse(1, "");
	}
	
	/**
	 * Status and time is correct.
	 */
	@Test
	public void constructor6() {
		new DefaultImpulse(1, DefaultImpulse.knownStateValues);
	}
	
	/**
	 * Impulses are equal.
	 */
	@Test
	public void equalsAndHashCode() {
		Impulse impulse1 = new DefaultImpulse(100, "1z10");
		Impulse impulse2 = new DefaultImpulse(100, "1z10");
		assertEquals(true, impulse1.equals(impulse2));
		assertEquals(impulse1.hashCode(), impulse2.hashCode());
	}
	
	/**
	 * Impulses are not equal.
	 */
	@Test
	public void equalsAndHashCode2() {
		Impulse impulse1 = new DefaultImpulse(100, "1010");
		Impulse impulse2 = new DefaultImpulse(200, "1010");
		assertEquals(false, impulse1.equals(impulse2));
		assertNotSame(impulse1.hashCode(), impulse2.hashCode());
	}
	
	/**
	 * Impulses are not equal.
	 */
	@Test
	public void equalsAndHashCode3() {
		Impulse impulse1 = new DefaultImpulse(100, "1010");
		Impulse impulse2 = new DefaultImpulse(100, "0110");
		assertEquals(false, impulse1.equals(impulse2));
		assertNotSame(impulse1.hashCode(), impulse2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		assertEquals(false, impulse.equals(null));
	}
	
	/**
	 * Object is not instance of Type.
	 */
	@Test
	public void equalsObject2() {
		assertEquals(false, impulse.equals(new String("1010")));
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
		System.out.println("********************");
		System.out.println("DefaultImpulse testing...");
		System.out.println("Testing method toString():");
		System.out.println(impulse);
		System.out.println("********************");
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DefaultImpulseTest.class);
	}
	
}