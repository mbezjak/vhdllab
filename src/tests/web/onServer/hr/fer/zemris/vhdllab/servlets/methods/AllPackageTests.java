package hr.fer.zemris.vhdllab.servlets.methods;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DoMethodCompileFileTest.class,
	DoMethodCreateNewFileTest.class,
	DoMethodCreateNewGlobalFileTest.class,
	DoMethodCreateNewProjectTest.class,
	DoMethodCreateNewUserFileTest.class,
	DoMethodDeleteFileTest.class,
	DoMethodDeleteGlobalFileTest.class,
	DoMethodDeleteProjectTest.class,
	DoMethodDeleteUserFileTest.class,
	DoMethodExistsFileTest.class,
	DoMethodExistsGlobalFileTest.class,
	DoMethodExistsProjectTest.class,
	DoMethodExistsUserFileTest.class,
	DoMethodFindGlobalFilesByTypeTest.class,
	DoMethodFindProjectsByUserTest.class,
	DoMethodFindUserFilesByUserTest.class,
	DoMethodGenerateVHDLTest.class,
	DoMethodLoadFileBelongsToProjectIdTest.class,
	DoMethodLoadFileContentTest.class,
	DoMethodLoadFileNameTest.class,
	DoMethodLoadFileTypeTest.class,
	DoMethodLoadGlobalFileContentTest.class,
	DoMethodLoadGlobalFileNameTest.class,
	DoMethodLoadGlobalFileTypeTest.class,
	DoMethodLoadProjectFilesIdTest.class,
	DoMethodLoadProjectNameTest.class,
	DoMethodLoadProjectNmbrFilesTest.class,
	DoMethodLoadProjectOwnerIdTest.class,
	DoMethodLoadUserFileContentTest.class,
	DoMethodLoadUserFileOwnerIdTest.class,
	DoMethodLoadUserFileTypeTest.class,
	DoMethodRenameFileTest.class,
	DoMethodRenameGlobalFileTest.class,
	DoMethodRenameProjectTest.class,
	DoMethodRunSimulationTest.class,
	DoMethodSaveFileTest.class,
	DoMethodSaveGlobalFileTest.class,
	DoMethodSaveProjectTest.class,
	DoMethodSaveUserFileTest.class
})
public class AllPackageTests {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllPackageTests.class);
	}
	
}
