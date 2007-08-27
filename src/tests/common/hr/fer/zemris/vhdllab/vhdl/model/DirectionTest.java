package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.utilities.SerializationUtil;

import org.junit.Ignore;
import org.junit.Test;

public class DirectionTest {
	
	/**
	 * Directions are equal.
	 */
	@Test
	public void equalsAndHashCode() {
		Direction dir1 = Direction.IN;
		Direction dir2 = Direction.IN;
		assertEquals(true, dir1.equals(dir2));
		assertEquals(dir1.hashCode(), dir2.hashCode());
	}
	
	/**
	 * Directions are not equal.
	 */
	@Test
	public void equalsAndHashCode2() {
		Direction dir1 = Direction.IN;
		Direction dir2 = Direction.OUT;
		assertEquals(false, dir1.equals(dir2));
		assertNotSame(dir1.hashCode(), dir2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		Direction dir = Direction.IN;
		assertEquals(false, dir.equals(null));
	}

	/**
	 * Object is not instance of Direction.
	 */
	@Test
	public void equalsObject2() {
		Direction dir = Direction.IN;
		assertEquals(false, dir.equals(new String("IN")));
	}

	/**
	 * Direction is IN.
	 */
	@Test
	public void isIN() {
		Direction dir = Direction.IN;
		assertEquals(true, dir.isIN());
	}
	
	/**
	 * Direction is not IN.
	 */
	@Test
	public void isIN2() {
		Direction dir = Direction.OUT;
		assertEquals(false, dir.isIN());
	}

	/**
	 * Direction is OUT.
	 */
	@Test
	public void isOUT() {
		Direction dir = Direction.OUT;
		assertEquals(true, dir.isOUT());
	}
	
	/**
	 * Direction is not OUT.
	 */
	@Test
	public void isOUT2() {
		Direction dir = Direction.IN;
		assertEquals(false, dir.isOUT());
	}
	
	/**
	 * Direction is INOUT.
	 */
	@Test
	public void isINOUT() {
		Direction dir = Direction.INOUT;
		assertEquals(true, dir.isINOUT());
	}
	
	/**
	 * Direction is not INOUT.
	 */
	@Test
	public void isINOUT2() {
		Direction dir = Direction.BUFFER;
		assertEquals(false, dir.isINOUT());
	}
	
	/**
	 * Direction is BUFFER.
	 */
	@Test
	public void isBUFFER() {
		Direction dir = Direction.BUFFER;
		assertEquals(true, dir.isBUFFER());
	}
	
	/**
	 * Direction is not BUFFER.
	 */
	@Test
	public void isBUFFER2() {
		Direction dir = Direction.INOUT;
		assertEquals(false, dir.isBUFFER());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization() {
		Direction dir = Direction.BUFFER;
		Direction dir2 = (Direction) SerializationUtil.serializeThenDeserializeObject(dir);
		
		assertEquals(dir, dir2);
		assertEquals(dir.hashCode(), dir2.hashCode());
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void testToString() {
		System.out.println("********************");
		System.out.println("Direction testing...");
		System.out.println("Testing method toString():");
		System.out.println(Direction.IN.toString());
		System.out.println("********************");
	}
	
}