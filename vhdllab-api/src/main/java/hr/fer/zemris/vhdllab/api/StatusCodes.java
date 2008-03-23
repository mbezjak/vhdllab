package hr.fer.zemris.vhdllab.api;

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
 * @version 1.0
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
	 * A server error has occurred where server refuses to give additional
	 * information.
	 */
	public static final short SERVER_ERROR = 501;

	/**
	 * Entity doesn't exist. For example: can't retrieve entity name when it
	 * doesn't exist.
	 */
	public static final short DAO_DOESNT_EXIST = 1100;
	/**
	 * Entity with such constraints already exists. For example: saving two
	 * files with same name in same project.
	 */
	public static final short DAO_ALREADY_EXISTS = 1110;
	/**
	 * Entity name is too long.
	 */
	public static final short DAO_NAME_TOO_LONG = 1200;
	/**
	 * Entity type is too long.
	 */
	public static final short DAO_TYPE_TOO_LONG = 1210;
	/**
	 * Entity content is too long.
	 */
	public static final short DAO_CONTENT_TOO_LONG = 1220;
	/**
	 * User ID is too long.
	 */
	public static final short DAO_USER_ID_TOO_LONG = 1230;
	/**
	 * File type can't be any string. Must only be one of registered file types
	 * (see server.xml configuration file).
	 */
	public static final short DAO_INVALID_FILE_TYPE = 1300;

}
