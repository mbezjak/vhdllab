package hr.fer.zemris.vhdllab.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.api.StatusCodes;

import org.junit.Test;

/**
 * A test case for {@link ServerException} class.
 * 
 * @author Miro Bezjak
 */
public class ServerExceptionTest {
	
	/**
	 * Message is <code>null</code>
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new ServerException(StatusCodes.OK, (String) null);
	}

	/**
	 * Cause is <code>null</code>
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new ServerException(StatusCodes.OK, (Throwable) null);
	}
	
	/**
	 * Message and cause are <code>null</code>
	 */
	@Test(expected = NullPointerException.class)
	public void constructor3() throws Exception {
		new ServerException(StatusCodes.OK, null, null);
	}
	
	/**
	 * A status code and message is set.
	 */
	@Test
	public void constructor4() throws Exception {
		short code = StatusCodes.OK;
		ServerException ex = new ServerException(code, "a message", null);
		assertEquals("Status codes doesn't match.", code, ex.getStatusCode());
		assertNull("Cause isn't null.", ex.getCause());
	}
	
	/**
	 * A status code and cause is set.
	 */
	@Test
	public void constructor5() throws Exception {
		short code = StatusCodes.OK;
		Throwable cause = new IllegalStateException("a message");
		ServerException ex = new ServerException(code, null, cause);
		assertEquals("Status codes doesn't match.", code, ex.getStatusCode());
		assertEquals("Cause doesn't match.", cause, ex.getCause());
		assertNull("Message isn't null.", ex.getMessage());
	}
	
}
