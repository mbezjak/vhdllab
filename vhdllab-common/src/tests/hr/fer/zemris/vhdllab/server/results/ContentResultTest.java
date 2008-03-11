package hr.fer.zemris.vhdllab.server.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link ContentResult}.
 * 
 * @author Miro Bezjak
 */
public class ContentResultTest {

	private static final Integer STATUS = Integer.valueOf(5);
	private static final boolean SUCCESSFUL = false;
	private static final List<Message> MESSAGES = Collections.emptyList();
	private static final String CONTENT = "a result content";

	private static ContentResult<Message> result;

	@Before
	public void initEachTest() {
		result = new ContentResult<Message>(STATUS, SUCCESSFUL, MESSAGES,
				CONTENT);
	}

	/**
	 * Result is null
	 */
	@Test(expected = NullPointerException.class)
	public void copyConstructor() throws Exception {
		new ContentResult<Message>(null, CONTENT);
	}

	/**
	 * Content is null
	 */
	@Test(expected = NullPointerException.class)
	public void copyConstructor2() throws Exception {
		new ContentResult<Message>(result, null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void copyConstructor3() throws Exception {
		ContentResult<Message> newResult = new ContentResult<Message>(result,
				CONTENT);
		assertEquals("results not equal.", newResult, result);
	}

	/**
	 * Content is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new ContentResult<Message>(SUCCESSFUL, MESSAGES, null);
	}

	/**
	 * Content is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new ContentResult<Message>(STATUS, SUCCESSFUL, MESSAGES, null);
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		assertEquals(CONTENT, result.getContent());
	}

	/**
	 * Test equals with self, null, and non-contentresult object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", result, result);
		assertNotSame("result is equal to null.", result, null);
		assertNotSame("can compare with string object.", result,
				"a string object");
		assertNotSame("can compare with result object.", result,
				new Result<Message>(result));
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		ContentResult<Message> newResult = new ContentResult<Message>(result,
				CONTENT);

		assertEquals("results not equal", result, newResult);
		assertEquals("results not equal", result.hashCode(), newResult
				.hashCode());
	}

	/**
	 * Content is different
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		ContentResult<Message> newResult = new ContentResult<Message>(result,
				"new.content");

		assertNotSame("results are equal", result, newResult);
		assertNotSame("results are equal", result.hashCode(), newResult
				.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
		Field field = result.getClass().getDeclaredField("content");
		field.setAccessible(true);
		field.set(result, null); // set illegal state of message object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(result);

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
		Field field = result.getClass().getSuperclass().getDeclaredField(
				"status");
		field.setAccessible(true);
		field.set(result, null); // set illegal state of message object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(result);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		bis.readObject();
	}

}
