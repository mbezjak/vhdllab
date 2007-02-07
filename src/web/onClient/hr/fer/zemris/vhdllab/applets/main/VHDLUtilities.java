package hr.fer.zemris.vhdllab.applets.main;

public class VHDLUtilities {

	public static boolean equalsProjectNames(String projectName1, String projectName2) {
		return projectName1.equals(projectName2);
	}

	public static boolean equalsFileNames(String fileName1, String fileName2) {
		return fileName1.equalsIgnoreCase(fileName2);
	}

}
