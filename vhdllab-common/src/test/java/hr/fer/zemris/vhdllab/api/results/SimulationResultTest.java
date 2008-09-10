package hr.fer.zemris.vhdllab.api.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 * A test case for {@link SimulationResult}.
 * 
 * @author Miro Bezjak
 */
public class SimulationResultTest {

	private static final Integer STATUS = Integer.valueOf(5);
	private static final boolean SUCCESSFUL = false;
	private static final List<SimulationMessage> MESSAGES = Collections
			.emptyList();
	private static final String CONTENT = "a result content";

	private static SimulationResult result;

	@Before
	public void initEachTest() {
		result = new SimulationResult(STATUS, SUCCESSFUL, MESSAGES, CONTENT);
	}

    /**
     * Result is null
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() {
        new SimulationResult(null);
    }
    
    /**
     * Everything ok.
     */
    @Test
    public void copyConstructor2() {
        SimulationResult copiedResult = new SimulationResult(result);
        assertEquals("result objects not same.", result, copiedResult);
    }

    /**
     * Result is null or equals to wrong object type.
     */
    @Test
    public void equals() {
        assertFalse("result equals to null.", result.equals(null));
        assertFalse("result equals to string object.", result
                .equals("a string object"));
        assertFalse("compilation result equals to content result object.",
                result.equals(new ContentResult<SimulationMessage>(result)));
    }

	/**
	 * Simulate serialization tempering
	 */
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
		Field field = result.getClass().getSuperclass().getDeclaredField(
				"content");
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
     * Deserialization returns object of type SimulationResult.
     */
    @Test
    public void readResolve2() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(result);
        
        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        Object readObject = bis.readObject();
        assertTrue("wrong object type.", readObject.getClass() == SimulationResult.class);
        assertEquals("result objects not same.", result, readObject);
    }

}
