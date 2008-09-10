package hr.fer.zemris.vhdllab.api.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
     * Message is null
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() {
        new SimulationMessage(null);
    }
    
    /**
     * Everything ok.
     */
    @Test
    public void copyConstructor2() {
        SimulationMessage copiedMessage = new SimulationMessage(message);
        assertEquals("message objects not same.", message, copiedMessage);
    }

    /**
     * Message is null or equals to wrong object type.
     */
    @Test
    public void equals() {
        assertFalse("message equals to null.", message.equals(null));
        assertFalse("message equals to string object.", message
                .equals("a string object"));
        assertFalse("compilation message equals to content result object.",
                message.equals(new Message(message)));
    }

	/**
	 * Simulate serialization tempering
	 */
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
	
    /**
     * Deserialization returns object of type SimulationMessage.
     */
    @Test
    public void readResolve2() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        Object readObject = bis.readObject();
        assertTrue("wrong object type.",
                readObject.getClass() == SimulationMessage.class);
        assertEquals("message objects not same.", message, readObject);
    }

}
