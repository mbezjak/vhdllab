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

	public static final String METHOD_KEY = "method";
	public static final String PROPERTY_PREFIX = "property.";
	public static final String RESULT_PREFIX = "result.";
	
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
	 * A status property.
	 */
	public static final String STATUS_CODE = "status.code";
	/**
	 * A status content property used to pass a message to client describing
	 * an error that occurred.
	 */
	public static final String STATUS_MESSAGE = "status.message";
	/**
	 * A value for status property when no errors occurred.
	 */
	public static final int STATUS_OK = 0;
	/**
	 * A status error value for status property when internal server error occurred.
	 */
	public static final int SE_INTERNAL_SERVER_ERROR = 1;
	/**
	 * A status error value for status property when invalid method was called.
	 */
	public static final int SE_INVALID_METHOD_CALL = 2;
	/**
	 * A status error value for status property when parse error occurred.
	 */
	public static final int SE_PARSE_ERROR = 3;
	/**
	 * A status error value for status property when parse error occurred.
	 */
	public static final int SE_STATUS_CODE_PARSE_ERROR = -1;
	/**
	 * A status error value for status property when method arguments are faulty.
	 */
	public static final int SE_METHOD_ARGUMENT_ERROR = 4;
	
	/**
	 * A status error value for status property when file was not found.
	 */
	public static final int SE_NO_SUCH_FILE = 101;
	/**
	 * A status error value for status property when file was not able to be saved.
	 */
	public static final int SE_CAN_NOT_SAVE_FILE = 102;
	/**
	 * A status error value for status property when file was not able to be renamed.
	 */
	public static final int SE_CAN_NOT_RENAME_FILE = 103;
	/**
	 * A status error value for status property when file was not able to be created.
	 */
	public static final int SE_CAN_NOT_CREATE_FILE = 104;
	/**
	 * A status error value for status property when existence of file was not able to be determine.
	 */
	public static final int SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE = 105;
	/**
	 * A status error value for status property when file can not be deleted.
	 */
	public static final int SE_CAN_NOT_DELETE_FILE = 106;
	
	/**
	 * A status error value for status property when project was not found.
	 */
	public static final int SE_NO_SUCH_PROJECT = 201;
	/**
	 * A status error value for status property when project was not able to be saved.
	 */
	public static final int SE_CAN_NOT_SAVE_PROJECT = 202;
	/**
	 * A status error value for status property when project was not able to be renamed.
	 */
	public static final int SE_CAN_NOT_RENAME_PROJECT = 203;
	/**
	 * A status error value for status property when project was not able to be created.
	 */
	public static final int SE_CAN_NOT_CREATE_PROJECT = 204;
	/**
	 * A status error value for status property when existence of project was not able to be determine.
	 */
	public static final int SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT = 205;
	/**
	 * A status error value for status property when project can not be deleted.
	 */
	public static final int SE_CAN_NOT_DELETE_PROJECT = 206;
	
	/**
	 * A status error value for status property when can not get compilation result.
	 */
	public static final int SE_CAN_NOT_GET_COMPILATION_RESULT = 501;
	/**
	 * A status error value for status property when can not get simulation result.
	 */
	public static final int SE_CAN_NOT_GET_SIMULATION_RESULT = 502;
	/**
	 * A status error value for status property when can not generate VHDL.
	 */
	public static final int SE_CAN_NOT_GENERATE_VHDL = 503;
	/**
	 * A status error value for status property when circuit interface could not be extracted.
	 */
	public static final int SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE = 504;
	/**
	 * A status error value for status property when dependencies could not be extracted.
	 */
	public static final int SE_CAN_NOT_EXTRACT_DEPENDENCIES = 505;
	/**
	 * A status error value for status property when hierarchy could not be extracted.
	 */
	public static final int SE_CAN_NOT_EXTRACT_HIERARCHY = 506;
	
	
	
	/**
	 * A file ID property.
	 */
	public static final String PROP_FILE_ID = "file.id";
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
	 * A file exists property. Contains <code>1</code> if file exists;
	 * <code>0</code> otherwise.
	 */
	public static final String PROP_FILE_EXISTS = "file.exists";
	/**
	 * A file owner ID property.
	 */
	public static final String PROP_FILE_OWNER_ID = "file.owner.id";
	
	/**
	 * A project ID property.
	 */
	public static final String PROP_PROJECT_ID = "project.id";
	/**
	 * A project name property.
	 */
	public static final String PROP_PROJECT_NAME = "project.name";
	/**
	 * A project owner ID property.
	 */
	public static final String PROP_PROJECT_OWNER_ID = "project.owner.id";
	/**
	 * A project number of files property.
	 */
	public static final String PROP_PROJECT_NMBR_FILES = "project.nmbr.files";
	/**
	 * A project exists property. Contains <code>1</code> if project exists;
	 * <code>0</code> otherwise.
	 */
	public static final String PROP_PROJECT_EXISTS = "project.exists";
	
	/**
	 * A compilation result serialization property.
	 */
	public static final String PROP_RESULT_COMPILATION_SERIALIZATION = "result.compilation.serialization";
	/**
	 * A simulation result serialization property.
	 */
	public static final String PROP_RESULT_SIMULATION_SERIALIZATION = "result.simulation.serialization";

	/**
	 * A hierarchy serialization property.
	 */
	public static final String PROP_HIERARCHY_SERIALIZATION = "hierarchy.serialization";

	/**
	 * A value for generate VHDL property.
	 */
	public static final String PROP_GENERATED_VHDL = "generated.vhdl";
	
	/**
	 * A value for entity name of circuit interface.
	 */
	public static final String PROP_CI_ENTITY_NAME = "ci.entity.name";
	/**
	 * A value for port name.
	 */
	public static final String PROP_CI_PORT_NAME = "ci.port.name";
	/**
	 * A value for port direction.
	 */
	public static final String PROP_CI_PORT_DIRECTION = "ci.port.direction";
	/**
	 * A value for type name of a port.
	 */
	public static final String PROP_CI_PORT_TYPE_NAME = "ci.port.type.name";
	/**
	 * A value for range from of a port.
	 */
	public static final String PROP_CI_PORT_TYPE_RANGE_FROM = "ci.port.type.range.from";
	/**
	 * A value for range to of a port.
	 */
	public static final String PROP_CI_PORT_TYPE_RANGE_TO = "ci.port.type.range.to";
	/**
	 * A value for vector direction of a port.
	 */
	public static final String PROP_CI_PORT_TYPE_VECTOR_DIRECTION = "ci.port.type.vector.direction";


}
