/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

/**
 * This interface contains various constants for communication
 * between client and server side of VHDLLab application. Communication
 * is maintained through methods. Methods are case sensitive
 * key words. Words within a method are separated by {@link #OP_SEPARATOR}.
 * It is possible to create a method that contains multiple
 * methods (inner methods) using operators. Operators may be
 * placed only between two inner methods. Such operators are:
 * <ul>
 * <li>{@link #OP_METHOD_SEPARATOR}
 * <li>{@link #OP_REDIRECT_TO_LEFT}
 * <li>{@link #OP_REDIRECT_TO_RIGHT}
 * <li>{@link #OP_LEFT_BRACKET}
 * <li>{@link #OP_RIGHT_BRACKET}
 * </ul>
 * <p>
 * Operators have priority as follows:
 * <ul>
 * <li>highest priority: {@link #OP_LEFT_BRACKET}, {@link #OP_RIGHT_BRACKET}
 * <li>medium priority: {@link #OP_METHOD_SEPARATOR}
 * <li>lowest priority: {@link #OP_REDIRECT_TO_LEFT}, {@link #OP_REDIRECT_TO_RIGHT}
 * </ul>
 * <p>
 * Operator {@link #OP_REDIRECT_TO_RIGHT} has higher priority then 
 * {@link #OP_REDIRECT_TO_LEFT}. Furthermore, if method contains
 * two operators with same priority in a tandem then the one on
 * the left have higher priority. If there is a conflict in merging
 * then left inner method will win the conflict. A method must be written
 * in the precise format without any extra characters, even whitespace!
 * Format:
 * <blockquote>
 * (method1&method2)>method3
 * ...
 * </blockquote>
 * 
 * @author Miro Bezjak
 */
public interface MethodConstants {

	/**
	 * An operator for separating parts of a method. This
	 * operator has no priority. It is simply a part of a
	 * method.
	 * <p>
	 * Example:
	 * <blockquote>
	 * load.file.name
	 * </blockquote>
	 */
	public static final String OP_SEPARATOR = ".";
	/**
	 * An operator for merging multiple methods into one. Both
	 * methods will be run and a result properties of merged
	 * methods will contain results of both methods. It has
	 * higher priority then redirecting operators and lower then
	 * bracket operators.
	 * <p>
	 * Example:
	 * <blockquote>
	 * {@link #MTD_LOAD_FILE_NAME}&{@link #MTD_LOAD_FILE_TYPE}
	 * </blockquote>
	 * This method will return a properties that will contain both
	 * {@link #PROP_FILE_NAME} and {@link #PROP_FILE_TYPE}.
	 */
	public static final String OP_METHOD_SEPARATOR = "&";
	/**
	 * An operator for redirecting output of left method to
	 * input of right method. This operator has the lowest
	 * priority.
	 */
	public static final String OP_REDIRECT_TO_RIGHT = ">";
	/**
	 * An operator for redirecting output of right method to
	 * input of left method. This operator has the lowest
	 * priority.
	 */
	public static final String OP_REDIRECT_TO_LEFT = "<";
	/**
	 * An operator that is used for grouping. It has the highest
	 * priority among all operators.
	 */
	public static final String OP_LEFT_BRACKET= "(";
	/**
	 * An operator that is used for grouping. It has the highest
	 * priority among all operators.
	 */
	public static final String OP_RIGHT_BRACKET= ")";
	
	
	/**
	 * A value when status code is not set.
	 */
	public static final int STATUS_NOT_SET = -1;
	/**
	 * A value for status property when no errors occurred.
	 */
	public static final int STATUS_OK = 200;
	/**
	 * A status error value for status property when method arguments are faulty.
	 */
	public static final int SE_METHOD_ARGUMENT_ERROR = 400;
	public static final int SE_NO_PERMISSION = 401;
	/**
	 * A status error value for status property when parse error occurred.
	 */
	public static final int SE_PARSE_ERROR = 402;
	/**
	 * A status error value for status property when invalid method was called.
	 */
	public static final int SE_INVALID_METHOD_CALL = 404;
	
	/**
	 * A status error value for status property when internal server error occurred.
	 */
	public static final int SE_INTERNAL_SERVER_ERROR = 500;
	
	/**
	 * A status error value for status property when file was not found.
	 */
	public static final int SE_NO_SUCH_FILE = 501;
	/**
	 * A status error value for status property when file was not able to be saved.
	 */
	public static final int SE_CAN_NOT_SAVE_FILE = 502;
	/**
	 * A status error value for status property when file was not able to be renamed.
	 */
	public static final int SE_CAN_NOT_RENAME_FILE = 503;
	/**
	 * A status error value for status property when file was not able to be created.
	 */
	public static final int SE_CAN_NOT_CREATE_FILE = 504;
	/**
	 * A status error value for status property when existence of file was not able to be determine.
	 */
	public static final int SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE = 505;
	/**
	 * A status error value for status property when file can not be deleted.
	 */
	public static final int SE_CAN_NOT_DELETE_FILE = 506;
	
	public static final int SE_CAN_NOT_FIND_FILE = 507;

	
	/**
	 * A status error value for status property when project was not found.
	 */
	public static final int SE_NO_SUCH_PROJECT = 521;
	/**
	 * A status error value for status property when project was not able to be saved.
	 */
	public static final int SE_CAN_NOT_SAVE_PROJECT = 522;
	/**
	 * A status error value for status property when project was not able to be renamed.
	 */
	public static final int SE_CAN_NOT_RENAME_PROJECT = 523;
	/**
	 * A status error value for status property when project was not able to be created.
	 */
	public static final int SE_CAN_NOT_CREATE_PROJECT = 524;
	/**
	 * A status error value for status property when existence of project was not able to be determine.
	 */
	public static final int SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT = 525;
	/**
	 * A status error value for status property when project can not be deleted.
	 */
	public static final int SE_CAN_NOT_DELETE_PROJECT = 526;
	public static final int SE_CAN_NOT_FIND_PROJECT = 527;
	
	/**
	 * A status error value for status property when can not get compilation result.
	 */
	public static final int SE_CAN_NOT_GET_COMPILATION_RESULT = 551;
	/**
	 * A status error value for status property when can not get simulation result.
	 */
	public static final int SE_CAN_NOT_GET_SIMULATION_RESULT = 552;
	/**
	 * A status error value for status property when can not generate VHDL.
	 */
	public static final int SE_CAN_NOT_GENERATE_VHDL = 553;
	/**
	 * A status error value for status property when circuit interface could not be extracted.
	 */
	public static final int SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE = 554;
	/**
	 * A status error value for status property when dependencies could not be extracted.
	 */
	public static final int SE_CAN_NOT_EXTRACT_DEPENDENCIES = 555;
	/**
	 * A status error value for status property when hierarchy could not be extracted.
	 */
	public static final int SE_CAN_NOT_EXTRACT_HIERARCHY = 556;
	
	
	
	/**
	 * A file name property.
	 */
	public static final String PROP_FILE_NAME = "file.name";
	
	/**
	 * A file type property.
	 */
	public static final String PROP_FILE_TYPE = "file.type";
	
	/**
	 * A file content property.
	 */
	public static final String PROP_FILE_CONTENT = "file.content";
	
	/**
	 * A project name property.
	 */
	public static final String PROP_PROJECT_NAME = "project.name";
	
	/**
	 * An identifier parameter. Same for file, global file, user file and project identifier.
	 */
	public static final String PROP_ID = "id";

}
