package hr.fer.zemris.vhdllab.api.results;

import hr.fer.zemris.vhdllab.api.results.CompilationMessage;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;

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
 * A test case for {@link CompilationResult}.
 * 
 * @author Miro Bezjak
 */
public class CompilationResultTest {

	private static final Integer STATUS = Integer.valueOf(5);
	private static final boolean SUCCESSFUL = false;
	private static final List<CompilationMessage> MESSAGES = Collections
			.emptyList();

	private static CompilationResult result;

	@Before
	public void initEachTest() {
		result = new CompilationResult(STATUS, SUCCESSFUL, MESSAGES);
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = NullPointerException.class)
	public void readResolve() throws Exception {
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
