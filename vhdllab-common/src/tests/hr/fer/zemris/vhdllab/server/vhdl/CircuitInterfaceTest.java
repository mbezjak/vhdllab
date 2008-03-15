package hr.fer.zemris.vhdllab.server.vhdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link CircuitInterface}.
 * 
 * @author Miro Bezjak
 */
public class CircuitInterfaceTest {

	private static final String NAME = "entity.name";
	private static Port PORT;
	static {
		Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(4,
				VectorDirection.DOWNTO, 1));
		PORT = new Port("port.name", PortDirection.IN, type);
	}

	private CircuitInterface ci;
	private List<Port> ports;

	@Before
	public void initEachTest() {
		ports = new ArrayList<Port>();
		ports.add(PORT);
		ci = new CircuitInterface(NAME, ports);
	}

	/**
	 * Entity name is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new CircuitInterface(null, ports);
	}

	/**
	 * Ports is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new CircuitInterface(NAME, null);
	}

	/**
	 * Port list contains null element
	 */
	@Test(expected = NullPointerException.class)
	public void constructor3() throws Exception {
		ports.add(null);
		new CircuitInterface(NAME, ports);
	}

	/**
	 * Port list contains duplicate element
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor4() throws Exception {
		ports.add(PORT);
		new CircuitInterface(NAME, ports);
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		assertEquals("getName", NAME, ci.getName());
	}

	/**
	 * Port name is null
	 */
	@Test(expected = NullPointerException.class)
	public void getPort() throws Exception {
		ci.getPort(null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void getPort2() throws Exception {
		Port retrievedPort = ci.getPort(PORT.getName());
		assertEquals("ports not equal", PORT, retrievedPort);
	}

	/**
	 * Port name is case insensitive.
	 */
	@Test
	public void getPort3() throws Exception {
		Port retrievedPort = ci.getPort(PORT.getName().toUpperCase());
		assertEquals("ports not equal", PORT, retrievedPort);
	}

	/**
	 * Unknown port name
	 */
	@Test
	public void getPort4() throws Exception {
		Port retrievedPort = ci.getPort("unknown.port.name");
		assertNull("port not null", retrievedPort);
	}

	/**
	 * getPorts returns an unmodifiable list
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void getPorts() throws Exception {
		ci.getPorts().remove(PORT);
	}

	/**
	 * Ports from circuit interface is immutable
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void iterator() throws Exception {
		Iterator<Port> iterator = ci.iterator();
		iterator.next();
		iterator.remove();
	}

	/**
	 * Test equals with self, null, and non-port object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", ci, ci);
		assertNotSame("port is equal to null.", ci, null);
		assertNotSame("can compare with string object.", ci, "a string object");
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		CircuitInterface newCi = new CircuitInterface(ci.getName(), ci
				.getPorts());

		assertEquals("circuit interface not equal", ci, newCi);
		assertEquals("circuit interface not equal", ci.hashCode(), newCi
				.hashCode());
	}

	/**
	 * Entity name is case insensitive.
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		CircuitInterface newCi = new CircuitInterface(ci.getName()
				.toUpperCase(), ci.getPorts());

		assertEquals("circuit interface not equal", ci, newCi);
		assertEquals("circuit interface not equal", ci.hashCode(), newCi
				.hashCode());
	}

	/**
	 * Name is different
	 */
	@Test
	public void hashCodeAndEquals3() throws Exception {
		CircuitInterface newCi = new CircuitInterface("new.entity.name", ci
				.getPorts());

		assertNotSame("circuit interface are equal", ci, newCi);
		assertNotSame("circuit interface are equal", ci.hashCode(), newCi
				.hashCode());
	}

	/**
	 * Ports is different
	 */
	@Test
	public void hashCodeAndEquals4() throws Exception {
		Port newPort = new Port("port2", PortDirection.OUT, new Type(
				TypeName.STD_LOGIC, Range.SCALAR));
		ports.add(newPort);
		CircuitInterface newCi = new CircuitInterface(ci.getName(), ports);

		assertNotSame("circuit interface are equal", ci, newCi);
		assertNotSame("circuit interface are equal", ci.hashCode(), newCi
				.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
		Field field = ci.getClass().getDeclaredField("name");
		field.setAccessible(true);
		field.set(ci, null); // set illegal state of result object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(ci);

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
		oos.writeObject(ci);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		CircuitInterface newCi = (CircuitInterface) bis.readObject();

		assertEquals("circuit interfaces not equal", ci, newCi);
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(ci.toString());
	}

}
