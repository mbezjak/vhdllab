package hr.fer.zemris.ajax.shared;

/**
 * This class contains various constants for communication
 * between Java applet and Servlet through AJAX. Communication
 * is maintained through methods. Methods are case sensitive
 * key words. Words within methods are separated by {@link #OP_SEPARATOR}.
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
 * in the precise format without any extra charaters, even whitespace!
 * Format:
 * <blockquote>
 * (method1&method2)>method3
 * ...
 * </blockquote>
 * 
 * @author Miro Bezjak
 */
public class MethodConstants {
	
	/**
	 * An operator for separating parts of a method. This
	 * operator has no priority. It is simply a part of a
	 * method.
	 * <p>
	 * Example:
	 * <blockquote>
	 * load.file.name
	 * </blockquote>
 	 * This method will load a name of a file and is actualy
 	 * a {@link #MTD_LOAD_FILE_NAME} method.
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
	 * A method property.
	 */
	public static final String PROP_METHOD = "method";
	/**
	 * A status property.
	 */
	public static final String PROP_STATUS = "status";
	/**
	 * A status content property used to pass a message to client describing
	 * an error that occured.
	 */
	public static final String PROP_STATUS_CONTENT = "status.content";
	/**
	 * A value for status property when no errors occured.
	 */
	public static final String STATUS_OK = "0";
	/**
	 * A status error value for status property when internal server error occured.
	 */
	public static final String SE_INTERNAL_SERVER_ERROR = "1";
	/**
	 * A status error value for status property when invalid method was called.
	 */
	public static final String SE_INVALID_METHOD_CALL = "2";
	/**
	 * A status error value for status property when parse error occured.
	 */
	public static final String SE_PARSE_ERROR = "3";
	/**
	 * A status error value for status property when method arguments are faulty.
	 */
	public static final String SE_METHOD_ARGUMENT_ERROR = "4";
	/**
	 * A status error value for status property when there are type inconsistencies.
	 */
	public static final String SE_TYPE_SAFETY = "5";
	
	/**
	 * A status error value for status property when file was not found.
	 */
	public static final String SE_NO_SUCH_FILE = "101";
	/**
	 * A status error value for status property when file was not able to be saved.
	 */
	public static final String SE_CAN_NOT_SAVE_FILE = "102";
	/**
	 * A status error value for status property when file was not able to be renamed.
	 */
	public static final String SE_CAN_NOT_RENAME_FILE = "103";
	/**
	 * A status error value for status property when file was not able to be created.
	 */
	public static final String SE_CAN_NOT_CREATE_FILE = "104";
	/**
	 * A status error value for status property when existance of file was not able to be determine.
	 */
	public static final String SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE = "105";
	/**
	 * A status error value for status property when file can not be deleted.
	 */
	public static final String SE_CAN_NOT_DELETE_FILE = "106";
	
	/**
	 * A status error value for status property when project was not found.
	 */
	public static final String SE_NO_SUCH_PROJECT = "201";
	/**
	 * A status error value for status property when project was not able to be saved.
	 */
	public static final String SE_CAN_NOT_SAVE_PROJECT = "202";
	/**
	 * A status error value for status property when project was not able to be renamed.
	 */
	public static final String SE_CAN_NOT_RENAME_PROJECT = "203";
	/**
	 * A status error value for status property when project was not able to be created.
	 */
	public static final String SE_CAN_NOT_CREATE_PROJECT = "204";
	/**
	 * A status error value for status property when existance of project was not able to be determine.
	 */
	public static final String SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT = "205";
	/**
	 * A status error value for status property when project can not be deleted.
	 */
	public static final String SE_CAN_NOT_DELETE_PROJECT = "206";
	
	/**
	 * A status error value for status property when can not get compilation result.
	 */
	public static final String SE_CAN_NOT_GET_COMPILATION_RESULT = "501";
	/**
	 * A status error value for status property when can not get simulation result.
	 */
	public static final String SE_CAN_NOT_GET_SIMULATION_RESULT = "502";
	/**
	 * A status error value for status property when can not generate VHDL.
	 */
	public static final String SE_CAN_NOT_GENERATE_VHDL = "503";
	/**
	 * A status error value for status property when circuit interface could not be extracted.
	 */
	public static final String SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE = "504";
	/**
	 * A status error value for status property when dependencies could not be extracted.
	 */
	public static final String SE_CAN_NOT_EXTRACT_DEPENDENCIES = "505";
	
	
	
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
	 * A result status property.
	 */
	public static final String PROP_RESULT_STATUS = "result.status";
	/**
	 * A result isSuccessful property.
	 */
	public static final String PROP_RESULT_IS_SUCCESSFUL = "result.is.successful";
	/**
	 * A simulation result waveform property.
	 */
	public static final String PROP_RESULT_WAVEFORM = "result.waveform";
	/**
	 * A result message text property.
	 */
	public static final String PROP_RESULT_MESSAGE_TEXT = "result.message.text";
	/**
	 * A compilation result message row property.
	 */
	public static final String PROP_RESULT_MESSAGE_ROW = "result.message.row";
	/**
	 * A compilation result message column property.
	 */
	public static final String PROP_RESULT_MESSAGE_COLUMN = "result.message.column";
	/**
	 * A result message type property. Type can be:
	 * <ul>
	 * <li>simulation
	 * <li>compilation
	 * <li>compilation warning
	 * <li>compilation error
	 * </ul>
	 */
	public static final String PROP_RESULT_MESSAGE_TYPE = "result.message.type";
	/**
	 * A value for result message type property. Value is simulation. This is normaly
	 * unnecessary.
	 */
	public static final String PROP_MESSAGE_TYPE_SIMULATION = "message.type.simulation";
	/**
	 * A value for result message type property. Value is compilation.
	 */
	public static final String PROP_MESSAGE_TYPE_COMPILATION = "message.type.compilation";
	/**
	 * A value for result message type property. Value is compilation warning.
	 */
	public static final String PROP_MESSAGE_TYPE_COMPILATION_WARNING = "message.type.compilation.warning";
	/**
	 * A value for result message type property. Value is compilation error.
	 */
	public static final String PROP_MESSAGE_TYPE_COMPILATION_ERROR = "message.type.compilation.error";
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

	
	/**
	 * A "load file name" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_NAME} - containing file name
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_FILE_NAME = "load.file.name";
	/**
	 * A "load file type" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_TYPE} - containing file type
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_FILE_TYPE = "load.file.type";
	/**
	 * A "load file content" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_CONTENT} - containing file content or empty string if file content is <code>null</code>.
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_FILE_CONTENT = "load.file.content";
	/**
	 * A "load file belongs to project id" method. Expected parametars (written as a property)
	 * are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_PROJECT_ID} - containing project id
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_FILE_BELONGS_TO_PROJECT_ID = "load.file.project.id";
	/**
	 * A "save file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * <li>{@link #PROP_FILE_CONTENT}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_SAVE_FILE} - if file with {@link #PROP_FILE_ID} could not be saved
	 * </ul>
	 */
	public static final String MTD_SAVE_FILE = "save.file";
	/**
	 * A "rename file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * <li>{@link #PROP_FILE_NAME}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_RENAME_FILE} - if file with {@link #PROP_FILE_ID} could not be renamed
	 * </ul>
	 */
	public static final String MTD_RENAME_FILE = "rename.file";
	/**
	 * A "create file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * <li>{@link #PROP_FILE_NAME}
	 * <li>{@link #PROP_FILE_TYPE}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_ID} - containing an ID of created file
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain all three parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_CREATE_FILE} - if file could not be created
	 * </ul>
	 */
	public static final String MTD_CREATE_NEW_FILE = "create.file";
	/**
	 * A "exists file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * <li>{@link #PROP_FILE_NAME}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_EXISTS} - containing <code>1</code> if file exists; <code>0</code> otherwise
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE} - if existance of file can not be determined
	 * </ul>
	 */
	public static final String MTD_EXISTS_FILE = "exists.file";
	/**
	 * A "find file by name" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * <li>{@link #PROP_FILE_NAME}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_ID} - containing a file
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_NAME} and {@link #PROP_PROJECT_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_FIND_FILE_BY_NAME = "find.file.by.name";
	/**
	 * A "find files by project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>list of {@link #PROP_FILE_ID} written in following format: {@link #PROP_FILE_ID}.[number]
	 *     (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = find.files.by.project<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_FILE_ID}.1 = 104<br/>
	 * {@link #PROP_FILE_ID}.2 = 3<br/>
	 * {@link #PROP_FILE_ID}.3 = 55<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_OWNER_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if projects with {@link #PROP_PROJECT_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_FIND_FILES_BY_PROJECT = "find.files.by.project";
	/**
	 * A "delete file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DELETE_FILE} - if file with {@link #PROP_FILE_ID} could not be deleted
	 * </ul>
	 */
	public static final String MTD_DELETE_FILE = "delete.file";
	
	/**
	 * A "load global file name" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_NAME} - containing file name
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_GLOBAL_FILE_NAME = "load.global.file.name";
	/**
	 * A "load global file type" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_TYPE} - containing file type
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_GLOBAL_FILE_TYPE = "load.global.file.type";
	/**
	 * A "load global file content" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_CONTENT} - containing file content or empty string if file content is <code>null</code>.
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_GLOBAL_FILE_CONTENT = "load.global.file.content";
	/**
	 * A "save global file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * <li>{@link #PROP_FILE_CONTENT}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_SAVE_FILE} - if file with {@link #PROP_FILE_ID} could not be saved
	 * </ul>
	 */
	public static final String MTD_SAVE_GLOBAL_FILE = "save.global.file";
	/**
	 * A "rename global file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * <li>{@link #PROP_FILE_NAME}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_RENAME_FILE} - if file with {@link #PROP_FILE_ID} could not be renamed
	 * </ul>
	 */
	public static final String MTD_RENAME_GLOBAL_FILE = "rename.global.file";
	/**
	 * A "create global file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_NAME}
	 * <li>{@link #PROP_FILE_TYPE}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_ID} - containing an ID of created file
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_CAN_NOT_CREATE_FILE} - if file could not be created
	 * </ul>
	 */
	public static final String MTD_CREATE_NEW_GLOBAL_FILE = "create.global.file";
	/**
	 * A "exists global file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_EXISTS} - containing <code>1</code> if file exists; <code>0</code> otherwise
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE} - if existance of file can not be determined
	 * </ul>
	 */
	public static final String MTD_EXISTS_GLOBAL_FILE = "exists.global.file";
	/**
	 * A "delete global file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DELETE_FILE} - if file with {@link #PROP_FILE_ID} could not be deleted
	 * </ul>
	 */
	public static final String MTD_DELETE_GLOBAL_FILE = "delete.global.file";
	/**
	 * A "find global files by type" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_TYPE}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>list of {@link #PROP_FILE_ID} written in following format: {@link #PROP_FILE_ID}.[number]
	 *     (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = find.global.files.by.type<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_FILE_ID}.1 = 104<br/>
	 * {@link #PROP_FILE_ID}.2 = 3<br/>
	 * {@link #PROP_FILE_ID}.3 = 55<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_TYPE} could not be found
	 * </ul>
	 */
	public static final String MTD_FIND_GLOBAL_FILES_BY_TYPE = "find.global.files.by.type";

	/**
	 * A "load user file owner id" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_OWNER_ID} - containing file owner id
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_USER_FILE_OWNER_ID = "load.user.file.owner.id";
	/**
	 * A "load user file type" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_TYPE} - containing file type
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_USER_FILE_TYPE = "load.user.file.type";
	/**
	 * A "load user file content" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_CONTENT} - containing file content or empty string if file content is <code>null</code>.
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_USER_FILE_CONTENT = "load.user.file.content";
	/**
	 * A "save user file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * <li>{@link #PROP_FILE_CONTENT}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_SAVE_FILE} - if file with {@link #PROP_FILE_ID} could not be saved
	 * </ul>
	 */
	public static final String MTD_SAVE_USER_FILE = "save.user.file";
	/**
	 * A "create user file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_OWNER_ID}
	 * <li>{@link #PROP_FILE_TYPE}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_ID} - containing an ID of created file
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_OWNER_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_CREATE_FILE} - if file could not be created
	 * </ul>
	 */
	public static final String MTD_CREATE_NEW_USER_FILE = "create.user.file";
	/**
	 * A "exists user file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_FILE_EXISTS} - containing <code>1</code> if file exists; <code>0</code> otherwise
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE} - if existance of file can not be determined
	 * </ul>
	 */
	public static final String MTD_EXISTS_USER_FILE = "exists.user.file";
	/**
	 * A "delete user file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DELETE_FILE} - if file with {@link #PROP_FILE_ID} could not be deleted
	 * </ul>
	 */
	public static final String MTD_DELETE_USER_FILE = "delete.user.file";
	/**
	 * A "find user files by user" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_OWNER_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>list of {@link #PROP_FILE_ID} written in following format: {@link #PROP_FILE_ID}.[number]
	 *     (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = find.user.files.by.user<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_FILE_ID}.1 = 104<br/>
	 * {@link #PROP_FILE_ID}.2 = 3<br/>
	 * {@link #PROP_FILE_ID}.3 = 55<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_OWNER_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_FILE} - if no file with {@link #PROP_FILE_OWNER_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_FIND_USER_FILES_BY_USER = "find.user.files.by.user";

	/**
	 * A "load project name" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_PROJECT_NAME} - containing project name
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_PROJECT_NAME = "load.project.name";
	/**
	 * A "load project owner id" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_PROJECT_OWNER_ID} - containing project owner id
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_PROJECT_OWNER_ID = "load.project.owner.id";
	/**
	 * A "load project number of files" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_PROJECT_NMBR_FILES} - containing project number of files
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_PROJECT_NMBR_FILES = "load.project.nmbr.files";
	/**
	 * A "load project's id of files" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>list of {@link #PROP_FILE_ID} written in following format: {@link #PROP_FILE_ID}.[number]
	 *     (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = load.project.files.id<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_FILE_ID}.1 = 23<br/>
	 * {@link #PROP_FILE_ID}.2 = 545<br/>
	 * {@link #PROP_FILE_ID}.3 = 7<br/>
	 * ...
	 * </blockquote>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_LOAD_PROJECT_FILES_ID = "load.project.files.id";
	/**
	 * A "save project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * <li>either {@link #PROP_FILE_ID} or a list of {@link #PROP_FILE_ID} written in
	 *     following format: {@link #PROP_FILE_ID}.[number] (number is positive and
	 *     starts at 1) also a combination of both
	 * </ul>
	 * <p>
	 * Example of request <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_PROJECT_ID} = 1<br/>
	 * {@link #PROP_FILE_ID} = 156<br/>
	 * {@link #PROP_FILE_ID}.1 = 174<br/>
	 * {@link #PROP_FILE_ID}.2 = 51<br/>
	 * ...
	 * </blockquote>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} or {@link #PROP_FILE_ID} are not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be found
	 * <li>{@link #SE_NO_SUCH_FILE} - if file with {@link #PROP_FILE_ID} could not be found
	 * <li>{@link #SE_CAN_NOT_SAVE_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be saved
	 * </ul>
	 */
	public static final String MTD_SAVE_PROJECT = "save.project";
	/**
	 * A "rename project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * <li>{@link #PROP_PROJECT_NAME}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_RENAME_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be renamed
	 * </ul>
	 */
	public static final String MTD_RENAME_PROJECT = "rename.project";
	/**
	 * A "create project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_NAME}
	 * <li>{@link #PROP_PROJECT_OWNER_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_PROJECT_ID} - containing an ID of created project
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_OWNER_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_CREATE_PROJECT} - if project could not be created
	 * </ul>
	 */
	public static final String MTD_CREATE_NEW_PROJECT = "create.project";
	/**
	 * A "exists project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_PROJECT_EXISTS} - containing <code>1</code> if project exists; <code>0</code> otherwise
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT} - if existance of project can not be determined
	 * </ul>
	 */
	public static final String MTD_EXISTS_PROJECT = "exists.project";
	/**
	 * A "delete project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_DELETE_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be deleted
	 * </ul>
	 */
	public static final String MTD_DELETE_PROJECT = "delete.project";
	/**
	 * A "find project by user" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_OWNER_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>list of {@link #PROP_PROJECT_ID} written in following format: {@link #PROP_PROJECT_ID}.[number]
	 *     (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = find.projects.by.user<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_PROJECT_ID}.1 = 104<br/>
	 * {@link #PROP_PROJECT_ID}.2 = 3<br/>
	 * {@link #PROP_PROJECT_ID}.3 = 55<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_OWNER_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if projects with {@link #PROP_PROJECT_OWNER_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_FIND_PROJECTS_BY_USER = "find.projects.by.user";
	
	/**
	 * A "compile file" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_RESULT_STATUS} - containing result status
	 * <li>{@link #PROP_RESULT_IS_SUCCESSFUL} - containing <code>1</code> if compilation
	 *     finished successfully; <code>0</code> otherwise
	 * <li>list of {@link #PROP_RESULT_MESSAGE_TEXT} written in following format:
	 *     {@link #PROP_RESULT_MESSAGE_TEXT}.[number] (number is positive and starts at 1)
	 * <li>list of {@link #PROP_RESULT_MESSAGE_ROW} written in following format:
	 *     {@link #PROP_RESULT_MESSAGE_ROW}.[number] (number is positive and starts at 1)
	 * <li>list of {@link #PROP_RESULT_MESSAGE_COLUMN} written in following format:
	 *     {@link #PROP_RESULT_MESSAGE_COLUMN}.[number] (number is positive and starts at 1)
	 * <li>list of {@link #PROP_RESULT_MESSAGE_TYPE} written in following format:
	 *     {@link #PROP_RESULT_MESSAGE_TYPE}.[number] (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = compile.file<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_RESULT_STATUS} = result_status<br/>
	 * {@link #PROP_RESULT_IS_SUCCESSFUL} = 0<br/>
	 * {@link #PROP_RESULT_MESSAGE_TEXT}.1 = message_text_1<br/>
	 * {@link #PROP_RESULT_MESSAGE_ROW}.1 = message_row_1<br/>
	 * {@link #PROP_RESULT_MESSAGE_COLUMN}.1 = message_column_1<br/>
	 * {@link #PROP_RESULT_MESSAGE_TYPE}.1 = message_type_1<br/>
	 * {@link #PROP_RESULT_MESSAGE_TEXT}.2 = message_text_2<br/>
	 * {@link #PROP_RESULT_MESSAGE_ROW}.2 = message_row_2<br/>
	 * {@link #PROP_RESULT_MESSAGE_COLUMN}.2 = message_column_2<br/>
	 * {@link #PROP_RESULT_MESSAGE_TYPE}.2 = message_type_2<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_GET_COMPILATION_RESULT} - if can not get compilation result for a file with {@link #PROP_FILE_ID}
	 * <li>{@link #SE_TYPE_SAFETY} - if found non-compilation type message in compilation result
	 * </ul>
	 */
	public static final String MTD_COMPILE_FILE = "compile.file";
	/**
	 * A "run simulation" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_RESULT_STATUS} - containing result status
	 * <li>{@link #PROP_RESULT_IS_SUCCESSFUL} - containing <code>1</code> if compilation
	 *     finished successfully; <code>0</code> otherwise
	 * <li>{@link #PROP_RESULT_WAVEFORM} - containing result waveform
	 * <li>list of {@link #PROP_RESULT_MESSAGE_TEXT} written in following format:
	 *     {@link #PROP_RESULT_MESSAGE_TEXT}.[number] (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = run.simulation<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_RESULT_STATUS} = result_status<br/>
	 * {@link #PROP_RESULT_IS_SUCCESSFUL} = 0<br/>
	 * {@link #PROP_RESULT_WAVEFORM} = result_waveform<br/>
	 * {@link #PROP_RESULT_MESSAGE_TEXT}.1 = message_text_1<br/>
	 * {@link #PROP_RESULT_MESSAGE_TEXT}.2 = message_text_2<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_GET_SIMULATION_RESULT} - if can not get simulation result for a file with {@link #PROP_FILE_ID}
	 * <li>{@link #SE_TYPE_SAFETY} - if found non-simulation type message in simulation result
	 * </ul>
	 */
	public static final String MTD_RUN_SIMULATION = "run.simulation";
	/**
	 * A "generate VHDL" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_GENERATED_VHDL} - containing generated VHDL
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_FILE_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_GENERATE_VHDL} - if can not generate VHDL for file with {@link #PROP_FILE_ID}
	 * </ul>
	 */
	public static final String MTD_GENERATE_VHDL = "generate.vhdl";
	/**
	 * A "extract circuit interface" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>{@link #PROP_CI_ENTITY_NAME} - containing entity name of a circuit interface
	 * <li>list of
	 * 		<ul>
	 * 		<li>{@link #PROP_CI_PORT_NAME}
	 * 		<li>{@link #PROP_CI_PORT_DIRECTION}
	 * 		<li>{@link #PROP_CI_PORT_TYPE_NAME}
	 * 		<li>{@link #PROP_CI_PORT_TYPE_RANGE_FROM}
	 * 		<li>{@link #PROP_CI_PORT_TYPE_RANGE_TO}
	 * 		<li>{@link #PROP_CI_PORT_TYPE_VECTOR_DIRECTION}
	 * 		</ul>
	 *     written in following format: property.[number] (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = extract.circuit.interface<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_CI_ENTITY_NAME} = circuit_1<br/>
	 * {@link #PROP_CI_PORT_NAME}.1 = a<br/>
	 * {@link #PROP_CI_PORT_DIRECTION}.1 = IN<br/>
	 * {@link #PROP_CI_PORT_TYPE_NAME}.1 = std_logic<br/>
	 * {@link #PROP_CI_PORT_TYPE_RANGE_FROM}.1 = 0<br/>
	 * {@link #PROP_CI_PORT_TYPE_RANGE_TO}.1 = 0<br/>
	 * {@link #PROP_CI_PORT_TYPE_VECTOR_DIRECTION}.1 = DOWNTO<br/>
	 * {@link #PROP_CI_PORT_NAME}.2 = b<br/>
	 * {@link #PROP_CI_PORT_DIRECTION}.2 = OUT<br/>
	 * {@link #PROP_CI_PORT_TYPE_NAME}.2 = std_logic_vector<br/>
	 * {@link #PROP_CI_PORT_TYPE_RANGE_FROM}.2 = 0<br/>
	 * {@link #PROP_CI_PORT_TYPE_RANGE_TO}.2 = 3<br/>
	 * {@link #PROP_CI_PORT_TYPE_VECTOR_DIRECTION}.2 = TO<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE} - if circuit interface for file with {@link #PROP_FILE_ID} could not be extracted
	 * </ul>
	 */
	public static final String MTD_EXTRACT_CIRCUIT_INTERFACE = "extract.circuit.interface";
	/**
	 * A "extract dependencies" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_FILE_ID}
	 * </ul>
	 * <p>
	 * If no error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing {@link #STATUS_OK}
	 * <li>list of {@link #PROP_FILE_ID} written in following format: {@link #PROP_FILE_ID}.[number]
	 *     (number is positive and starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = extract.dependencies<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_FILE_ID}.1 = 104<br/>
	 * {@link #PROP_FILE_ID}.2 = 3<br/>
	 * {@link #PROP_FILE_ID}.3 = 55<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #PROP_STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain both parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} is not a long int number
	 * <li>{@link #SE_CAN_NOT_EXTRACT_DEPENDENCIES} - if dependencies for file with {@link #PROP_FILE_ID} could not be extracted
	 * </ul>
	 */
	public static final String MTD_EXTRACT_DEPENDENCIES = "extract.dependencies";
	
	/**
	 * Dont let anyone instantiate this class.
	 */
	private MethodConstants() {}

}