package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.Direction} class.
 * 
 * @author Miro Bezjak
 */
public class DirectionTest extends TestCase {
	
	public DirectionTest(String name) {
		super(name);
	}
	
	/** Test method equals(Object) and hashCode() when equals return true */
	public void testEqualsAndHashCode() {
		Direction dir1 = Direction.IN;
		Direction dir2 = Direction.IN;
		assertEquals(true, dir1.equals(dir2));
		assertEquals(dir1.hashCode(), dir2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false */
	public void testEqualsAndHashCode2() {
		Direction dir1 = Direction.IN;
		Direction dir2 = Direction.OUT;
		assertEquals(false, dir1.equals(dir2));
		assertNotSame(dir1.hashCode(), dir2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		Direction dir = Direction.IN;
		boolean val = dir.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof Direction */
	public void testEqualsObject2() {
		Direction dir = Direction.IN;
		boolean val = dir.equals(new String("IN"));
		assertEquals(false, val);
	}

	/** Test method isIN() if direction is IN */
	public void testIsIN() {
		Direction dir = Direction.IN;
		assertEquals(true, dir.isIN());
	}
	
	/** Test method isIN() if direction is not IN */
	public void testIsIN2() {
		Direction dir = Direction.OUT;
		assertEquals(false, dir.isIN());
	}

	/** Test method isOUT() if direction is OUT */
	public void testIsOUT() {
		Direction dir = Direction.OUT;
		assertEquals(true, dir.isOUT());
	}
	
	/** Test method isOUT() if direction is not OUT */
	public void testIsOUT2() {
		Direction dir = Direction.IN;
		assertEquals(false, dir.isOUT());
	}
	
	/** Test method isINOUT() if direction is INOUT */
	public void testIsINOUT() {
		Direction dir = Direction.INOUT;
		assertEquals(true, dir.isINOUT());
	}
	
	/** Test method isINOUT() if direction is not INOUT */
	public void testIsINOUT2() {
		Direction dir = Direction.BUFFER;
		assertEquals(false, dir.isINOUT());
	}
	
	/** Test method isBUFFER() if direction is BUFFER */
	public void testIsBUFFER() {
		Direction dir = Direction.BUFFER;
		assertEquals(true, dir.isBUFFER());
	}
	
	/** Test method isBUFFER() if direction is not BUFFER */
	public void testIsBUFFER2() {
		Direction dir = Direction.INOUT;
		assertEquals(false, dir.isBUFFER());
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	public void testToString() {
		System.out.println("********************");
		System.out.println("Direction testing...");
		System.out.println("Testing method toString():");
		System.out.println(Direction.IN.toString());
		System.out.println("********************");
	}
}
