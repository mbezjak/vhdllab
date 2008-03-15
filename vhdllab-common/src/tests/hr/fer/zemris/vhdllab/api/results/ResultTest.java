package hr.fer.zemris.vhdllab.api.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import hr.fer.zemris.vhdllab.api.results.Message;
import hr.fer.zemris.vhdllab.api.results.MessageType;
import hr.fer.zemris.vhdllab.api.results.Result;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Result}.
 * 
 * @author Miro Bezjak
 */
public class ResultTest {

	private static final Integer STATUS = Integer.valueOf(5);
	private static final boolean SUCCESSFUL = false;
	private static final List<Message> MESSAGES = Collections.emptyList();

	private static Result<Message> result;
	private static Message message;

	@Before
	public void initEachTest() {
		result = new Result<Message>(STATUS, SUCCESSFUL, MESSAGES);
		message = new Message(MessageType.SUCCESSFUL, "successfull message",
				null);
	}

	/**
	 * Result is null
	 */
	@Test(expected = NullPointerException.class)
	public void copyConstructor() throws Exception {
		new Result<Message>(null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void copyConstructor2() throws Exception {
		Result<Message> newResult = new Result<Message>(result);
		assertEquals("results not equal.", newResult, result);
	}

	/**
	 * Messages is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new Result<Message>(SUCCESSFUL, null);
	}

	/**
	 * If result is successful status is 0, otherwise it is 1
	 */
	@Test
	public void constructor2() throws Exception {
		Result<Message> newResult;

		newResult = new Result<Message>(true, MESSAGES);
		assertEquals("status is not 0.", Integer.valueOf(0), newResult
				.getStatus());

		newResult = new Result<Message>(false, MESSAGES);
		assertEquals("status is not 1.", Integer.valueOf(1), newResult
				.getStatus());
	}

	/**
	 * Status is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor3() throws Exception {
		new Result<Message>(null, SUCCESSFUL, MESSAGES);
	}

	/**
	 * Messages is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor4() throws Exception {
		new Result<Message>(STATUS, SUCCESSFUL, null);
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		List<Message> messages = new ArrayList<Message>(1);
		messages.add(message);

		Result<Message> newResult = new Result<Message>(STATUS, SUCCESSFUL,
				messages);

		assertEquals("getStatus", STATUS, newResult.getStatus());
		assertEquals("isSuccessful", SUCCESSFUL, newResult.isSuccessful());
		assertEquals("getMessages", messages, newResult.getMessages());
	}

	/**
	 * Messages is unmodifiable list
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void messages() throws Exception {
		result.getMessages().add(message);
	}

	/**
	 * Messages are defensively copied
	 */
	@Test
	public void messages2() throws Exception {
		List<Message> messages = new ArrayList<Message>(1);
		Result<Message> newResult = new Result<Message>(Integer.valueOf(5),
				false, messages);

		messages.add(message);
		assertEquals("messages modified.", Collections.emptyList(), newResult
				.getMessages());
		assertNotSame("messages modified.", messages, newResult.getMessages());
	}

	/**
	 * Test equals with self, null, and non-result object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", result, result);
		assertNotSame("result is equal to null.", result, null);
		assertNotSame("can compare with string object.", result,
				"a string object");
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		Result<Message> newResult = new Result<Message>(result.getStatus(),
				result.isSuccessful(), result.getMessages());

		assertEquals("results not equal", result, newResult);
		assertEquals("results not equal", result.hashCode(), newResult
				.hashCode());
	}

	/**
	 * Status is different
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		Result<Message> newResult = new Result<Message>(Integer.valueOf(0),
				SUCCESSFUL, MESSAGES);

		assertNotSame("results are equal", result, newResult);
		assertNotSame("results are equal", result.hashCode(), newResult
				.hashCode());
	}

	/**
	 * Successful is different
	 */
	@Test
	public void hashCodeAndEquals3() throws Exception {
		Result<Message> newResult = new Result<Message>(STATUS, !SUCCESSFUL,
				MESSAGES);

		assertNotSame("results are equal", result, newResult);
		assertNotSame("results are equal", result.hashCode(), newResult
				.hashCode());
	}

	/**
	 * Messages is different
	 */
	@Test
	public void hashCodeAndEquals4() throws Exception {
		List<Message> messages = new ArrayList<Message>(1);
		messages.add(message);
		Result<Message> newResult = new Result<Message>(result.getStatus(),
				result.isSuccessful(), messages);

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
		Field field = result.getClass().getDeclaredField("status");
		field.setAccessible(true);
		field.set(result, null); // set illegal state of result object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(result);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		bis.readObject();
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(result.toString());
	}

}
