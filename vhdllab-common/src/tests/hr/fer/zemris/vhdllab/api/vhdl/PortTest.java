package hr.fer.zemris.vhdllab.api.vhdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.api.vhdl.VectorDirection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Port}.
 * 
 * @author Miro Bezjak
 */
public class PortTest {

	private static final String NAME = "port.name";
	private static final PortDirection DIRECTION = PortDirection.IN;
	private static final Type TYPE = new Type(TypeName.STD_LOGIC_VECTOR,
			new Range(4, VectorDirection.DOWNTO, 1));

	private Port port;

	@Before
	public void initEachTest() {
		port = new Port(NAME, DIRECTION, TYPE);
	}

	/**
	 * Port name is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new Port(null, DIRECTION, TYPE);
	}

	/**
	 * Port direction is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new Port(NAME, null, TYPE);
	}

	/**
	 * Type is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor3() throws Exception {
		new Port(NAME, DIRECTION, null);
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		assertEquals("getName", NAME, port.getName());
		assertEquals("getDirection", DIRECTION, port.getDirection());
		assertEquals("getType", TYPE, port.getType());
	}

	/**
	 * Test equals with self, null, and non-port object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", port, port);
		assertNotSame("port is equal to null.", port, null);
		assertNotSame("can compare with string object.", port,
				"a string object");
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		Port newPort = new Port(port.getName(), port.getDirection(), port
				.getType());

		assertEquals("port not equal", port, newPort);
		assertEquals("port not equal", port.hashCode(), newPort.hashCode());
	}

	/**
	 * Port name is case insensitive.
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		Port newPort = new Port(port.getName().toUpperCase(), port
				.getDirection(), port.getType());

		assertEquals("port not equal", port, newPort);
		assertEquals("port not equal", port.hashCode(), newPort.hashCode());
	}

	/**
	 * Name is different
	 */
	@Test
	public void hashCodeAndEquals3() throws Exception {
		Port newPort = new Port("new.port.name", port.getDirection(), port
				.getType());

		assertNotSame("port are equal", port, newPort);
		assertNotSame("port are equal", port.hashCode(), newPort.hashCode());
	}

	/**
	 * Direction is different
	 */
	@Test
	public void hashCodeAndEquals4() throws Exception {
		Port newPort = new Port(port.getName(), PortDirection.OUT, port
				.getType());

		assertNotSame("port are equal", port, newPort);
		assertNotSame("port are equal", port.hashCode(), newPort.hashCode());
	}

	/**
	 * Type is different
	 */
	@Test
	public void hashCodeAndEquals5() throws Exception {
		Type newType = new Type(TypeName.STD_LOGIC, Range.SCALAR);
		Port newPort = new Port(port.getName(), port.getDirection(), newType);

		assertNotSame("port are equal", port, newPort);
		assertNotSame("port are equal", port.hashCode(), newPort.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
		Field field = port.getClass().getDeclaredField("name");
		field.setAccessible(true);
		field.set(port, null); // set illegal state of result object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(port);

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
		oos.writeObject(port);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		Port newPort = (Port) bis.readObject();

		assertEquals("ports not equal", port, newPort);
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(port.toString());
	}

}