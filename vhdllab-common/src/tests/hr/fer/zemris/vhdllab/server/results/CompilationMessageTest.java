package hr.fer.zemris.vhdllab.server.results;

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
 * A test case for {@link CompilationMessage}.
 * 
 * @author Miro Bezjak
 */
public class CompilationMessageTest {

	private static final MessageType TYPE = MessageType.SUCCESSFUL;
	private static final String TEXT = "message text";
	private static final String ENTITY_NAME = "entity.name";
	private static final int ROW = 15;
	private static final int COLUMN = 3;

	private static CompilationMessage message;

	@Before
	public void initEachTest() {
		message = new CompilationMessage(TYPE, TEXT, ENTITY_NAME, ROW, COLUMN);
	}

	/**
	 * Message is null
	 */
	@Test(expected = NullPointerException.class)
	public void copyConstructor() throws Exception {
		new Message(null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void copyConstructor2() throws Exception {
		CompilationMessage newMessage = new CompilationMessage(message, ROW,
				COLUMN);
		assertEquals("messages not equal.", newMessage, message);
	}

	/**
	 * row is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void copyConstructor3() throws Exception {
		new CompilationMessage(message, -1, COLUMN);
	}

	/**
	 * column is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void copyConstructor4() throws Exception {
		new CompilationMessage(message, ROW, -1);
	}

	/**
	 * Message type is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new CompilationMessage(null, TEXT, ROW, COLUMN);
	}

	/**
	 * Message text is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new CompilationMessage(TYPE, null, ROW, COLUMN);
	}

	/**
	 * row is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor3() throws Exception {
		new CompilationMessage(TYPE, TEXT, -1, COLUMN);
	}

	/**
	 * column is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor4() throws Exception {
		new CompilationMessage(TYPE, TEXT, ROW, -1);
	}

	/**
	 * Message type is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor5() throws Exception {
		new CompilationMessage(null, TEXT, ENTITY_NAME, ROW, COLUMN);
	}

	/**
	 * Message text is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor6() throws Exception {
		new CompilationMessage(TYPE, null, ENTITY_NAME, ROW, COLUMN);
	}

	/**
	 * row is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor7() throws Exception {
		new CompilationMessage(TYPE, TEXT, ENTITY_NAME, -1, COLUMN);
	}

	/**
	 * column is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor8() throws Exception {
		new CompilationMessage(TYPE, TEXT, ENTITY_NAME, ROW, -1);
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		assertEquals("getRow", ROW, message.getRow());
		assertEquals("getColumn", COLUMN, message.getColumn());
	}

	/**
	 * Test equals with self, null, and non-compilationmessage object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", message, message);
		assertNotSame("message is equal to null.", message, null);
		assertNotSame("can compare with string object.", message,
				"a string object");
		assertNotSame("can compare with message object.", message, new Message(
				TYPE, TEXT));
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		CompilationMessage newMessage = new CompilationMessage(message, ROW,
				COLUMN);

		assertEquals("messages not equal", message, newMessage);
		assertEquals("messages not equal", message.hashCode(), newMessage
				.hashCode());
	}

	/**
	 * Row is different
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		CompilationMessage newMessage = new CompilationMessage(message, 5,
				COLUMN);

		assertNotSame("messages are equal", message, newMessage);
		assertNotSame("messages are equal", message.hashCode(), newMessage
				.hashCode());
	}

	/**
	 * Column is different
	 */
	@Test
	public void hashCodeAndEquals3() throws Exception {
		CompilationMessage newMessage = new CompilationMessage(message, ROW, 5);

		assertNotSame("messages are equal", message, newMessage);
		assertNotSame("messages are equal", message.hashCode(), newMessage
				.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void readResolve() throws Exception {
		Field field = message.getClass().getDeclaredField("row");
		field.setAccessible(true);
		field.set(message, -1); // set illegal state of message object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(message);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		bis.readObject();
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve2() throws Exception {
		Field field = message.getClass().getSuperclass().getDeclaredField(
				"type");
		field.setAccessible(true);
		field.set(message, null); // set illegal state of message object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(message);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		bis.readObject();
	}

	/**
	 * With entity name
	 */
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(message.toString());
	}

	/**
	 * Without entity name
	 */
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString2() {
		message = new CompilationMessage(TYPE, TEXT, ROW, COLUMN);
		System.out.println(message.toString());
	}

}
