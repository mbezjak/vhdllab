package hr.fer.zemris.vhdllab.server.results;

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
 * A test case for {@link VHDLGenerationResult}.
 * 
 * @author Miro Bezjak
 */
public class VHDLGenerationResultTest {

	private static final Integer STATUS = Integer.valueOf(5);
	private static final boolean SUCCESSFUL = false;
	private static final List<VHDLGenerationMessage> MESSAGES = Collections
			.emptyList();
	private static final String CONTENT = "a result content";

	private static VHDLGenerationResult result;

	@Before
	public void initEachTest() {
		result = new VHDLGenerationResult(STATUS, SUCCESSFUL, MESSAGES, CONTENT);
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
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

}
