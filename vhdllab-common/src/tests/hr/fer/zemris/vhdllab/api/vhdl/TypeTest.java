package hr.fer.zemris.vhdllab.api.vhdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Type}.
 * 
 * @author Miro Bezjak
 */
public class TypeTest {

	private static final Range RANGE = new Range(4, VectorDirection.DOWNTO, 1);
	private static final TypeName TYPE_NAME = TypeName.STD_LOGIC_VECTOR;

	private Type type;

	@Before
	public void initEachTest() {
		type = new Type(TYPE_NAME, RANGE);
	}

	/**
	 * Type name is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new Type(null, RANGE);
	}

	/**
	 * Range is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new Type(TYPE_NAME, null);
	}

	/**
	 * Type name is STD_LOGIC while range is a vector
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor3() throws Exception {
		new Type(TypeName.STD_LOGIC, RANGE);
	}

	/**
	 * Type name is STD_LOGIC_VECTOR while range is a scalar
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor4() throws Exception {
		new Type(TypeName.STD_LOGIC_VECTOR, Range.SCALAR);
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		assertEquals("getTypeName.", TYPE_NAME, type.getTypeName());
		assertEquals("getRange.", RANGE, type.getRange());
	}

	/**
	 * Test equals with self, null, and non-type object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", type, type);
		assertNotSame("type is equal to null.", type, null);
		assertNotSame("can compare with string object.", type,
				"a string object");
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		Type newType = new Type(type.getTypeName(), type.getRange());

		assertEquals("types not equal.", type, newType);
		assertEquals("types not equal.", type.hashCode(), newType.hashCode());
	}

	/**
	 * Range is different
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		Type newType = new Type(TypeName.STD_LOGIC, Range.SCALAR);

		assertNotSame("types are equal.", type, newType);
		assertNotSame("types are equal.", type.hashCode(), newType.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
		Field field = type.getClass().getDeclaredField("range");
		field.setAccessible(true);
		field.set(type, null); // set illegal state of result object
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(type);
		
		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		bis.readObject();
	}
	
	/**
	 * Serialize then deserialize
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void readResolve2() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(type);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		Type newType = (Type) bis.readObject();

		assertEquals("types names are not equal.", type.getTypeName(), newType
				.getTypeName());
		assertEquals("types not equal.", type, newType);
	}

	/**
	 * Range is a vector.
	 */
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(type.toString());
	}

	/**
	 * Range is a scalar.
	 */
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString2() {
		Type newType = new Type(TypeName.STD_LOGIC, Range.SCALAR);
		System.out.println(newType.toString());
	}

}
