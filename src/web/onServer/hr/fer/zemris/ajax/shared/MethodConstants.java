package hr.fer.zemris.ajax.shared;

/**
 * This class contains constants for all known requests from Java to Ajax and
 * all responces from Ajax to Java.
 * 
 * @author Miro Bezjak
 */
public class MethodConstants {

	public static final String SEPARATOR = ".";
	public static final String METHOD_SEPARATOR = "&";
	
	public static final String PROP_METHOD = "method";
	public static final String PROP_STATUS = "status";
	public static final String STATUS_OK = "OK";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_CONTENT = "status.content";
	
	public static final String PROP_FILE_ID = "file.id";
	public static final String PROP_FILE_NAME = "file.name";
	public static final String PROP_FILE_TYPE = "file.type";
	public static final String PROP_FILE_CONTENT = "file.content";
	public static final String PROP_FILE_BELONGS_TO_PROJECT_ID = "file.project.id";
	
	public static final String PROP_PROJECT_ID = "project.id";
	public static final String PROP_PROJECT_NAME = "project.name";
	public static final String PROP_PROJECT_OWNER_ID = "project.owner.id";
	public static final String PROP_PROJECT_NMBR_FILES = "project.nmbr.files";
	
	
	
	public static final String MTD_LOAD_FILE_NAME = "load.file.name";
	public static final String MTD_LOAD_FILE_TYPE = "load.file.type";
	public static final String MTD_LOAD_FILE_CONTENT = "load.file.content";
	public static final String MTD_SAVE_FILE = "save.file";
	public static final String MTD_RENAME_FILE = "rename.file";
	
	
	/**
	 * Dont let anyone instantiate this class.
	 */
	private MethodConstants() {}

}
