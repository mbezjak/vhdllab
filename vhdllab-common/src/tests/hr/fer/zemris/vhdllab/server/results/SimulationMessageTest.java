package hr.fer.zemris.vhdllab.server.results;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link SimulationMessage}.
 * 
 * @author Miro Bezjak
 */
public class SimulationMessageTest {

	private static final MessageType TYPE = MessageType.SUCCESSFUL;
	private static final String TEXT = "message text";
	private static final String ENTITY_NAME = "entity.name";

	private static SimulationMessage message;

	@Before
	public void initEachTest() {
		message = new SimulationMessage(TYPE, TEXT, ENTITY_NAME);
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
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

}
