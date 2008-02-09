package hr.fer.zemris.vhdllab.server.api;

/**
 * This class contains status code constants. They describe a status of a
 * response (sent by server). Almost all of them describe what type of error
 * occurred. Only one is defined as {@link #OK} status describing that request
 * (sent by user - client application) was successfully processed. Other status
 * codes describe why an error occurred. For example:
 * {@link #DAO_CONTENT_TOO_LONG} describe that error occurred because user tried
 * to save resource whose content is too long for server to accept it. Note that
 * these status codes are similar to HTTP status codes.
 * <p>
 * Currently only [0, 4000] status code range is in use for only dozens of
 * status codes. Here is a description of every status code range:
 * <ul>
 * <li>[0, 1000) - special range (global - for every tier)</li>
 * <li>[1000, 2000) - errors in dao tier</li>
 * <li>[2000, 3000) - errors in service tier</li>
 * <li>[3000, 4000) - errors in presentation tier</li>
 * </ul>
 * </p>
 * 
 * @author Miro Bezjak
 * @version 0.1
 * @since 6/2/2008
 */
public final class StatusCodes {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private StatusCodes() {
	}

	/**
	 * Indicates that a request (sent by client application) is successfully
	 * processed.
	 */
	public static final short OK = 200;
	/**
	 * An unknown error occurred on server.
	 */
	public static final short INTERNAL_SERVER_ERROR = 500;
	/**
	 * A server error has occurred where server refuses to give additional information.
	 */
	public static final short SERVER_ERROR = 501;
	
	public static final short DAO_CREATE_ERROR = 1100;
	
	public static final short DAO_ALREADY_EXISTS = 1101;
	
	public static final short DAO_CONTENT_TOO_LONG = 1000;

}
