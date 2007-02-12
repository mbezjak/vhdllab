package hr.fer.zemris.vhdllab.model;

/**
 * Any change in this class must reflect upon a change in adequate model
 * (package hr.fer.zemris.vhdllab.dao.model)!
 * @author Miro Bezjak
 */
public class ModelUtil {

	public static final int USER_ID_SIZE = 255;
	public static final int PROJECT_NAME_SIZE = 255;
	public static final int FILE_NAME_SIZE = 255;
	public static final int FILE_TYPE_SIZE = 255;
	public static final int FILE_CONTENT_SIZE = 65535;
	public static final int GLOBAL_FILE_NAME_SIZE = 255;
	public static final int GLOBAL_FILE_TYPE_SIZE = 255;
	public static final int GLOBAL_FILE_CONTENT_SIZE = 65535;
	public static final int USER_FILE_NAME_SIZE = 255;
	public static final int USER_FILE_TYPE_SIZE = 255;
	public static final int USER_FILE_CONTENT_SIZE = 65535;

	public static boolean userIdAreEqual(String ownerId1, String ownerId2) {
		return ownerId1.equalsIgnoreCase(ownerId2);
	}
	
	public static boolean projectNamesAreEqual(String projectName1, String projectName2) {
		return projectName1.equalsIgnoreCase(projectName2);
	}

	public static boolean fileNamesAreEqual(String fileName1, String fileName2) {
		return fileName1.equalsIgnoreCase(fileName2);
	}
	
	public static boolean fileTypesAreEqual(String fileType1, String fileType2) {
		return fileType1.equalsIgnoreCase(fileType2);
	}

	public static boolean fileContentsAreEqual(String fileContent1, String fileContent2) {
		return fileContent1.equals(fileContent2);
	}
	
	public static boolean globalFileNamesAreEqual(String fileName1, String fileName2) {
		return fileName1.equalsIgnoreCase(fileName2);
	}
	
	public static boolean globalFileTypesAreEqual(String fileType1, String fileType2) {
		return fileType1.equalsIgnoreCase(fileType2);
	}

	public static boolean globalFileContentsAreEqual(String fileContent1, String fileContent2) {
		return fileContent1.equals(fileContent2);
	}
	
	public static boolean userFileNamesAreEqual(String fileName1, String fileName2) {
		return fileName1.equalsIgnoreCase(fileName2);
	}
	
	public static boolean userFileTypesAreEqual(String fileType1, String fileType2) {
		return fileType1.equalsIgnoreCase(fileType2);
	}

	public static boolean userFileContentsAreEqual(String fileContent1, String fileContent2) {
		return fileContent1.equals(fileContent2);
	}
	
}
