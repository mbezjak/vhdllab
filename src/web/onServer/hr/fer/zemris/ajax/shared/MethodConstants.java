package hr.fer.zemris.ajax.shared;

/**
 * This class contains various constants for all known requests
 * from Java to Ajax and all responces from Ajax to Java.
 * 
 * - case insensitive
 * - parsing priority: left to right
 * 
 * @author Miro Bezjak
 */
public class MethodConstants {

	/**
	 * A separator for separating parts of a method.
	 */
	public static final String SEPARATOR = ".";
	/**
	 * A separator for merging multiple methods into one.
	 */
	public static final String METHOD_SEPARATOR = "&";
	/**
	 * Redirect output of left method to input of right method
	 */
	public static final String METHOD_REDIRECT_TO_RIGHT = ">";
	/**
	 * Redirect output of right method to input of left method
	 */
	public static final String METHOD_REDIRECT_TO_LEFT = "<";
	
	
	/**
	 * A method property.
	 */
	public static final String PROP_METHOD = "method";
	/**
	 * A status property.
	 */
	public static final String PROP_STATUS = "status";
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
	 * A status content property used to pass a message to client describing
	 * an error that occured.
	 */
	public static final String STATUS_CONTENT = "status.content";
	
	
	
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
	 * A file exists property. Contains <code>true</code> if file exists;
	 * <code>false</code> otherwise.
	 */
	public static final String PROP_FILE_EXISTS = "file.exists";
	
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
	 * A project exists property. Contains <code>true</code> if project exists;
	 * <code>false</code> otherwise.
	 */
	public static final String PROP_PROJECT_EXISTS = "project.exists";
	
	
	
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #PROP_FILE_NAME} - containing file type
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #PROP_FILE_NAME} - containing file name
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #PROP_FILE_ID} - containing an ID of a created file
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #PROP_FILE_EXISTS} - containing <code>true</code> if file exists; <code>false</code> otherwise
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * A "save project" method. Expected parametars (written as a property) are
	 * <ul>
	 * <li>{@link #PROP_PROJECT_ID}
	 * <li>list of {@link #PROP_FILE_ID} written in following format: {@link #PROP_FILE_ID}.number
	 *     (number starts at 1)
	 * </ul>
	 * <p>
	 * Example of request <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_PROJECT_ID} = project_ID<br/>
	 * {@link #PROP_FILE_ID}.1 = file_ID_1<br/>
	 * {@link #PROP_FILE_ID}.2 = file_ID_2<br/>
	 * {@link #PROP_FILE_ID}.3 = file_ID_3<br/>
	 * {@link #PROP_FILE_ID}.4 = file_ID_4<br/>
	 * {@link #PROP_FILE_ID}.5 = file_ID_5<br/>
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain parametars
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_ID} or {@link #PROP_FILE_ID} are not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if project with {@link #PROP_PROJECT_ID} could not be found
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
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #PROP_PROJECT_ID} - containing an ID of a created project
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>{@link #PROP_PROJECT_EXISTS} - containing <code>true</code> if project exists; <code>false</code> otherwise
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
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
	 * <li>list of {@link #PROP_PROJECT_ID} written in following format: {@link #PROP_PROJECT_ID}.number
	 *     (number starts at 1)
	 * </ul>
	 * <p>
	 * Example of response <code>Properties</code>:
	 * <blockquote>
	 * {@link #PROP_METHOD} = find_projects_b<_user_method<br/>
	 * {@link #PROP_STATUS} = {@link #STATUS_OK}<br/>
	 * {@link #PROP_PROJECT_ID}.1 = project_ID_1<br/>
	 * {@link #PROP_PROJECT_ID}.2 = project_ID_2<br/>
	 * {@link #PROP_PROJECT_ID}.3 = project_ID_3<br/>
	 * {@link #PROP_PROJECT_ID}.4 = project_ID_4<br/>
	 * {@link #PROP_PROJECT_ID}.5 = project_ID_5<br/>
	 * ...
	 * </blockquote>
	 * </ul>
	 * <p>
	 * However if error occured, a returned <code>Properties</code> will contain following
	 * property
	 * <ul>
	 * <li>{@link #PROP_METHOD} - containing this method request
	 * <li>{@link #PROP_STATUS} - containing one of status errors
	 * <li>{@link #STATUS_CONTENT} - containing a message that describes an error message
	 * </ul>
	 * <p>
	 * This method may cause following status errors:
	 * <ul>
	 * <li>{@link #SE_METHOD_ARGUMENT_ERROR} - if method does not contain a parametar
	 * <li>{@link #SE_PARSE_ERROR} - if {@link #PROP_PROJECT_OWNER_ID} is not a long int number
	 * <li>{@link #SE_NO_SUCH_PROJECT} - if projects with {@link #PROP_PROJECT_OWNER_ID} could not be found
	 * </ul>
	 */
	public static final String MTD_FIND_PROJECTS_BY_USER = "find.project.by.user";
	
	/**
	 * Dont let anyone instantiate this class.
	 */
	private MethodConstants() {}

}